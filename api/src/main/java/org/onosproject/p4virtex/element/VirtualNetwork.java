package org.onosproject.p4virtex.element;

import org.onosproject.net.TenantId;

public interface VirtualNetwork {


    TenantId tenantId();

    NetworkId networkId();

}
