package org.onosporject.pipelines.srv6;

import org.onosproject.core.CoreService;
import org.onosproject.net.behaviour.Pipeliner;
import org.onosproject.net.device.PortStatisticsDiscovery;
import org.onosproject.net.pi.model.*;
import org.onosproject.net.pi.service.PiPipeconfService;
import org.onosproject.p4runtime.model.P4InfoParser;
import org.onosproject.p4runtime.model.P4InfoParserException;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.onosproject.net.pi.model.PiPipeconf.ExtensionType.BMV2_JSON;
import static org.onosproject.net.pi.model.PiPipeconf.ExtensionType.P4_INFO_TEXT;

@Component(immediate = true)
public final class PipeconfLoader {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String APP_NAME = "org.onosproject.pipelines.srv6";

    private static final PiPipeconfId SRV6_PIPECONF_ID = new PiPipeconfId("org.onosproject.pipelines.srv6");
    private static final String SRV6_JSON_PATH = "/p4c-out/bmv2/srv6.json";
    private static final String SRV6_P4INFO = "/p4c-out/bmv2/srv6_p4info.txt";

    public static final PiPipeconf BASIC_PIPECONF = buildSRv6Pipeconf();


    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private PiPipeconfService piPipeconfService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private CoreService coreService;

    @Activate
    public void activate() {
        coreService.registerApplication(APP_NAME);
        // Registers srv6 pipeconf at component activation.
        piPipeconfService.register(BASIC_PIPECONF);
    }

    @Deactivate
    public void deactivate() {
        // Unregisters the srv6 pipeconf at component deactivation.
        try {
            piPipeconfService.unregister(SRV6_PIPECONF_ID);
        } catch (IllegalStateException e) {
            log.warn("{} haven't been registered", SRV6_PIPECONF_ID);
        }
    }

    private static PiPipeconf buildSRv6Pipeconf() {
        final URL jsonUrl = PipeconfLoader.class.getResource(SRV6_JSON_PATH);
        final URL p4InfoUrl = PipeconfLoader.class.getResource(SRV6_P4INFO);

        return DefaultPiPipeconf.builder()
                .withId(SRV6_PIPECONF_ID)
                .withPipelineModel(parseP4Info(p4InfoUrl))
                .addBehaviour(PiPipelineInterpreter.class, Srv6InterpreterImpl.class)
                .addBehaviour(Pipeliner.class, Srv6PipelinerImpl.class)
                .addBehaviour(PortStatisticsDiscovery.class, PortStatisticsDiscoveryImpl.class)
                .addExtension(P4_INFO_TEXT, p4InfoUrl)
                .addExtension(BMV2_JSON, jsonUrl)
                .build();
    }

    private static PiPipelineModel parseP4Info(URL p4InfoUrl) {
        try {
            return P4InfoParser.parse(p4InfoUrl);
        } catch (P4InfoParserException e) {
            throw new IllegalStateException(e);
        }
    }
}
