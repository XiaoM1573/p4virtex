package org.onosproject.p4virtex.element;

import org.onlab.util.Identifier;

public final class NetworkId extends Identifier<Long> {

    /**
     * Represents no virtual network, or an unspecified virtual network
     */
    public static final NetworkId NONE = new NetworkId();

    private NetworkId(long id) {
        super(id);
    }

    protected NetworkId() {
        super(-1L);
    }

    /**
     * Creates a virtual network id using the supplied backing id.
     *
     * @param id virtual network id
     * @return virtual network identifier
     */
    public static NetworkId virtualNetworkId(long id) {
        return new NetworkId(id);
    }

}
