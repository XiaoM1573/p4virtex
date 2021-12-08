package org.onosproject.p4virtex.element;

import org.onosproject.net.TenantId;

import java.util.Objects;

import static com.google.common.base.MoreObjects.toStringHelper;

public final class DefaultVirtualNetwork implements VirtualNetwork {

    private final NetworkId networkId;
    private final TenantId tenantId;

    public DefaultVirtualNetwork(NetworkId networkId, TenantId tenantId) {
        this.networkId = networkId;
        this.tenantId = tenantId;
    }

    @Override
    public NetworkId networkId() {
        return networkId;
    }

    @Override
    public TenantId tenantId() {
        return tenantId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(networkId, tenantId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DefaultVirtualNetwork) {
            DefaultVirtualNetwork that = (DefaultVirtualNetwork) obj;
            return Objects.equals(this.networkId, that.networkId)
                    && Objects.equals(this.tenantId, that.tenantId);
        }
        return false;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("networkId", this.networkId)
                .add("tenantId", this.tenantId)
                .toString();
    }


}
