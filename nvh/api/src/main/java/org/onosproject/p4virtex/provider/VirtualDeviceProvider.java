package org.onosproject.p4virtex.provider;

import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DeviceId;

/**
 * Abstraction of a virtual device information provider.
 */
public interface VirtualDeviceProvider extends VirtualProvider{

    /**
     * Indicates whether or not the specificied connect points on the underlying
     * network are traversable
     *
     * @param src source connection point
     * @param dst destination connection point
     * @return
     */
    boolean isTraversable(ConnectPoint src, ConnectPoint dst);

    /**
     * Indicates whether or not the all physical devices mapped by the given
     * virtual device are reachable.
     *
     * @param deviceId
     * @return
     */
    boolean isReachable(DeviceId deviceId);
}
