package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onlab.packet.Ip6Address;
import org.onlab.packet.Ip6Prefix;
import org.onlab.packet.MacAddress;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.cli.net.DeviceIdCompleter;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.DeviceService;
import org.onosproject.p4virtex.impl.service.TunnelService;

@Service
@Command(scope = "onos", name = "routing-insert", description = "Insert routing rule to the device")
public class InsertRoutingCommand extends AbstractShellCommand {

    @Argument(name = "deviceId", description = "Device ID", required = true)
    @Completion(DeviceIdCompleter.class)
    String deviceId = null;

    @Argument(index = 1, name = "ipv6Prefix", description = "ipv6 prefix", required = true)
    String ipv6Prefix = null;

    @Argument(index = 2, name = "dstMac", description = "dst mac address", required = true)
    String dstMacAddress;

    @Argument(index = 3, name = "outputPort", description = "output port", required = true)
    Long outputPort;

    @Override
    protected void doExecute() throws Exception {
        DeviceService deviceService = get(DeviceService.class);
        TunnelService tunnelService = get(TunnelService.class);
        Device device = deviceService.getDevice(DeviceId.deviceId(deviceId));
        if (device == null) {
            print("The device is not existed");
            return;
        }
        tunnelService.insertRoutingRule(device.id(), Ip6Prefix.valueOf(ipv6Prefix),
                MacAddress.valueOf(dstMacAddress), PortNumber.portNumber(outputPort));
        print("Insert Routing Rule Successfully. ");
    }
}
