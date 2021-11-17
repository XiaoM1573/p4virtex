package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.DeviceId;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualDevice;
import org.onosproject.p4virtex.element.VirtualPort;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;
import org.onosproject.p4virtex.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Command(scope = "onos", name = "vn-ports", description = "List all virtual ports in a virtual network. ")
public class VirtualPortListCommand extends AbstractShellCommand {

    private static final String FORMAT_PRINT_VIRTUAL_PORT = "virtual portNumber=%s, physical deviceId=%s, physical portNumber=%s, isEnabled=%s";

    @Argument(index = 0, name = "networkId", description = "Network Id", required = true)
    @Completion(VirtualNetworkCompleter.class)
    Long networkId = null;

    @Argument(index = 1, name = "deviceId", description = "Virtual Device Id", required = true)
    @Completion(VirtualDeviceCompleter.class)
    String deviceId = null;

    @Override
    protected void doExecute() throws Exception {
        getSortedVirtualPorts().forEach(this::printVirtualPort);
    }

    private List<VirtualPort> getSortedVirtualPorts() {

        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        List<VirtualPort> virtualPorts = new ArrayList<>();
        virtualPorts.addAll(service.getVirtualPorts(NetworkId.networkId(networkId), DeviceId.deviceId(deviceId)));
        Collections.sort(virtualPorts, Comparators.VIRTUAL_PORT_COMPARATOR);
        return virtualPorts;
    }

    private void printVirtualPort(VirtualPort virtualPort) {
        if (virtualPort.phyConnectPoint() == null) {
            print(FORMAT_PRINT_VIRTUAL_PORT, virtualPort.number(), "None", "None", virtualPort.isEnabled());
        } else {
            print(FORMAT_PRINT_VIRTUAL_PORT, virtualPort.number(),
                    virtualPort.phyConnectPoint().deviceId(),
                    virtualPort.phyConnectPoint().port(),
                    virtualPort.isEnabled());
        }
    }
}
