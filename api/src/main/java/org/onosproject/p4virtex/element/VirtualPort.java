package org.onosproject.p4virtex.element;

import org.onosproject.net.ConnectPoint;
import org.onosproject.net.Port;

public interface VirtualPort extends VirtualElement, Port {

    ConnectPoint phyConnectPoint();
}
