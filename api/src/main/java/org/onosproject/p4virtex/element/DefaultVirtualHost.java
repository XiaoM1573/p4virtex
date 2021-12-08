package org.onosproject.p4virtex.element;

import org.onlab.packet.IpAddress;
import org.onlab.packet.MacAddress;
import org.onlab.packet.VlanId;
import org.onosproject.net.DefaultAnnotations;
import org.onosproject.net.DefaultHost;
import org.onosproject.net.HostId;
import org.onosproject.net.HostLocation;
import org.onosproject.net.provider.ProviderId;

import java.util.Objects;
import java.util.Set;

public class DefaultVirtualHost extends DefaultHost implements VirtualHost {

    private static final String VIRTUAL = "virtual";

    private static final ProviderId PID = new ProviderId(VIRTUAL, VIRTUAL);

    private final NetworkId networkId;

    public DefaultVirtualHost(NetworkId networkId, HostId id, MacAddress macAddress,
                              VlanId vlan, Set<HostLocation> locations, Set<IpAddress> ips) {

        super(PID, id, macAddress, vlan, locations, ips, true, DefaultAnnotations.builder().build());
        this.networkId = networkId;
    }


    @Override
    public NetworkId networkId() {
        return networkId;
    }

    // 重写hashcode和equals方法
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
}
