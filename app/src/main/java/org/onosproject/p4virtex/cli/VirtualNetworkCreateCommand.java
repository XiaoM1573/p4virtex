package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;

@Service
@Command(scope = "onos", name = "vn-create", description = "Creates a virtual network")
public class VirtualNetworkCreateCommand extends AbstractShellCommand {

    @Argument(name = "tenantId", description = "Tenant ID", required = true)
    @Completion(TenantCompleter.class)
    String tenantId = null;

    @Override
    protected void doExecute() throws Exception {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        service.createVirtualNetwork(TenantId.tenantId(tenantId));
        print("Virtual network successfully created. ");
    }
}
