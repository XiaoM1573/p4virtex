package org.onosproject.p4virtex.element;

import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;

/**
 * 虚拟设备的抽象接口
 * 虚拟设备包括P4定义的SWITCH, ROUTER, FIREWALL, BALANCER, IPS, IDS...
 */
public interface VirtualDevice extends VirtualElement, Device {


}
