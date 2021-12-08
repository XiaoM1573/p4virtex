package org.onosproject.p4virtex.element;

import org.onosproject.net.ConnectPoint;
import org.onosproject.net.Port;

/**
 * 虚拟端口抽象接口
 */
public interface VirtualPort extends VirtualElement, Port {

    /**
     * 对应底层物理网络中的connect point
     *
     * @return physical connect point
     */
    ConnectPoint phyConnectPoint();
}
