package org.onosproject.p4virtex.service;

import org.onosproject.p4virtex.element.NetworkId;

public interface VirtualNetworkFlowRuleService {

    // 为当前虚拟网络内的虚拟节点分配sid
    void allocateSid(NetworkId networkId);

    //void insertRoutingRule();

}
