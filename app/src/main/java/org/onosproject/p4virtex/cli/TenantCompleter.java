package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.apache.karaf.shell.api.console.CommandLine;
import org.apache.karaf.shell.api.console.Completer;
import org.apache.karaf.shell.api.console.Session;
import org.apache.karaf.shell.support.completers.StringsCompleter;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.net.TenantId;
import org.onosproject.p4virtex.service.VirtualNetworkAdminService;

import java.util.List;
import java.util.SortedSet;

@Service
public class TenantCompleter implements Completer {
    @Override
    public int complete(Session session, CommandLine commandLine, List<String> candidates) {
        StringsCompleter delegate = new StringsCompleter();

        VirtualNetworkAdminService service = AbstractShellCommand.get(VirtualNetworkAdminService.class);

        SortedSet<String> strings = delegate.getStrings();

        for (TenantId tenantId : service.getTenantIds()) {
            strings.add(tenantId.id());
        }

        return delegate.complete(session, commandLine, candidates);
    }
}
