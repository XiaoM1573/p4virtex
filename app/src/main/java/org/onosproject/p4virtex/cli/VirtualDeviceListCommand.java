package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualDevice;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;
import org.onosproject.p4virtex.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Command(scope = "onos", name = "vn-devices", description = "Lists all virtual devices in a virtual network. ")
public class VirtualDeviceListCommand extends AbstractShellCommand {

    private static final String FORMAT_PRINT_VIRTUAL_DEVICE = "deviceId=%s";

    @Argument(name = "networkId", description = "network id", required = true)
    @Completion(VirtualNetworkCompleter.class)
    Long networkId = null;

    @Override
    protected void doExecute() throws Exception {
        List<VirtualDevice> devices = getSortedVirtualDevices();
        devices.forEach(this::printVirtualDevice);
    }

    private List<VirtualDevice> getSortedVirtualDevices() {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        List<VirtualDevice> virtualDevices = new ArrayList<>();
        virtualDevices.addAll(service.getVirtualDevices(NetworkId.networkId(networkId)));
        Collections.sort(virtualDevices, Comparators.VIRTUAL_DEVICE_COMPARATOR);
        return virtualDevices;
    }

    private void printVirtualDevice(VirtualDevice virtualDevice) {
        print(FORMAT_PRINT_VIRTUAL_DEVICE, virtualDevice.deviceId());
    }
}
