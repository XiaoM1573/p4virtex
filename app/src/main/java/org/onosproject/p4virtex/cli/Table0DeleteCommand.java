package org.onosproject.p4virtex.cli;

import org.apache.karaf.shell.api.action.Argument;
import org.apache.karaf.shell.api.action.Command;
import org.apache.karaf.shell.api.action.Completion;
import org.apache.karaf.shell.api.action.Option;
import org.apache.karaf.shell.api.action.lifecycle.Service;
import org.onlab.packet.Ip4Address;
import org.onosproject.cli.AbstractShellCommand;
import org.onosproject.cli.net.DeviceIdCompleter;
import org.onosproject.net.Device;
import org.onosproject.net.DeviceId;
import org.onosproject.net.device.DeviceService;
import org.onosproject.net.pi.model.PiMatchFieldId;
import org.onosproject.p4virtex.impl.SimpleApp;

@Service
@Command(scope = "onos", name = "table0-delete", description = "Delete a flow rule from the table0")
public class Table0DeleteCommand extends AbstractShellCommand {
    @Argument(index = 0, name = "uri", description = "Device ID", required = true, multiValued = false)
    @Completion(DeviceIdCompleter.class)
    String uri = null;

    @Option(name = "--nw_dst", description = "host ip dst address", required = false, multiValued = false)
    String nwDst = null;

    @Option(name = "--nw_dst_mask", description = "ternary value of ip dst address", required = false, multiValued = false)
    String nwDstMask = null;

    @Override
    protected void doExecute() throws Exception {
        DeviceService deviceService = get(DeviceService.class);
        SimpleApp simpleApp = get(SimpleApp.class);
        Device device = deviceService.getDevice(DeviceId.deviceId(uri));
        if (device == null) {
            print("Device %s is not found", uri);
            return;
        }
        Ip4Address _nwDst = Ip4Address.valueOf(nwDst);
        long _nwDstMask = Long.decode(nwDstMask);


        simpleApp.deleteForwardRule(device.id(), _nwDst, _nwDstMask);
    }
}
