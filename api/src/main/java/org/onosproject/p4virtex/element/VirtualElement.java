package org.onosproject.p4virtex.element;

/**
 * 虚拟网络元素抽象接口，区别于原网络元素需要添加networkId属性，表明该网络元素属于哪一个虚拟网络
 */
public interface VirtualElement {

    NetworkId networkId();

}
