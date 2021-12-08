package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.CommandLine;
import org.apache.karaf.shell.api.console.Completer;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.support.completers.StringsCompleter;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.element.VirtualNetwork;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;
import org.onosproject.p4virtex.utils.Comparators;

import java.util.*;

@Service
public class VirtualNetworkCompleter implements Completer {
    @Override
    public int complete(Session session, CommandLine commandLine, List<String> candidates) {
        StringsCompleter delegate = new StringsCompleter();

        VirtualNetworkAdminService service = AbstractShellCommand.get(VirtualNetworkAdminService.class);

        List<VirtualNetwork> virtualNetworks = new ArrayList<>();

        Set<TenantId> tenantIds = service.getTenantIds();

        tenantIds.forEach(tenantId -> virtualNetworks.addAll(service.getVirtualNetworks(tenantId)));

        Collections.sort(virtualNetworks, Comparators.VIRTUAL_NETWORK_COMPARATOR);

        SortedSet<String> strings = delegate.getStrings();

        virtualNetworks.forEach(virtualNetwork -> strings.add(virtualNetwork.networkId().toString()));

        return delegate.complete(session, commandLine, candidates);
    }
}
