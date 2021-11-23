package org.onosproject.p4virtex.store;

import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.element.*;

import java.util.Set;

/**
 * CRUD of Virtual Network Management
 */
public interface VirtualNetworkStore {

    void addTenantId(TenantId tenantId);

    void removeTenantId(TenantId tenantId);

    Set<TenantId> getTenantIds();

    VirtualNetwork addNetwork(TenantId tenantId);

    void removeNetwork(NetworkId networkId);

    Set<VirtualNetwork> getNetworks(TenantId tenantId);

    VirtualNetwork getNetwork(NetworkId networkId);

    VirtualDevice addDevice(NetworkId networkId, DeviceId deviceId);

    void removeDevice(NetworkId networkId, DeviceId deviceId);

    Set<VirtualDevice> getDevices(NetworkId networkId);

    VirtualHost addHost();

    void removeHost();

    Set<VirtualHost> getHosts(NetworkId networkId);

    VirtualLink addLink();

    void removeLink();

    Set<VirtualLink> getLinks(NetworkId networkId);

    VirtualPort addPort(NetworkId networkId, DeviceId deviceId, PortNumber portNumber, ConnectPoint phyConnectPoint);

    void removePort();

    Set<VirtualPort> getPorts(NetworkId networkId, DeviceId deviceId);

    void updatePortState(NetworkId networkId, DeviceId deviceId, PortNumber portNumber, boolean isEnabled);


}
