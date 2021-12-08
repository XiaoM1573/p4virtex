package org.onosproject.p4virtex.element;

import org.onosproject.net.DeviceId;

import java.util.Objects;

public class VirtualDeviceId {

    private NetworkId networkId;
    private DeviceId deviceId;

    public VirtualDeviceId(NetworkId networkId, DeviceId deviceId) {
        this.networkId = networkId;
        this.deviceId = deviceId;
    }

    public NetworkId getNetworkId() {
        return networkId;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId, deviceId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof VirtualDeviceId) {
            VirtualDeviceId that = (VirtualDeviceId) obj;
            return this.deviceId.equals(that.deviceId) && this.networkId.equals(that.networkId);
        }
        return false;
    }
}
