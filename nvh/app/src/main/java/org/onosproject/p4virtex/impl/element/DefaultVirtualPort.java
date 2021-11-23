package org.onosproject.p4virtex.impl.element;

import org.onosproject.net.*;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualPort;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

public class DefaultVirtualPort extends DefaultPort implements VirtualPort {

    private NetworkId networkId;
    private DeviceId deviceId;
    private PortNumber portNumber;
    private ConnectPoint phyConnectPoint;

    public DefaultVirtualPort(NetworkId networkId, Device device, PortNumber portNumber, ConnectPoint phyConnectPoint) {
        this(networkId, device, portNumber, false, phyConnectPoint);
    }

    public DefaultVirtualPort(NetworkId networkId, Device device, PortNumber portNumber, boolean isEnabled, ConnectPoint phyConnectPoint) {
        super(device, portNumber, isEnabled, DefaultAnnotations.builder().build());
        this.networkId = networkId;
        this.deviceId = device.id();
        this.portNumber = portNumber;
        this.phyConnectPoint = phyConnectPoint;
    }

    public NetworkId getNetworkId() {
        return networkId;
    }

    public ConnectPoint getPhyConnectPoint() {
        return phyConnectPoint;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public PortNumber getPortNumber() {
        return portNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId, deviceId, portNumber, phyConnectPoint);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof DefaultVirtualPort) {
            DefaultVirtualPort that = (DefaultVirtualPort) obj;
            return super.equals(that)
                    && Objects.equals(this.networkId, that.networkId)
                    && Objects.equals(this.deviceId, that.deviceId)
                    && Objects.equals(this.portNumber, that.portNumber)
                    && Objects.equals(this.phyConnectPoint, that.phyConnectPoint);
        }
        return false;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("networkId", networkId)
                .add("deviceId", deviceId)
                .add("portNumber", portNumber)
                .add("phyConnectPoint", phyConnectPoint)
                .toString();
    }

    @Override
    public ConnectPoint phyConnectPoint() {
        return this.phyConnectPoint;
    }
}
