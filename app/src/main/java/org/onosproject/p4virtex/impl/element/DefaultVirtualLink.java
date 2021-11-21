package org.onosproject.p4virtex.impl.element;

import org.onosproject.net.Annotations;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DefaultLink;
import org.onosproject.net.provider.ProviderId;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualLink;

public final class DefaultVirtualLink extends DefaultLink implements VirtualLink {

    private static final String VIRTUAL = "virtual";
    public static final ProviderId PID = new ProviderId(VIRTUAL, VIRTUAL);

    private NetworkId networkId;





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
}
