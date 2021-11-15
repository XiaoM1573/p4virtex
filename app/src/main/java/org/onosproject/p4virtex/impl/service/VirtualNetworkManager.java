package org.onosproject.p4virtex.impl.service;

import org.onosproject.core.ApplicationId;
import org.onosproject.core.CoreService;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.element.*;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;
import org.onosproject.p4virtex.service.VirtualNetworkService;
import org.onosproject.p4virtex.store.VirtualNetworkStore;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of the virtual network service
 */
@Component(immediate = true, service = {
        VirtualNetworkAdminService.class
})
public class VirtualNetworkManager implements VirtualNetworkAdminService {

    private static final String VIRTUAL_NETWORK_MANAGEMENT_APP_NAME = "org.onosproject.vn-manager";

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ApplicationId applicationId;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private CoreService coreService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private VirtualNetworkStore store;

    public void activate() {
        log.info("Virtual Network Manager Service is starting ...");
        this.applicationId = coreService.registerApplication(VIRTUAL_NETWORK_MANAGEMENT_APP_NAME);
        log.info("Virtual Network Manager Service has been started.");
    }

    public void deactivate() {
        log.info("Virtual Network Manager Service is stopping ...");


        log.info("Virtual Network Manager Service has been stopped.");
    }


    @Override
    public void registerTenantId(TenantId tenantId) {
        store.addTenantId(tenantId);
    }

    @Override
    public void unregisterTenantId(TenantId tenantId) {
        store.removeTenantId(tenantId);
    }

    @Override
    public Set<TenantId> getTenantIds() {
        return store.getTenantIds();
    }

    @Override
    public VirtualNetwork createVirtualNetwork(TenantId tenantId) {
        return null;
    }

    @Override
    public void removeVirtualNetwork(NetworkId networkId) {

    }

    @Override
    public VirtualDevice createVirtualDevice(NetworkId networkId, DeviceId deviceId) {
        return null;
    }

    @Override
    public void removeVirtualDevice(NetworkId networkId, DeviceId deviceId) {

    }

    @Override
    public VirtualHost createVirtualHost() {
        return null;
    }

    @Override
    public void removeVirtualHost() {

    }

    @Override
    public VirtualLink createVirtualLink(NetworkId networkId, ConnectPoint src, ConnectPoint dst) {
        return null;
    }

    @Override
    public void removeVirtualLink(NetworkId networkId, ConnectPoint src, ConnectPoint dst) {

    }

    @Override
    public VirtualPort createVirtualPort(NetworkId networkId, DeviceId deviceId, PortNumber portNumber, ConnectPoint phyConnectPoint) {
        return null;
    }

    @Override
    public void removeVirtualPort(NetworkId networkId, DeviceId deviceId, PortNumber portNumber) {

    }
}
