package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onlab.packet.Ip6Address;
import org.onlab.packet.Ip6Prefix;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.cli.net.DeviceIdCompleter;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.DeviceService;
import org.onosproject.p4virtex.impl.service.TunnelService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Command(scope = "onos", name = "transit-insert", description = "Insert transit rule to the device")
public class InsertTransitCommand extends AbstractShellCommand {

    @Argument(name = "deviceId", description = "Device ID", required = true)
    @Completion(DeviceIdCompleter.class)
    String deviceId = null;

    @Argument(index = 1, name = "ipv6Prefix", description = "ipv6 prefix", required = true)
    String ipv6Prefix = null;

    @Argument(index = 2, name = "segments", description = "srv6 segments", multiValued = true)
    List<String> segments = null;

    @Override
    protected void doExecute() throws Exception {
        DeviceService deviceService = get(DeviceService.class);
        TunnelService tunnelService = get(TunnelService.class);
        Device device = deviceService.getDevice(DeviceId.deviceId(deviceId));
        if (device == null) {
            print("The device is not existed");
            return;
        }
        Ip6Prefix ip6Prefix = Ip6Prefix.valueOf(ipv6Prefix);
        List<Ip6Address> sids = segments.stream().map(Ip6Address::valueOf).collect(Collectors.toList());
        tunnelService.insertTransitRule(device.id(), ip6Prefix, sids);
        print("Insert srv6 path successfully. ");

    }
}