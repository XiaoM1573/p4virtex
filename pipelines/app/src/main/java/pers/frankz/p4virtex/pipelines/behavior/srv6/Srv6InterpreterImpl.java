package pers.frankz.p4virtex.pipelines.behavior.srv6;

import com.google.common.collect.ImmutableMap;
import org.onosproject.net.DeviceId;
import org.onosproject.net.driver.AbstractHandlerBehaviour;
import org.onosproject.net.flow.TrafficTreatment;
import org.onosproject.net.flow.criteria.Criterion;
import org.onosproject.net.packet.InboundPacket;
import org.onosproject.net.packet.OutboundPacket;
import org.onosproject.net.pi.model.PiMatchFieldId;
import org.onosproject.net.pi.model.PiPipelineInterpreter;
import org.onosproject.net.pi.model.PiTableId;
import org.onosproject.net.pi.runtime.PiAction;
import org.onosproject.net.pi.runtime.PiPacketOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static org.onosporject.pipelines.srv6.Srv6Constants.*;
import static org.onosporject.pipelines.srv6.Srv6Constants.LOCAL_METADATA_ICMPV6_TYPE;

/**
 *
 */
public class Srv6InterpreterImpl extends AbstractHandlerBehaviour
        implements PiPipelineInterpreter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final Map<Criterion.Type, PiMatchFieldId> CRITERION_MAP =
            new ImmutableMap.Builder<Criterion.Type, PiMatchFieldId>()
                    .put(Criterion.Type.IN_PORT, STANDARD_METADATA_INGRESS_PORT)
                    .put(Criterion.Type.ETH_DST, HDR_ETHERNET_DST_ADDR)
                    .put(Criterion.Type.ETH_SRC, HDR_ETHERNET_SRC_ADDR)
                    .put(Criterion.Type.ETH_TYPE, HDR_ETHERNET_ETHER_TYPE)
                    .put(Criterion.Type.IPV6_DST, HDR_IPV6_DST_ADDR)
                    .put(Criterion.Type.IP_PROTO, LOCAL_METADATA_IP_PROTO)
                    .put(Criterion.Type.ICMPV4_TYPE, LOCAL_METADATA_ICMP_TYPE)
                    .put(Criterion.Type.ICMPV6_TYPE, LOCAL_METADATA_ICMPV6_TYPE)
                    .build();

    /**
     *
     * @param type criterion type
     * @return
     */
    @Override
    public Optional<PiMatchFieldId> mapCriterionType(Criterion.Type type) {
        return Optional.ofNullable(CRITERION_MAP.get(type));
    }

    /**
     * 准备丢弃的函数
     *
     * @param flowRuleTableId a numeric table ID
     * @return
     */
    @Override
    public Optional<PiTableId> mapFlowRuleTableId(int flowRuleTableId) {
        return Optional.empty();
    }

    @Override
    public PiAction mapTreatment(TrafficTreatment treatment, PiTableId piTableId) throws PiInterpreterException {
        return null;
    }

    @Override
    public Collection<PiPacketOperation> mapOutboundPacket(OutboundPacket packet) throws PiInterpreterException {
        return null;
    }

    @Override
    public InboundPacket mapInboundPacket(PiPacketOperation packetOperation, DeviceId deviceId) throws PiInterpreterException {
        return null;
    }
}
