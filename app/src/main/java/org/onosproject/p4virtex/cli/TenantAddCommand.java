package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;

@Service
@Command(scope = "onos", name = "vn-add-tenant", description = "Creates a new tenant of virtual network.")
public class TenantAddCommand extends AbstractShellCommand {

    @Argument(index = 0, name = "tenantId", description = "Tenant ID", required = true, multiValued = false)
    String tenantId = null;

    @Override
    protected void doExecute() throws Exception {

    }
}
