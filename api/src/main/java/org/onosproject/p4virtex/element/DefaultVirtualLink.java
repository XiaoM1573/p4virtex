package org.onosproject.p4virtex.element;

import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DefaultAnnotations;
import org.onosproject.net.DefaultLink;
import org.onosproject.net.provider.ProviderId;

import java.util.Objects;

public class DefaultVirtualLink extends DefaultLink implements VirtualLink {

    private static final String VIRTUAL = "virtual";

    private static final ProviderId PID = new ProviderId(VIRTUAL, VIRTUAL);

    private final NetworkId networkId;

    public DefaultVirtualLink(NetworkId networkId, ConnectPoint src, ConnectPoint dst, State state) {
        super(PID, src, dst, Type.VIRTUAL, state, DefaultAnnotations.builder().build());
        this.networkId = networkId;
    }

    @Override
    public NetworkId networkId() {
        return networkId;
    }

    // 重写hashcode和equals方法

    @Override
    public int hashCode() {
        return Objects.hash(networkId, src(), dst());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof DefaultVirtualLink) {
            DefaultVirtualLink that = (DefaultVirtualLink) obj;
            return super.equals(that) && Objects.equals(this.networkId, that.networkId);
        }
        return false;
    }
}
