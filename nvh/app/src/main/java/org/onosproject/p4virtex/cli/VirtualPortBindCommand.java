package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.cli.net.DeviceIdCompleter;
import org.onosproject.cli.net.PortNumberCompleter;

@Service
@Command(scope = "onos", name = "vn-bind-port",
        description = "Binds an existing virtual port with a physical port.")
public class VirtualPortBindCommand extends AbstractShellCommand {
    @Argument(index = 0, name = "networkId", description = "network id"
            , required = true)
    Long networkId = null;

    @Argument(index = 1, name = "deviceId", description = "virtual device id"
            , required = true)
    String deviceId = null;

    @Argument(index = 2, name = "portNum", description = "virtual device port number"
            , required = true)
    Integer portNum = null;

    @Argument(index = 3, name = "phyDeviceId", description = "physical device id")
    @Completion(DeviceIdCompleter.class)
    String phyDeiceId = null;

    @Argument(index = 4, name = "phyPortNum", description = "physical port number")
    @Completion(PortNumberCompleter.class)
    Integer phyPortNum = null;


    @Override
    protected void doExecute() throws Exception {

    }
}
