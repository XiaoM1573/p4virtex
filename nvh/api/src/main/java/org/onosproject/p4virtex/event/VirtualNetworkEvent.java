package org.onosproject.p4virtex.event;

import org.onosproject.event.AbstractEvent;
import org.onosproject.p4virtex.element.NetworkId;

public class VirtualNetworkEvent extends AbstractEvent<VirtualNetworkEvent.Type, NetworkId> {
    /**
     * Type of virtual network events.
     */
    public enum Type {


        TENANT_REGISTERED,

        TENANT_UNREGISTERED,

        NETWORK_ADDED,

        NETWORK_REMOVED,

        VIRTUAL_DEVICE_ADDED,

        VIRTUAL_DEVICE_REMOVED,

        VIRTUAL_PORT_ADDED,

        VIRTUAL_PORT_REMOVED,

    }

    /**
     * Creates an event of a given type and for the specified virtual network
     * @param type
     * @param networkId
     */

    public VirtualNetworkEvent(Type type, NetworkId networkId) {
        super(type, networkId);
    }

}
