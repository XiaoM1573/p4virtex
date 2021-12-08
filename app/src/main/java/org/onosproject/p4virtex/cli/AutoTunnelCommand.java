package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.cli.net.HostIdCompleter;
import org.onosproject.net.Host;
import org.onosproject.net.HostId;
import org.onosproject.net.host.HostService;
import org.onosproject.p4virtex.impl.service.TunnelService;

@Service
@Command(scope = "onos", name = "auto-tunnel", description = "Provide tunnel between hosts")
public class AutoTunnelCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "networkId", description = "Virtual Network ID", required = true)
    @Completion(VirtualNetworkCompleter.class)
    Long NetworkId;

    @Argument(index = 1, name = "srcHost", description = "Source Host", required = true)
    @Completion(HostIdCompleter.class)
    String srcHostId;

    @Argument(index = 2, name = "dstHost", description = "Destination Host", required = true)
    @Completion(HostIdCompleter.class)
    String dstHostId;

    @Override
    protected void doExecute() throws Exception {

        HostService hostService = get(HostService.class);
        TunnelService tunnelService = get(TunnelService.class);

        Host srcHost = hostService.getHost(HostId.hostId(srcHostId));
        Host dstHost = hostService.getHost(HostId.hostId(dstHostId));

        if (srcHost == null) {
            print("The source host is not existed");
            return;
        }

        if (dstHost == null) {
            print("The destination host is not existed");
            return;
        }

        tunnelService.autoSetTunnel(srcHost, dstHost);

        print("Successfully set tunnel. ");
    }
}
