package org.onosproject.p4virtex.service;

import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.element.*;

import java.util.Set;

/**
 * Service for managing the virtual network
 */
public interface VirtualNetworkAdminService extends VirtualNetworkService {


    void registerTenantId(TenantId tenantId);

    void unregisterTenantId(TenantId tenantId);

    Set<TenantId> getTenantIds();

    // virtual network
    VirtualNetwork createVirtualNetwork(TenantId tenantId);

    void removeVirtualNetwork(NetworkId networkId);

    // virtual device
    VirtualDevice createVirtualDevice(NetworkId networkId, DeviceId deviceId);

    void removeVirtualDevice(NetworkId networkId, DeviceId deviceId);

    // virtual host
    // TODO.
    VirtualHost createVirtualHost();

    void removeVirtualHost();

    // virtual link
    VirtualLink createVirtualLink(NetworkId networkId, ConnectPoint src, ConnectPoint dst);

    void removeVirtualLink(NetworkId networkId, ConnectPoint src, ConnectPoint dst);

    // virtual port
    /**
     * @param networkId       network identifier
     * @param deviceId        virtual device identifier
     * @param portNumber      virtual port number
     * @param phyConnectPoint underlying physical port using which this virtual port is mapped
     * @return newly created port
     */
    VirtualPort createVirtualPort(NetworkId networkId, DeviceId deviceId, PortNumber portNumber, ConnectPoint phyConnectPoint);

    void removeVirtualPort(NetworkId networkId, DeviceId deviceId, PortNumber portNumber);


}
