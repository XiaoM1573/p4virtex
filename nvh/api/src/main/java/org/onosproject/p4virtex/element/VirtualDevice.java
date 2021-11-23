package org.onosproject.p4virtex.element;

import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;

public interface VirtualDevice extends VirtualElement, Device {

    NetworkId networkId();

    DeviceId deviceId();

}
