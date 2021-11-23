package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;

@Service
@Command(scope = "onos", name = "vn-add-tenant", description = "Creates a new tenant. ")
public class TenantAddCommand extends AbstractShellCommand {

    @Argument(name = "tenantId", description = "Tenant ID", required = true, multiValued = false)
    String tenantId = null;

    @Override
    protected void doExecute() throws Exception {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        service.registerTenantId(TenantId.tenantId(tenantId));
        print("Tenant successfully added.");
    }
}
