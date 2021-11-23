package org.onosproject.p4virtex.element;

import org.onosproject.net.Link;

/**
 * Abstraction of a virtual link
 */
public interface VirtualLink extends VirtualElement, Link {

    /**
     * Returns the tunnel identifier to which this virtual link belongs.
     *
     * @return tunnel identifier
     */
    TunnelId tunnelId();

}
