package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.ConnectPoint;
import org.onosproject.net.DeviceId;
import org.onosproject.net.PortNumber;
import org.onosproject.p4virtex.element.NetworkId;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;

@Service
@Command(scope = "onos", name = "vn-create-link",
        description = "Creates a new virtual link in a network.")
public class VirtualLinkCreateCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "networkId", description = "Network ID",
            required = true)
    @Completion(VirtualNetworkCompleter.class)
    Long networkId = null;

    @Argument(index = 1, name = "srcDeviceId", description = "Source device ID",
            required = true)
    @Completion(VirtualDeviceCompleter.class)
    String srcDeviceId = null;

    @Argument(index = 2, name = "srcPortNum", description = "Source port number",
            required = true)
    @Completion(VirtualPortCompleter.class)
    Integer srcPortNum = null;

    @Argument(index = 3, name = "dstDeviceId", description = "Destination device ID",
            required = true)
    @Completion(VirtualDeviceCompleter.class)
    String dstDeviceId = null;

    @Argument(index = 4, name = "dstPortNum", description = "Destination port number",
            required = true, multiValued = false)
    @Completion(VirtualPortCompleter.class)
    Integer dstPortNum = null;

    @Option(name = "-b", aliases = "--bidirectional",
            description = "If this argument is passed in then the virtual link created will be bidirectional, " +
                    "otherwise the link will be unidirectional.")
    boolean bidirectional = false;

    @Override
    protected void doExecute() throws Exception {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        ConnectPoint src = new ConnectPoint(DeviceId.deviceId(srcDeviceId), PortNumber.portNumber(srcPortNum));
        ConnectPoint dst = new ConnectPoint(DeviceId.deviceId(dstDeviceId), PortNumber.portNumber(dstPortNum));

        service.createVirtualLink(NetworkId.networkId(networkId), src, dst);

        if (bidirectional) {
            service.createVirtualLink(NetworkId.networkId(networkId), dst, src);
        }
        print("Virtual link successfully created. ");
    }
}
