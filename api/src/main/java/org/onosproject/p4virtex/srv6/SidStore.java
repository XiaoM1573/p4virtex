package org.onosproject.p4virtex.srv6;

import org.onosproject.net.DeviceId;
import org.onosproject.p4virtex.element.NetworkId;

import java.util.Set;

public interface SidStore {

    void addSid(DeviceId deviceId, Sid sid);

    Set<Sid> getSidListByDeviceId(DeviceId deviceId);

    Set<Sid> getAllSidsByNetworkId(NetworkId networkId);
}
