package org.onosproject.p4virtex.impl.service;

import com.google.common.collect.Lists;
import org.onlab.packet.Ip6Address;
import org.onlab.packet.Ip6Prefix;
import org.onlab.packet.MacAddress;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.flow.*;
import org.onosproject.net.flow.criteria.PiCriterion;
import org.onosproject.net.host.HostService;
import org.onosproject.net.pi.model.PiActionId;
import org.onosproject.net.pi.model.PiActionParamId;
import org.onosproject.net.pi.model.PiMatchFieldId;
import org.onosproject.net.pi.model.PiTableId;
import org.onosproject.net.pi.runtime.PiAction;
import org.onosproject.net.pi.runtime.PiActionParam;
import org.onosproject.net.topology.TopologyService;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualDevice;
import org.onosproject.p4virtex.element.VirtualPort;
import org.onosproject.p4virtex.service.VirtualNetworkFlowRuleService;
import org.onosproject.p4virtex.srv6.Sid;
import org.onosproject.p4virtex.store.VirtualNetworkStore;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Component(immediate = true, service = VirtualNetworkFlowRuleService.class)
public class VirtualNetworkFlowRuleManager implements VirtualNetworkFlowRuleService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String VN_FLOW_RULE_APP_NAME= "org.onosproject.p4virtex.flowrule";

    private ApplicationId appId;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private FlowRuleService flowRuleService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private TopologyService topologyService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private HostService hostService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private VirtualNetworkStore store;

    @Activate
    public void activate() {
        appId = coreService.registerApplication(VN_FLOW_RULE_APP_NAME);
        log.info("virtual flow rule manager started.");
    }

    @Deactivate
    public void deactivate() {
        log.info("virtual flow rule manager stopped.");
    }

    @Override
    public void allocateSid(NetworkId networkId) {

        Short domainId = Short.decode("0x000a");

        // 获取当前虚拟网络内所有的虚拟设备
        Set<VirtualDevice> deviceSet = store.getDevices(networkId);

        // 用于给底层的网络设备编号
        Short index = 1;

        // 用于当前虚拟网络涉及的所有的物理设备
        Set<DeviceId> phyDevices = new HashSet<>();
        Map<DeviceId, Sid> map = new HashMap<>();



        for (VirtualDevice device : deviceSet) {

            Set<VirtualPort> virtualPorts = store.getPorts(networkId, device.id());

            for (VirtualPort port : virtualPorts) {
                DeviceId deviceId = port.phyConnectPoint().deviceId();
                // 遇到新的物理设备
                if (!phyDevices.contains(deviceId)) {
                    Sid sid = new Sid(domainId, networkId.id().intValue(), index, 1L);
                    phyDevices.add(deviceId);
                    map.put(deviceId, sid);
                    // 将该SID插入local sid 表中
                    insertLocalSid(deviceId, sid.getIp6Address());
                    index++;
                }
            }
        }
    }


    public void insertLocalSid(DeviceId deviceId, Ip6Address ip6Address) {
        PiTableId tableId = PiTableId.of("ingress.local_sid_table");

        PiMatchFieldId ipv6DstAddrMatchFieldId = PiMatchFieldId.of("hdr.ipv6.dst_addr");

        PiCriterion match = PiCriterion.builder()
                .matchLpm(ipv6DstAddrMatchFieldId,
                        ip6Address.toOctets(),
                        128)
                .build();

        PiActionId actionId = PiActionId.of("ingress.end");
        PiAction action = PiAction.builder()
                .withId(actionId)
                .build();

        insertPiFlowRule(deviceId, tableId, match, action);
    }

    public void insertTransitRule(DeviceId deviceId, Ip6Prefix ip6Prefix, List<Ip6Address> segmentList) {
        PiTableId tableId = PiTableId.of("ingress.transit_table");

        PiMatchFieldId ipv6DstAddrMatchFieldId = PiMatchFieldId.of("hdr.ipv6.dst_addr");
        PiCriterion match = PiCriterion.builder()
                .matchLpm(ipv6DstAddrMatchFieldId,
                        ip6Prefix.address().toOctets(),
                        ip6Prefix.prefixLength())
                .build();

        PiActionId actionId = PiActionId.of("ingress.insert_segment_list_" + (segmentList.size() + 1));
        List<PiActionParam> actionParams = Lists.newArrayList();
        for (int i = 0; i < segmentList.size(); i++) {
            PiActionParamId paramId = PiActionParamId.of("s" + (i + 1));
            PiActionParam param = new PiActionParam(paramId, segmentList.get(i).toOctets());
            actionParams.add(param);
        }
        PiActionParamId lastParamId = PiActionParamId.of("s" + (segmentList.size() + 1));
        PiActionParam lastParam = new PiActionParam(lastParamId, ip6Prefix.address().toOctets());
        actionParams.add(lastParam);

        PiAction action = PiAction.builder()
                .withId(actionId)
                .withParameters(actionParams)
                .build();

        insertPiFlowRule(deviceId, tableId, match, action);
    }


    public void insertRoutingRule(DeviceId deviceId, Ip6Prefix ip6Prefix, MacAddress macAddress, PortNumber outputPort) {
        PiTableId tableId = PiTableId.of("ingress.routing_v6_table");

        PiMatchFieldId ipv6DstAddrMatchFieldId = PiMatchFieldId.of("hdr.ipv6.dst_addr");
        PiCriterion match = PiCriterion.builder()
                .matchLpm(ipv6DstAddrMatchFieldId,
                        ip6Prefix.address().toOctets(),
                        ip6Prefix.prefixLength())
                .build();

        PiActionId actionId = PiActionId.of("ingress.set_next_hop");

        PiActionParamId dstMacParamId = PiActionParamId.of("dmac");
        PiActionParam dstMacParam = new PiActionParam(dstMacParamId, macAddress.toLong());

        PiActionParamId portParamId = PiActionParamId.of("port");
        PiActionParam portParam = new PiActionParam(portParamId, outputPort.toLong());

        PiAction action = PiAction.builder()
                .withId(actionId)
                .withParameter(portParam)
                .withParameter(dstMacParam)
                .build();

        insertPiFlowRule(deviceId, tableId, match, action);
    }

    private void insertPiFlowRule(DeviceId switchId, PiTableId tableId, PiCriterion piCriterion, PiAction piAction) {
        FlowRule rule = DefaultFlowRule.builder()
                .forDevice(switchId)
                .forTable(tableId)
                .fromApp(appId)
                .withPriority(100)
                .makePermanent()
                .withSelector(DefaultTrafficSelector.builder().matchPi(piCriterion).build())
                .withTreatment(DefaultTrafficTreatment.builder().piTableAction(piAction).build())
                .build();
        flowRuleService.applyFlowRules(rule);
    }
}
