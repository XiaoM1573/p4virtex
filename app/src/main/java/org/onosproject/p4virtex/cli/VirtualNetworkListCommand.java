package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.element.VirtualNetwork;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;
import org.onosproject.p4virtex.utils.Comparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Command(scope = "onos", name = "vns", description = "List all virtual networks.")
public class VirtualNetworkListCommand extends AbstractShellCommand {

    private static final String FORMAT_PRINT_VIRTUAL_NETWORK = "tenantId=%s, networkId=%s";

    @Option(name = "--tenantId", description = "tenant Id")
    @Completion(TenantCompleter.class)
    private String tenantId = null;

    @Override
    protected void doExecute() throws Exception {
        getVirtualNetworks().forEach(this::printVirtualNetwork);
    }

    private List<VirtualNetwork> getVirtualNetworks() {
        VirtualNetworkAdminService service = get(VirtualNetworkAdminService.class);
        List<VirtualNetwork> virtualNetworks = new ArrayList<>();
        Set<TenantId> tenantIds = service.getTenantIds();
        if (tenantId != null) {
            tenantIds = tenantIds.stream().filter(_tenantId -> _tenantId.id().equals(tenantId)).collect(Collectors.toSet());
        }
        tenantIds.forEach(tenantId -> virtualNetworks.addAll(service.getVirtualNetworks(tenantId)));
        Collections.sort(virtualNetworks, Comparators.VIRTUAL_NETWORK_COMPARATOR);
        return virtualNetworks;
    }

    private void printVirtualNetwork(VirtualNetwork virtualNetwork) {
        print(FORMAT_PRINT_VIRTUAL_NETWORK, virtualNetwork.tenantId(), virtualNetwork.networkId());
    }
}
