package org.onosproject.p4virtex.impl.store;

import com.google.common.collect.ImmutableSet;
import org.onosproject.core.CoreService;
import org.onosproject.core.IdGenerator;
import org.onosproject.net.*;
import org.onosproject.p4virtex.element.*;
import org.onosproject.p4virtex.element.DefaultVirtualNetwork;
import org.onosproject.p4virtex.store.VirtualNetworkStore;
import org.onosproject.store.service.StorageService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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

    // Track virtual ports by network id
    private Map<NetworkId, Set<VirtualPort>> networkIdVirtualPortSetMap;


    // some error message
    private static final String MSG_NETWORK_NOT_EXIST = "The virtual network is not existed. ";

    private static final String MSG_DEVICE_EXIST = "The virtual device has already been created. ";

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

        networkIdVirtualPortSetMap = new HashMap<>();


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
        checkState(!tenantIdSet.contains(tenantId), "The tenant has been existed.");
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
    public VirtualNetwork getNetwork(NetworkId networkId) {
        return networkIdVirtualNetworkMap.get(networkId);
    }

    @Override
    public VirtualDevice addDevice(NetworkId networkId, DeviceId deviceId) {
        checkState(networkExists(networkId), MSG_NETWORK_NOT_EXIST);

        Set<DeviceId> deviceIdSet = networkIdDeviceIdSetMap.get(networkId);

        if (deviceIdSet == null) {
            deviceIdSet = new HashSet<>();
        }

        checkState(!deviceIdSet.contains(deviceId), MSG_DEVICE_EXIST);

        deviceIdSet.add(deviceId);
        networkIdDeviceIdSetMap.put(networkId, deviceIdSet);

        VirtualDevice virtualDevice = new DefaultVirtualDevice(networkId, deviceId);
        VirtualDeviceId virtualDeviceId = new VirtualDeviceId(networkId, deviceId);
        virtualDeviceIdVirtualDeviceMap.put(virtualDeviceId, virtualDevice);

        return virtualDevice;
    }

    @Override
    public void removeDevice(NetworkId networkId, DeviceId deviceId) {
        // TODO.
    }

    @Override
    public Set<VirtualDevice> getDevices(NetworkId networkId) {
        checkState(networkExists(networkId), MSG_NETWORK_NOT_EXIST);
        Set<DeviceId> deviceIdSet = networkIdDeviceIdSetMap.get(networkId);

        Set<VirtualDevice> virtualDeviceSet = new HashSet<>();

        if (deviceIdSet != null) {
            deviceIdSet.forEach(deviceId -> virtualDeviceSet.add(virtualDeviceIdVirtualDeviceMap.get(new VirtualDeviceId(networkId, deviceId))));
        }
        return ImmutableSet.copyOf(virtualDeviceSet);
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
    public VirtualLink addLink(NetworkId networkId, ConnectPoint src, ConnectPoint dst, Link.State state) {
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
    public VirtualPort addPort(NetworkId networkId, DeviceId deviceId, PortNumber portNumber, ConnectPoint phyConnectPoint) {
        checkState(networkExists(networkId), "The virtual network is not existed. ");

        Set<VirtualPort> virtualPortSet = networkIdVirtualPortSetMap.get(networkId);

        if (virtualPortSet == null) {
            virtualPortSet = new HashSet<>();
        }

        VirtualDevice virtualDevice = virtualDeviceIdVirtualDeviceMap.get(new VirtualDeviceId(networkId, deviceId));
        checkNotNull(virtualDevice, "The virtual device has not been created for deviceId: " + deviceId);

        checkState(!virtualPortExists(networkId, deviceId, portNumber), "The requested port has been added.");

        VirtualPort virtualPort = new DefaultVirtualPort(networkId, virtualDevice, portNumber, phyConnectPoint);

        virtualPortSet.add(virtualPort);

        networkIdVirtualPortSetMap.put(networkId, virtualPortSet);

        return virtualPort;
    }


    @Override
    public void removePort() {

    }

    @Override
    public Set<VirtualPort> getPorts(NetworkId networkId, DeviceId deviceId) {
        checkState(networkExists(networkId), "The network has not been added.");
        Set<VirtualPort> virtualPortSet = networkIdVirtualPortSetMap.get(networkId);
        if (virtualPortSet == null) {
            virtualPortSet = new HashSet<>();
        }

        if (deviceId == null) {
            return ImmutableSet.copyOf(virtualPortSet);
        }

        Set<VirtualPort> portSet = new HashSet<>();
        virtualPortSet.forEach(virtualPort -> {
            if (virtualPort.element().id().equals(deviceId)) {
                portSet.add(virtualPort);
            }
        });
        return ImmutableSet.copyOf(portSet);
    }

    @Override
    public void updatePortState(NetworkId networkId, DeviceId deviceId, PortNumber portNumber, boolean isEnabled) {
//        checkState(networkExists(networkId), MSG_NETWORK_NOT_EXIST);
//
//        VirtualDevice device = virtualDeviceIdVirtualDeviceMap.get(new VirtualDeviceId(networkId, deviceId));
//        checkNotNull(device, "No device %s exists in virtual networkId: %s", deviceId, networkId);
//
//        Set<VirtualPort> virtualPorts = networkIdVirtualPortSetMap.get(networkId);
//        checkNotNull(virtualPorts, "No port has been added for NetworkId: %s", networkId);
//
//        Optional<VirtualPort> virtualPortOptional = virtualPorts.stream()
//                .filter(p -> p.element().id().equals(deviceId) && p.number().equals(portNumber)).findFirst();
//        checkState(virtualPortOptional.isPresent(), "The virtual port has not been added. ");
//
//        VirtualPort virtualPort = virtualPortOptional.get();
//        if (virtualPort.isEnabled() == isEnabled) {
//            log.debug("No change in port state");
//            return;
//        }
//
//        VirtualPort _virtualPort = new DefaultVirtualPort(networkId, device, portNumber, isEnabled, virtualPort.phyConnectPoint());
//        virtualPorts.remove(virtualPort);
//        virtualPorts.add(_virtualPort);
//        networkIdVirtualPortSetMap.put(networkId, virtualPorts);
//
//        log.debug("port state change from {} to {}", virtualPort.isEnabled(), isEnabled);
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

    private boolean virtualPortExists(NetworkId networkId, DeviceId deviceId, PortNumber portNumber) {
        Set<VirtualPort> virtualPortSet = networkIdVirtualPortSetMap.get(networkId);
        if (virtualPortSet != null) {
            return virtualPortSet.stream().anyMatch(p -> p.element().id().equals(deviceId) && p.number().equals(portNumber));
        } else {
            return false;
        }
    }

}
