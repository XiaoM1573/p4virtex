package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.DeviceId;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualPort;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Command(scope = "onos", name = "vn-port-state", description = "Modify the state of the virtual port (enable or disable)")
public class VirtualPortStateCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "networkId", description = "virtual network id", required = true)
    @Completion(VirtualNetworkCompleter.class)
    Long networkId = null;

    @Argument(index = 1, name = "deviceId", description = "virtual device id", required = true)
    @Completion(VirtualDeviceCompleter.class)
    String deviceId = null;

    @Argument(index = 2, name = "portNum", description = "virtual port number", required = true)
    @Completion(VirtualPortCompleter.class)
    Integer portNum = null;

    @Argument(index = 3, name = "portState", description = "virtual port state : enable or disable", required = true)
    String portState = null;

    @Override
    protected void doExecute() throws Exception {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);

        Set<VirtualPort> virtualPorts = service.getVirtualPorts(NetworkId.networkId(networkId), DeviceId.deviceId(deviceId));
        VirtualPort virtualPort = virtualPorts.stream().filter(p -> p.number().equals(portNum)).findFirst().get();
        checkNotNull(virtualPort, "The virtual port does not exist");

        boolean isEnabled;
        if ("enable".equals(portState)) {
            isEnabled = true;
        } else if ("disable".equals(portState)) {
            isEnabled = false;
        } else {
            print("Port state must be enable or disable");
            return;
        }

        service.updateVirtualPortState(NetworkId.networkId(networkId), DeviceId.deviceId(deviceId), virtualPort.number(), isEnabled);

        print("Virtual port state updated successfully.");
    }

}
