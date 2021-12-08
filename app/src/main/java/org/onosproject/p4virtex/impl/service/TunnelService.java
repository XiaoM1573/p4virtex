package org.onosproject.p4virtex.impl.service;

import com.google.common.collect.Lists;
import org.onlab.packet.Ip6Address;
import org.onlab.packet.Ip6Prefix;
import org.onlab.packet.MacAddress;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.*;
import org.onosproject.net.flow.*;
import org.onosproject.net.flow.criteria.PiCriterion;
import org.onosproject.net.host.HostEvent;
import org.onosproject.net.host.HostListener;
import org.onosproject.net.host.HostService;
import org.onosproject.net.pi.model.PiActionId;
import org.onosproject.net.pi.model.PiActionParamId;
import org.onosproject.net.pi.model.PiMatchFieldId;
import org.onosproject.net.pi.model.PiTableId;
import org.onosproject.net.pi.runtime.PiAction;
import org.onosproject.net.pi.runtime.PiActionParam;
import org.onosproject.net.topology.Topology;
import org.onosproject.net.topology.TopologyService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Component(immediate = true,
        service = TunnelService.class
)
public class TunnelService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String TUNNEL_APP_NAME = "org.onosproject.p4virtex.tunnel";

    private ApplicationId applicationId;

    private final HostListener hostListener = new InternalHostListener();

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private FlowRuleService flowRuleService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private TopologyService topologyService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private HostService hostService;

    @Activate
    public void activate() {
        // Register app and event listeners.
        log.info("Starting...");
        applicationId = coreService.registerApplication(TUNNEL_APP_NAME);
        hostService.addListener(hostListener);
        log.info("STARTED", applicationId.id());
    }

    @Deactivate
    public void deactivate() {
        // Remove listeners and clean-up flow rules.
        log.info("Stopping...");
        hostService.removeListener(hostListener);
        flowRuleService.removeFlowRulesById(applicationId);
        log.info("STOPPED");
    }


    private void provisionTunnel(Host srcHost, Host dstHost, Topology topo) {
        // Get the switches connected to source and destination hosts
        DeviceId srcSwitch = srcHost.location().deviceId();
        DeviceId dstSwitch = dstHost.location().deviceId();

        List<Link> pathLinks;
        if (srcSwitch.equals(dstSwitch)) {
            // src and dst hosts are connected to the same switch
            pathLinks = Collections.emptyList();
        } else {
            // compute the shortest path.
            Set<Path> allPaths = topologyService.getPaths(topo, srcSwitch, dstSwitch);

            if (allPaths.size() == 0) {
                log.warn("No paths between{} and {}", srcHost.id(), dstHost.id());
                return;
            }

            //If exists many shortest paths, pick a random one.
            pathLinks = pickRandomPath(allPaths).links();

            for (Link link : pathLinks) {
                log.info("================");
                log.info(link.src().toString());
                log.info(link.dst().toString());
                log.info("================");
            }
        }
    }

    public void autoSetTunnel(Host srcHost, Host dstHost) {
        provisionTunnel(srcHost, dstHost, topologyService.currentTopology());

    }

    private Path pickRandomPath(Set<Path> paths) {
        int item = new Random().nextInt(paths.size());
        List<Path> pathList = Lists.newArrayList(paths);
        return pathList.get(item);
    }

    public void insertLocalMac(DeviceId deviceId, MacAddress macAddress) {
        PiTableId tableId = PiTableId.of("ingress.local_mac_table");

        PiMatchFieldId macDstAddr = PiMatchFieldId.of("hdr.ethernet.dst_addr");

        PiCriterion match = PiCriterion.builder()
                .matchExact(macDstAddr, macAddress.toLong())
                .build();

        PiActionId actionId = PiActionId.of("NoAction");
        PiAction action = PiAction.builder()
                .withId(actionId)
                .build();

        insertPiFlowRule(deviceId, tableId, match, action);
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
                .fromApp(applicationId)
                .withPriority(100)
                .makePermanent()
                .withSelector(DefaultTrafficSelector.builder().matchPi(piCriterion).build())
                .withTreatment(DefaultTrafficTreatment.builder().piTableAction(piAction).build())
                .build();
        flowRuleService.applyFlowRules(rule);
    }


    private class InternalHostListener implements HostListener {

        @Override
        public void event(HostEvent event) {
            if (event.type() != HostEvent.Type.HOST_ADDED) {
                // Ignore other host events.
                return;
            }
            synchronized (this) {
                // Synchronizing here is an overkill, but safer for demo purposes.
                Host host = event.subject();
                Topology topo = topologyService.currentTopology();
                for (Host otherHost : hostService.getHosts()) {
                    if (!host.equals(otherHost)) {
                        provisionTunnel(host, otherHost, topo);
                        provisionTunnel(otherHost, host, topo);
                    }
                }
            }
        }
    }

}
