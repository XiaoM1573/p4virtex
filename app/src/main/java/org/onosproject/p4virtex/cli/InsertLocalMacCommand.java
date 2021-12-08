package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onlab.packet.MacAddress;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.cli.net.DeviceIdCompleter;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.device.DeviceService;
import org.onosproject.p4virtex.impl.service.TunnelService;

@Service
@Command(scope = "onos", name = "local-mac-insert", description = "Insert mac address to device")
public class InsertLocalMacCommand extends AbstractShellCommand {

    @Argument(name = "deviceId", description = "Device ID", required = true)
    @Completion(DeviceIdCompleter.class)
    String deviceId = null;

    @Argument(index = 1, name = "mac", description = "Mac Address", required = true)
    String mac = null;

    @Override
    protected void doExecute() throws Exception {
        DeviceService deviceService = get(DeviceService.class);
        TunnelService tunnelService = get(TunnelService.class);

        Device device = deviceService.getDevice(DeviceId.deviceId(deviceId));
        if (device == null) {
            print("The device is not existed");
            return;
        }
        MacAddress macAddress = MacAddress.valueOf(mac);
        tunnelService.insertLocalMac(device.id(), macAddress);
        print("Insert local mac successfully. ");
    }

}
