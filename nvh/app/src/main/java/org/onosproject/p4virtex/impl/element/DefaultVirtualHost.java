package org.onosproject.p4virtex.impl.element;

import org.onlab.packet.IpAddress;
import org.onlab.packet.MacAddress;
import org.onlab.packet.VlanId;
import org.onosproject.net.DefaultAnnotations;
import org.onosproject.net.DefaultHost;
import org.onosproject.net.HostId;
import org.onosproject.net.HostLocation;
import org.onosproject.net.provider.ProviderId;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualHost;

import java.util.Objects;
import java.util.Set;

import static com.google.common.base.MoreObjects.toStringHelper;

public class DefaultVirtualHost extends DefaultHost implements VirtualHost {

    private static final String VIRTUAL = "virtual";
    private static final ProviderId PID = new ProviderId(VIRTUAL, VIRTUAL);

    private final NetworkId networkId;

    private final HostId hostId;

    /**
     * Creates a virtual host attributed to the specified provider
     *
     * @param networkId    network identifier
     * @param hostId       host identifier
     * @param mac          host mac address
     * @param vlanId       host vlan identifier
     * @param hostLocation host location
     * @param ips          host IP addresses
     */
    public DefaultVirtualHost(NetworkId networkId, HostId hostId, MacAddress mac, VlanId vlanId, HostLocation hostLocation, Set<IpAddress> ips) {

        super(PID, hostId, mac, vlanId, hostLocation, ips, false, DefaultAnnotations.builder().build());
        this.networkId = networkId;
        this.hostId = hostId;
    }

    public NetworkId networkId() {
        return networkId;
    }

    public HostId hostId() {
        return hostId;
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

        if (obj instanceof DefaultVirtualHost) {
            DefaultVirtualHost that = (DefaultVirtualHost) obj;
            return super.equals(that) && Objects.equals(this.networkId, that.networkId);
        }

        return false;
    }

    @Override
    public String toString() {
        return toStringHelper(this)
                .add("networkId", networkId)
                .add("hostId", hostId)
                .toString();
    }

}
