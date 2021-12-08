package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.cli.net.DeviceIdCompleter;
import org.onosproject.cli.net.PortNumberCompleter;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.DeviceService;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.element.VirtualDevice;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;
import org.onosproject.p4virtex.service.VirtualNetworkService;

import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

@Service
@Command(scope = "onos", name = "vn-create-port"
        , description = "Creates a new virtual port. ")
public class VirtualPortCreateCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "networkId", description = "network id"
            , required = true)
    @Completion(VirtualNetworkCompleter.class)
    Long networkId = null;

    @Argument(index = 1, name = "deviceId", description = "virtual device id"
            , required = true)
    @Completion(VirtualDeviceCompleter.class)
    String deviceId = null;

    @Argument(index = 2, name = "portNum", description = "virtual device port number"
            , required = true)
    Integer portNum = null;

    @Argument(index = 3, name = "phyDeviceId", description = "physical device id")
    @Completion(DeviceIdCompleter.class)
    String phyDeviceId = null;

    @Argument(index = 4, name = "phyPortNum", description = "physical port number")
    @Completion(PortNumberCompleter.class)
    Integer phyPortNum = null;

    @Override
    protected void doExecute() throws Exception {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);

        DeviceService deviceService = get(DeviceService.class);

        VirtualDevice virtualDevice = getVirtualDevices(DeviceId.deviceId(deviceId));
        checkNotNull(virtualDevice, "The virtual device does not exist.");

        ConnectPoint phyConnectPoint = null;
        if (phyDeviceId != null && phyPortNum != null) {
            phyConnectPoint = new ConnectPoint(DeviceId.deviceId(phyDeviceId), PortNumber.portNumber(phyPortNum));
            checkNotNull(phyConnectPoint, "The physical device or port does not exist. ");
        }

        service.createVirtualPort(NetworkId.networkId(networkId), DeviceId.deviceId(deviceId), PortNumber.portNumber(portNum), phyConnectPoint);
        print("Virtual port successfully created.");

    }

    /**
     * Get the target virtual device
     *
     * @param targetDeviceId device identifier
     * @return matching virtual device, or null
     */
    private VirtualDevice getVirtualDevices(DeviceId targetDeviceId) {
        VirtualNetworkService service = get(VirtualNetworkService.class);
        Set<VirtualDevice> virtualDevices = service.getVirtualDevices(NetworkId.networkId(networkId));

        for (VirtualDevice virtualDevice : virtualDevices) {
            if (virtualDevice.id().equals(targetDeviceId)) {
                return virtualDevice;
            }
        }
        return null;
    }


}
