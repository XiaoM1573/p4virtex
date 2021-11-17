package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractChoicesCompleter;
import org.onosproject.net.DeviceId;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualPort;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;
import org.onosproject.p4virtex.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.onosproject.cli.AbstractShellCommand.get;

@Service
public class VirtualPortCompleter extends AbstractChoicesCompleter {
    @Override
    protected List<String> choices() {
        String[] args = commandLine.getArguments();
        for (String arg : args) {
            if (arg.matches("[0-9]+")) {
                long networkId = Long.parseLong(arg);
                // the last one is device id
                String deviceId = args[args.length - 1];
                return getSortedVirtualPorts(networkId, deviceId).stream()
                        .map(virtualPort -> virtualPort.number().toString())
                        .collect(Collectors.toList());
            }
        }
        return Collections.singletonList("Missing network id");
    }

    private List<VirtualPort> getSortedVirtualPorts(long networkId, String deviceId) {

        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        List<VirtualPort> virtualPorts = new ArrayList<>();
        virtualPorts.addAll(service.getVirtualPorts(NetworkId.networkId(networkId), DeviceId.deviceId(deviceId)));
        Collections.sort(virtualPorts, Comparators.VIRTUAL_PORT_COMPARATOR);
        return virtualPorts;
    }
}
