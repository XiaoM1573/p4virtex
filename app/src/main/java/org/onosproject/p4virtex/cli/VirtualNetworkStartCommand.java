package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;

@Service
@Command(scope = "onos", name = "vn-start", description = "Start a virtual network. ")
public class VirtualNetworkStartCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "networkId", description = "network id", required = true)
    @Completion(VirtualNetworkCompleter.class)
    Long networkId = null;

    @Override
    protected void doExecute() throws Exception {

    }
}
