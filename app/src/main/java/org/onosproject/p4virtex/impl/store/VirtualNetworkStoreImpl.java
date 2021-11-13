package org.onosproject.p4virtex.impl.store;

import org.onosproject.core.CoreService;
import org.onosproject.core.IdGenerator;
import org.onosproject.net.DeviceId;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.element.*;
import org.onosproject.p4virtex.store.VirtualNetworkStore;
import org.onosproject.store.service.ConsistentMap;
import org.onosproject.store.service.DistributedSet;
import org.onosproject.store.service.StorageService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

@Component(immediate = true, service = VirtualNetworkStore.class)
public class VirtualNetworkStoreImpl implements VirtualNetworkStore {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private StorageService storageService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private CoreService coreService;

    private IdGenerator idGenerator;

    private DistributedSet<TenantId> tenantIdSet;

    // Track virtual network by network id
    private ConsistentMap<NetworkId, VirtualNetwork> networkIdVirtualNetworkConsistentMap;
    private Map<NetworkId, VirtualNetwork> networkIdVirtualNetworkMap;

    // Track virtual network IDs by tenant id
    private ConsistentMap<TenantId, Set<NetworkId>> tenantIdNetworkSetConsistentMap;
    private Map<TenantId, Set<NetworkId>> tenantIdNetworkIdSetMap;

    // Track virtual device by device id
    private ConsistentMap<VirtualDeviceId, VirtualDevice> deviceIdVirtualDeviceConsistentMap;
    private Map<VirtualDeviceId, VirtualDevice> deviceIdVirtualDeviceMap;




    @Activate
    public void activate() {
        log.info("Virtual Network Store is starting ...");
        // init
        tenantIdSet = storageService.<TenantId>setBuilder()
                .withName("onos-p4virtex-tenantId")
                .build()
                .asDistributedSet();

        networkIdVirtualNetworkConsistentMap = storageService.<NetworkId, VirtualNetwork>consistentMapBuilder()
                .withName("onos-p4virtex-networkId-virtualnetwork")
                .build();
        networkIdVirtualNetworkMap = networkIdVirtualNetworkConsistentMap.asJavaMap();


        log.info("Virtual Network Store has been started.");
    }

    @Deactivate
    public void deactivate() {
        log.info("Virtual Network Store is stopping ...");

        log.info("Virtual Network Store has been stopped.");
    }

    @Override
    public void addTenantId(TenantId tenantId) {
        tenantIdSet.add(tenantId);

    }

    @Override
    public void removeTenantId(TenantId tenantId) {
        Set<VirtualNetwork> networkIdSet = getNetworks(tenantId);
        if (networkIdSet != null) {
            networkIdSet.forEach(virtualNetwork -> removeNetwork(virtualNetwork.networkId()));
        }
    }

    @Override
    public Set<TenantId> getTenantIds() {
        return null;
    }

    @Override
    public VirtualNetwork addNetwork(TenantId tenantId) {
        return null;
    }

    @Override
    public void removeNetwork(NetworkId networkId) {

    }

    @Override
    public Set<VirtualNetwork> getNetworks(TenantId tenantId) {
        return null;
    }

    @Override
    public VirtualDevice addDevice() {
        return null;
    }

    @Override
    public void removeDevice() {

    }

    @Override
    public Set<VirtualDevice> getDevices(NetworkId networkId) {
        return null;
    }

    @Override
    public VirtualHost addHost() {
        return null;
    }

    @Override
    public void removeHost() {

    }

    @Override
    public Set<VirtualHost> getHosts(NetworkId networkId) {
        return null;
    }

    @Override
    public VirtualLink addLink() {
        return null;
    }

    @Override
    public void removeLink() {

    }

    @Override
    public Set<VirtualLink> getLinks(NetworkId networkId) {
        return null;
    }

    @Override
    public VirtualPort addPort() {
        return null;
    }

    @Override
    public void removePort() {

    }

    @Override
    public Set<VirtualPort> getPorts(NetworkId networkId, DeviceId deviceId) {
        return null;
    }
}
