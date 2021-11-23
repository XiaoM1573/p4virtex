package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;
import org.onosproject.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Command(scope = "onos", name = "vn-tenants",
        description = "Lists all virtual network of specified tenant. ")
public class TenantListCommand extends AbstractShellCommand {
    private static final String FORMAT_PRINT_TENANT = "tenantId=%s";

    @Override
    protected void doExecute() throws Exception {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        List<TenantId> tenants = new ArrayList<>();
        tenants.addAll(service.getTenantIds());
        Collections.sort(tenants, Comparators.TENANT_ID_COMPARATOR);
        tenants.forEach(this::printTenant);

    }

    private void printTenant(TenantId tenantId) {
        print(FORMAT_PRINT_TENANT, tenantId.id());
    }
}
