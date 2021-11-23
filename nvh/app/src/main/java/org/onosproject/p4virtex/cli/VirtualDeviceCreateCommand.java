package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.DeviceId;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;

@Service
@Command(scope = "onos", name = "vn-create-device", description = "Creates a new virtual device in a network.")
public class VirtualDeviceCreateCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "networkId", description = "network id", required = true)
    @Completion(VirtualNetworkCompleter.class)
    Long networkId = null;

    @Argument(index = 1, name = "deviceId", description = "device id", required = true)
    String deviceId = null;

    @Override
    protected void doExecute() throws Exception {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        service.createVirtualDevice(NetworkId.networkId(networkId), DeviceId.deviceId(deviceId));
        print("Virtual device successfully created. ");
    }
}
