package org.onosproject.p4virtex.utils;

import org.onosproject.p4virtex.element.VirtualDevice;
import org.onosproject.p4virtex.element.VirtualNetwork;
import org.onosproject.p4virtex.element.VirtualPort;

import java.util.Comparator;

/**
 * Various comparators
 */
public final class Comparators {

    // Ban construction
    private Comparators() {

    }

    public static final Comparator<VirtualNetwork> VIRTUAL_NETWORK_COMPARATOR =
            (v1, v2) -> {
                int compareId = v1.tenantId().toString().compareTo(v2.tenantId().toString());
                return (compareId != 0) ? compareId : Long.signum(v1.networkId().id() - v2.networkId().id());
            };

    public static final Comparator<VirtualDevice> VIRTUAL_DEVICE_COMPARATOR =
            (v1, v2) -> v1.deviceId().toString().compareTo(v2.deviceId().toString());

    public static final Comparator<VirtualPort> VIRTUAL_PORT_COMPARATOR =
            (v1, v2) -> v1.number().toString().compareTo(v2.number().toString());
}
