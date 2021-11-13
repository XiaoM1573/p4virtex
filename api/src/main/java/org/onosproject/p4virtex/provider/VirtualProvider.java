package org.onosproject.p4virtex.provider;

import org.onosproject.net.provider.ProviderId;

/**
 * role: translate virtual objects into physical objects
 */
public interface VirtualProvider {

    /**
     * Return the provider identifier
     *
     * @return provider identification
     */
    ProviderId id();
}
