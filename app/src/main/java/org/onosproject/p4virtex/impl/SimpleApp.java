package org.onosproject.p4virtex.impl;

import org.onlab.packet.Ip4Address;
import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.flow.*;
import org.onosproject.net.flow.criteria.PiCriterion;
import org.onosproject.net.host.HostEvent;
import org.onosproject.net.host.HostListener;
import org.onosproject.net.host.HostService;
import org.onosproject.net.link.LinkService;
import org.onosproject.net.pi.model.PiActionId;
import org.onosproject.net.pi.model.PiActionParamId;
import org.onosproject.net.pi.model.PiMatchFieldId;
import org.onosproject.net.pi.model.PiTableId;
import org.onosproject.net.pi.runtime.PiAction;
import org.onosproject.net.pi.runtime.PiActionParam;
import org.onosproject.net.topology.TopologyService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.collect.Streams.stream;

@Component(immediate = true,
        // set to ture when ready
        enabled = true,
        service = SimpleApp.class
)
public class SimpleApp {

    private static final String APP_NAME = "org.onosproject.p4virtex.impl.SimpleApp";

    private static final Logger log = LoggerFactory.getLogger(SimpleApp.class);

    private static final int DEFAULT_RULE_PRIORITY = 100;

    private final HostListener hostListener = new InternalHostListener();

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private FlowRuleService flowRuleService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private TopologyService topologyService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private HostService hostService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private LinkService linkService;

    private ApplicationId appId;

    @Activate
    public void activate() {
        log.info("simple app start...");
        this.appId = coreService.registerApplication(APP_NAME);
        hostService.addListener(hostListener);
        log.info("simple app started");

    }

    @Deactivate
    public void deactivate() {
        log.info("simple app stop...");
        hostService.removeListener(hostListener);
        log.info("simple app stopped...");
    }

    public void insertForwardRule(DeviceId switchId, Ip4Address dstIp, long mask, boolean isEgress, PortNumber outPort) {
        // table
        PiTableId table0Id = PiTableId.of("ingress.table0_control.table0");

        // match field
        PiMatchFieldId ipv4DstAddrMatcFieldId = PiMatchFieldId.of("hdr.ipv4.dst_addr");
        PiCriterion macth = PiCriterion.builder()
                .matchTernary(ipv4DstAddrMatcFieldId, dstIp.toInt(), mask)
                .build();
        // action
        PiActionId egressActionId = PiActionId.of("ingress.table0_control.set_egress_port");
        PiActionId cpuActionId = PiActionId.of("ingrss.table0_control.send_to_cpu");

        // action param
        PiActionParamId portParamId = PiActionParamId.of("port");
        PiActionParam portParam = new PiActionParam(portParamId, outPort.toLong());

        PiAction action = PiAction.builder()
                .withId(egressActionId)
                .withParameter(portParam)
                .build();
//        if (isEgress) {
//            action = PiAction.builder()
//                    .withId(egressActionId)
//                    .withParameter(portParam)
//                    .build();
//        } else {
//            action = PiAction.builder()
//                    .withId(cpuActionId)
//                    .build();
//        }
        insertPiFlowRule(switchId, table0Id, macth, action);
    }

    public void deleteForwardRule(DeviceId deviceId,  Ip4Address dstIp, long mask) {

        // table
        PiTableId table0Id = PiTableId.of("ingress.table0_control.table0");

        // match field
        PiMatchFieldId ipv4DstAddrMatcFieldId = PiMatchFieldId.of("hdr.ipv4.dst_addr");
        PiCriterion macth = PiCriterion.builder()
                .matchTernary(ipv4DstAddrMatcFieldId, dstIp.toInt(), mask)
                .build();

        deletePiFlowRule(deviceId,table0Id,macth);

    }

    private void insertPiFlowRule(DeviceId switchId, PiTableId tableId, PiCriterion piCriterion, PiAction piAction) {
        FlowRule rule = DefaultFlowRule.builder()
                .forDevice(switchId)
                .forTable(tableId)
                .fromApp(appId)
                .withPriority(DEFAULT_RULE_PRIORITY)
                .makePermanent()
                .withSelector(DefaultTrafficSelector.builder().matchPi(piCriterion).build())
                .withTreatment(DefaultTrafficTreatment.builder().piTableAction(piAction).build())
                .build();
        flowRuleService.applyFlowRules(rule);
    }

    private void deletePiFlowRule(DeviceId switchId, PiTableId tableId, PiCriterion piCriterion) {
        PiTableId table0Id = PiTableId.of("ingress.table0_control.table0");
        FlowRuleOperations.Builder ops = FlowRuleOperations.builder();
        stream(flowRuleService.getFlowEntries(switchId))
                .filter(flowEntry -> flowEntry.appId() == appId.id())
                .filter(flowEntry -> flowEntry.table().equals(table0Id))
                .filter(flowEntry -> flowEntry.selector().equals(DefaultTrafficSelector.builder().matchPi(piCriterion).build()))
                .forEach(ops::remove);
        flowRuleService.apply(ops.build());
    }


    private class InternalHostListener implements HostListener {

        @Override
        public void event(HostEvent event) {
            log.info("TODO. Host Listener");
        }
    }

}
