package org.onosproject.p4virtex.element;

import org.onlab.util.Identifier;

import java.util.Objects;

/**
 * 用于标识虚拟网络和物理网络
 */
public final class NetworkId extends Identifier<Long> {

    // 标识一个未指定的网络
    public static final NetworkId NONE = new NetworkId(-1L);

    // 标示底层的物理网络
    public static final NetworkId PHYSICAL = networkId(0L);

    /**
     * 判断当前网络是否是虚拟网络
     *
     * @return 如果是虚拟网络，返回true
     */
    public boolean isVirtualNetworkId() {
        return (!Objects.equals(this, NONE)) && (!Objects.equals(this, PHYSICAL));
    }

    // 禁止公共构造函数
    private NetworkId(long id) {
        super(id);
    }

    protected NetworkId() {
        super(-1L);
    }

    /**
     * 创建新的网络id, 后面配合IdGenerator，生成唯一的network id
     * 详细用法可见： DistributedVirtualNetworkStore
     *
     * @param id network id
     * @return network identifier
     */
    public static NetworkId networkId(long id) {
        return new NetworkId(id);
    }
}
