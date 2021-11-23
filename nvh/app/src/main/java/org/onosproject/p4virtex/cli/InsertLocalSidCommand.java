package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onlab.packet.Ip6Address;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.cli.net.DeviceIdCompleter;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.device.DeviceService;
import org.onosproject.p4virtex.impl.service.TunnelService;

@Service
@Command(scope = "onos", name = "local-sid-insert", description = "Insert sid to device")
public class InsertLocalSidCommand extends AbstractShellCommand {

    @Argument(name = "deviceId", description = "Device ID", required = true)
    @Completion(DeviceIdCompleter.class)
    String deviceId = null;

    @Argument(index= 1, name = "sid", description = "Local Sid", required = true)
    String sid = null;

    @Override
    protected void doExecute() throws Exception {
        DeviceService deviceService = get(DeviceService.class);
        TunnelService tunnelService = get(TunnelService.class);

        Device device = deviceService.getDevice(DeviceId.deviceId(deviceId));
        if (device == null) {
            print("The device is not existed");
            return;
        }

        Ip6Address ip6Address = Ip6Address.valueOf(sid);

        tunnelService.insertLocalSid(device.id(), ip6Address);
        print("Insert local sid successfully. ");
    }
}
