package org.onosproject.p4virtex.physical;

import org.onosproject.core.CoreService;
import org.onosproject.net.device.DeviceEvent;
import org.onosproject.net.device.DeviceListener;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.host.HostService;
import org.onosproject.net.link.LinkService;
import org.onosproject.net.topology.TopologyService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = NetworkManager.class)
public class NetworkManager {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private TopologyService topologyService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private DeviceService deviceService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private HostService hostService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private LinkService linkService;

    private final DeviceListener deviceListener = new InternalDeviceListener();

    @Activate
    public void activate() {
        logger.info("p4virtex network manager is starting ... ");

    }

    @Deactivate
    public void deactivate() {

    }

    private class InternalDeviceListener implements DeviceListener {


        @Override
        public void event(DeviceEvent event) {




        }
    }
}
