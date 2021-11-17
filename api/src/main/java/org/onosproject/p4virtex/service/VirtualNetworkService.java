package org.onosproject.p4virtex.service;

import org.onosproject.net.DeviceId;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.element.*;

import java.util.Set;

public interface VirtualNetworkService {

    Set<VirtualNetwork> getVirtualNetworks(TenantId tenantId);

    VirtualNetwork getVirtualNetwork(NetworkId networkId);

    TenantId getTenantId(NetworkId networkId);

    Set<VirtualDevice> getVirtualDevices(NetworkId networkId);

    Set<VirtualHost> getVirtualHosts(NetworkId networkId);

    Set<VirtualLink> getVirtualLinks(NetworkId networkId);

    Set<VirtualPort> getVirtualPorts(NetworkId networkId, DeviceId deviceId);

    Set<DeviceId> getPhysicalDevices(NetworkId networkId, DeviceId deviceId);

}
