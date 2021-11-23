package org.onosproject.p4virtex.impl.element;

import org.onosproject.net.Annotations;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DefaultAnnotations;
import org.onosproject.net.DefaultLink;
import org.onosproject.net.provider.ProviderId;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.TunnelId;
import org.onosproject.p4virtex.element.VirtualLink;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

public final class DefaultVirtualLink extends DefaultLink implements VirtualLink {

    private static final String VIRTUAL = "virtual";
    public static final ProviderId PID = new ProviderId(VIRTUAL, VIRTUAL);

    private NetworkId networkId;
    private TunnelId tunnelId;

    public DefaultVirtualLink(NetworkId networkId, ConnectPoint src, ConnectPoint dst, State state, TunnelId tunnelId) {
        super(PID, src, dst, Type.VIRTUAL, state, DefaultAnnotations.builder().build());
        this.networkId = networkId;
        this.tunnelId = tunnelId;
    }

    /**
     * Creates an infrastructure link using the supplied information.
     *
     * @param providerId  provider identity
     * @param src         link source
     * @param dst         link destination
     * @param type        link type
     * @param state       link state
     * @param annotations optional key/value annotations
     */
    protected DefaultVirtualLink(ProviderId providerId, ConnectPoint src, ConnectPoint dst, Type type, State state, Annotations... annotations) {
        super(providerId, src, dst, type, state, annotations);
    }

    public NetworkId networkId(){
        return networkId;
    }

    public TunnelId tunnelId() {
        return tunnelId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId, tunnelId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DefaultVirtualLink) {
           DefaultVirtualLink that = (DefaultVirtualLink) obj;
            return super.equals(that) &&
                    Objects.equals(this.networkId, that.networkId) &&
                    Objects.equals(this.tunnelId, that.tunnelId);
        }
        return false;
    }

    @Override
    public String toString() {
        return toStringHelper(this).add("networkId", networkId).add("tunnelId", tunnelId).toString();
    }
}
