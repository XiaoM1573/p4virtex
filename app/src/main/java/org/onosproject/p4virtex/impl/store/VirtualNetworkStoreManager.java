package org.onosproject.p4virtex.impl.store;

import com.google.common.collect.ImmutableSet;
import org.onosproject.core.CoreService;
import org.onosproject.core.IdGenerator;
import org.onosproject.net.DeviceId;
import org.onosproject.net.HostId;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.element.*;
import org.onosproject.p4virtex.impl.element.DefaultVirtualDevice;
import org.onosproject.p4virtex.impl.element.DefaultVirtualNetwork;
import org.onosproject.p4virtex.store.VirtualNetworkStore;
import org.onosproject.store.service.ConsistentMap;
import org.onosproject.store.service.DistributedSet;
import org.onosproject.store.service.StorageService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

@Component(immediate = true, service = VirtualNetworkStore.class)
public class VirtualNetworkStoreManager implements VirtualNetworkStore {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private StorageService storageService;

    @Reference(cardinality = ReferenceCardinality.MANDATORY)
    private CoreService coreService;

    private IdGenerator idGenerator;

    private Set<TenantId> tenantIdSet;

    private Map<TenantId, Set<NetworkId>> tenantIdNetworkIdSetMap;

    private Map<NetworkId, VirtualNetwork> networkIdVirtualNetworkMap;

    private Map<NetworkId, Set<DeviceId>> networkIdDeviceIdSetMap;

    private Map<VirtualDeviceId, VirtualDevice> virtualDeviceIdVirtualDeviceMap;

    // Track virtual network by network id
//    private ConsistentMap<NetworkId, VirtualNetwork> networkIdVirtualNetworkConsistentMap;
//    private Map<NetworkId, VirtualNetwork> networkIdVirtualNetworkMap;
//
//    // Track virtual network IDs by tenant id
//    private ConsistentMap<TenantId, Set<NetworkId>> tenantIdNetworkSetConsistentMap;
//    private Map<TenantId, Set<NetworkId>> tenantIdNetworkIdSetMap;
//
//    // Track virtual device by device id
//    private ConsistentMap<VirtualDeviceId, VirtualDevice> deviceIdVirtualDeviceConsistentMap;
//    private Map<VirtualDeviceId, VirtualDevice> deviceIdVirtualDeviceMap;
//
//    // Track virtual host by host id
//    private ConsistentMap<HostId, VirtualHost> hostIdVirtualHostConsistentMap;
//    private Map<HostId, VirtualHost> hostIdVirtualHostMap;
//
//    // Track virtual links by network id
//    private ConsistentMap<NetworkId, Set<VirtualLink>> networkIdVirtualLinkSetConsistentMap;
//    private Map<NetworkId, Set<VirtualLink>> networkIdVirtualLinkSetMap;


    @Activate
    public void activate() {
        log.info("Virtual Network Store is starting ...");
        // used to generate unique network id
        idGenerator = coreService.getIdGenerator("p4virtex-virtual-network-ids");

        tenantIdSet = new HashSet<>();

        networkIdVirtualNetworkMap = new HashMap<>();

        tenantIdNetworkIdSetMap = new HashMap<>();

        networkIdDeviceIdSetMap = new HashMap<>();

        virtualDeviceIdVirtualDeviceMap = new HashMap<>();


        // init
//        tenantIdSet = storageService.<TenantId>setBuilder()
//                .withName("onos-p4virtex-tenantId")
//                .build()
//                .asDistributedSet();
//
//        networkIdVirtualNetworkConsistentMap = storageService.<NetworkId, VirtualNetwork>consistentMapBuilder()
//                .withName("onos-p4virtex-networkId-virtualnetwork")
//                .build();
//        networkIdVirtualNetworkMap = networkIdVirtualNetworkConsistentMap.asJavaMap();


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
        // firstly remove all the virtual networks of tenant
        if (networkIdSet != null) {
            networkIdSet.forEach(virtualNetwork -> removeNetwork(virtualNetwork.networkId()));
        }
        // then remove the tenant
        tenantIdSet.remove(tenantId);
    }

    @Override
    public Set<TenantId> getTenantIds() {
        return ImmutableSet.copyOf(tenantIdSet);
    }

    @Override
    public VirtualNetwork addNetwork(TenantId tenantId) {
        checkState(tenantIdSet.contains(tenantId), "The tenant has not been registered.");
        // auto generate network id
        VirtualNetwork virtualNetwork = new DefaultVirtualNetwork(autoGenerateNetworkId(), tenantId);
        // map networkId -> virtual network
        networkIdVirtualNetworkMap.put(virtualNetwork.networkId(), virtualNetwork);

        // update map tenantId -> set of virtual network id
        Set<NetworkId> networkIdSet = tenantIdNetworkIdSetMap.get(tenantId);
        if (networkIdSet == null) {
            networkIdSet = new HashSet<>();
        }
        networkIdSet.add(virtualNetwork.networkId());
        tenantIdNetworkIdSetMap.put(tenantId, networkIdSet);
        return virtualNetwork;
    }

    @Override
    public void removeNetwork(NetworkId networkId) {
        checkState(networkIdVirtualNetworkMap.containsKey(networkId), "The virtual network does not exist.");

        //TODO.
    }

    @Override
    public Set<VirtualNetwork> getNetworks(TenantId tenantId) {
        Set<NetworkId> networkIdSet = tenantIdNetworkIdSetMap.get(tenantId);
        Set<VirtualNetwork> virtualNetworkSet = new HashSet<>();
        if (networkIdSet != null) {
            networkIdSet.forEach(networkId -> {
                if (networkIdVirtualNetworkMap.containsKey(networkId)) {
                    virtualNetworkSet.add(networkIdVirtualNetworkMap.get(networkId));
                }
            });
        }
        return ImmutableSet.copyOf(virtualNetworkSet);
    }

    @Override
    public VirtualDevice addDevice(NetworkId networkId, DeviceId deviceId) {
        checkState(networkExists(networkId), "The network has not been added. ");

        // get all device ids of the specified network id
        Set<DeviceId> deviceIdSet = networkIdDeviceIdSetMap.get(networkId);
        if (deviceIdSet == null) {
            deviceIdSet = new HashSet<>();
        }

        checkState(!deviceIdSet.contains(deviceId), "The device already exists.");

        VirtualDevice virtualDevice = new DefaultVirtualDevice(networkId, deviceId);
        VirtualDeviceId virtualDeviceId = new VirtualDeviceId(networkId, deviceId);

        deviceIdSet.add(deviceId);

        virtualDeviceIdVirtualDeviceMap.put(virtualDeviceId, virtualDevice);
        networkIdDeviceIdSetMap.put(networkId, deviceIdSet);

        return virtualDevice;
    }

    @Override
    public void removeDevice(NetworkId networkId, DeviceId deviceId) {
        // TODO.
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

    // auto generate a network id
    private synchronized NetworkId autoGenerateNetworkId() {
        NetworkId networkId;
        do {
            networkId = NetworkId.networkId(idGenerator.getNewId());
        } while (!networkId.isVirtualNetworkId());
        return networkId;
    }

    private boolean networkExists(NetworkId networkId) {
        return networkIdVirtualNetworkMap.containsKey(networkId);
    }

}
