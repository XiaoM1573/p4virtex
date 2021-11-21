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
        VirtualNetworkService.class,
        VirtualNetworkAdminService.class
})
public class VirtualNetworkManager implements VirtualNetworkService, VirtualNetworkAdminService {

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
        return store.addNetwork(tenantId);
    }

    @Override
    public void removeVirtualNetwork(NetworkId networkId) {
        // TODO
    }

    @Override
    public VirtualDevice createVirtualDevice(NetworkId networkId, DeviceId deviceId) {
        return store.addDevice(networkId, deviceId);
    }

    @Override
    public void removeVirtualDevice(NetworkId networkId, DeviceId deviceId) {
        // TODO
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
        return store.addPort(networkId, deviceId, portNumber, phyConnectPoint);
    }

    @Override
    public void removeVirtualPort(NetworkId networkId, DeviceId deviceId, PortNumber portNumber) {

    }

    @Override
    public void updateVirtualPortState(NetworkId networkId, DeviceId deviceId, PortNumber portNumber, boolean isEnabled) {
        store.updatePortState(networkId, deviceId, portNumber, isEnabled);
    }

    // methods of the virtual network service
    @Override
    public Set<VirtualNetwork> getVirtualNetworks(TenantId tenantId) {
        return store.getNetworks(tenantId);
    }

    @Override
    public VirtualNetwork getVirtualNetwork(NetworkId networkId) {
        return store.getNetwork(networkId);
    }

    @Override
    public TenantId getTenantId(NetworkId networkId) {
        return null;
    }

    @Override
    public Set<VirtualDevice> getVirtualDevices(NetworkId networkId) {
        return store.getDevices(networkId);
    }

    @Override
    public Set<VirtualHost> getVirtualHosts(NetworkId networkId) {
        return null;
    }

    @Override
    public Set<VirtualLink> getVirtualLinks(NetworkId networkId) {
        return null;
    }

    @Override
    public Set<VirtualPort> getVirtualPorts(NetworkId networkId, DeviceId deviceId) {
        return store.getPorts(networkId, deviceId);
    }

    @Override
    public Set<DeviceId> getPhysicalDevices(NetworkId networkId, DeviceId deviceId) {
        return null;
    }
}
