package org.onosproject.p4virtex.element;

import org.onlab.packet.ChassisId;
import org.onosproject.net.DefaultDevice;
import org.onosproject.net.DeviceId;
import org.onosproject.net.provider.ProviderId;

import java.util.Objects;

/**
 * 默认VirtualDevice的实现类
 * <p>
 * TODO. 添加Pipelines指定功能
 */
public class DefaultVirtualDevice extends DefaultDevice implements VirtualDevice {

    private static final String VIRTUAL = "virtual";

    private static final ProviderId PID = new ProviderId(VIRTUAL, VIRTUAL);

    private final NetworkId networkId;

    public DefaultVirtualDevice(NetworkId networkId, DeviceId id) {
        super(PID, id, Type.VIRTUAL, VIRTUAL, VIRTUAL, VIRTUAL, VIRTUAL, new ChassisId(0));
        this.networkId = networkId;
    }


    @Override
    public NetworkId networkId() {
        return networkId;
    }

    // 需要重写hashcode和equals方法，用于后面的map和set存储

    @Override
    public int hashCode() {
        return Objects.hash(networkId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DefaultVirtualDevice) {
            DefaultVirtualDevice that = (DefaultVirtualDevice) obj;
            return super.equals(that) && Objects.equals(this.networkId, that.networkId);
        }
        return false;
    }
}
