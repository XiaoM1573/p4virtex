package org.onosproject.p4virtex.element;

import org.onosproject.net.Link;

/**
 * 虚拟链路的抽象接口
 * 本质上包含有 一对多的 关系
 */
public interface VirtualLink extends VirtualElement, Link {

    /**
     * 用于实现虚拟链路的隧道ID
     * 本部分预采用SRv6的BSID(bing sid)思想来实现两个connect point之间的连接
     * 详情可以见
     * TODO.
     * @return
     */
    // TunnelId tunnelId();

}
