package org.onosproject.p4virtex.element;

import org.onosproject.net.*;

import java.util.Objects;

public class DefaultVirtualPort extends DefaultPort implements VirtualPort {
    private final NetworkId networkId;
    private final ConnectPoint phyConnectPoint;

    public DefaultVirtualPort(NetworkId networkId, Device virtualDevice, PortNumber virtualPortNumber, ConnectPoint phyConnectPoint) {
        // 默认虚拟端口的isEnabled置为true
        super(virtualDevice, virtualPortNumber, true, DefaultAnnotations.builder().build());
        this.networkId = networkId;
        this.phyConnectPoint = phyConnectPoint;
    }


    @Override
    public NetworkId networkId() {
        return networkId;
    }

    @Override
    public ConnectPoint phyConnectPoint() {
        return phyConnectPoint;
    }

    // 重写hashcode和equals方法
    @Override
    public int hashCode() {
        return Objects.hash(networkId, phyConnectPoint);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DefaultVirtualPort) {
            DefaultVirtualPort that = (DefaultVirtualPort) obj;
            return super.equals(that) &&
                    Objects.equals(this.networkId, that.networkId) &&
                    Objects.equals(this.number(), that.number()) &&
                    Objects.equals(this.phyConnectPoint, that.phyConnectPoint);
        }
        return false;
    }
}
