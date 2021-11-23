package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractChoicesCompleter;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualDevice;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;
import org.onosproject.p4virtex.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.onosproject.cli.AbstractShellCommand.get;

@Service
public class VirtualDeviceCompleter extends AbstractChoicesCompleter {


    @Override
    protected List<String> choices() {
        // parse arguments
        String[] args = commandLine.getArguments();
        // assumes that the first argument which can be parsed to a number is network id
        for (String arg : args) {
            if (arg.matches("[0-9]+")) {
                long networkId = Long.parseLong(arg);
                return getSortedVirtualDevices(networkId).stream()
                        .map(virtualDevice -> virtualDevice.deviceId().toString())
                        .collect(Collectors.toList());
            }
        }
        return Collections.singletonList("Missing network id");
    }

    private List<VirtualDevice> getSortedVirtualDevices(long networkId) {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        List<VirtualDevice> virtualDevices = new ArrayList<>();
        virtualDevices.addAll(service.getVirtualDevices(NetworkId.networkId(networkId)));
        Collections.sort(virtualDevices, Comparators.VIRTUAL_DEVICE_COMPARATOR);
        return virtualDevices;
    }
}
