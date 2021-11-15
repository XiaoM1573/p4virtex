package org.onosproject.p4virtex.impl.element;

import org.onlab.packet.ChassisId;
import org.onosproject.net.DefaultDevice;
import org.onosproject.net.DeviceId;
import org.onosproject.net.provider.ProviderId;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualDevice;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

public final class DefaultVirtualDevice extends DefaultDevice implements VirtualDevice {

    private final NetworkId networkId;

    private static final String VIRTUAL = "virtual";

    private static final ProviderId providerId = new ProviderId(VIRTUAL, VIRTUAL);

    public DefaultVirtualDevice(NetworkId networkId, DeviceId deviceId) {
        // identity of the provider
        // device id
        // device type
        // device manufacturer
        // device HW version
        // device SW version
        // device serial number
        // chassis id
        // optional key/value annotations
        super(providerId, deviceId, Type.VIRTUAL, VIRTUAL, VIRTUAL, VIRTUAL, VIRTUAL, new ChassisId(0));
        this.networkId = networkId;
    }

    public NetworkId networkId() {
        return networkId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof DefaultVirtualDevice) {
            DefaultVirtualDevice that = (DefaultVirtualDevice) obj;
            return super.equals(that) && Objects.equals(this.networkId, that.networkId);
        }
        return false;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("networkId", networkId)
                .toString();
    }

}
