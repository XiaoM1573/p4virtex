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
import org.onosproject.net.PortNumber;
import org.onosproject.net.device.DeviceService;
import org.onosproject.p4virtex.impl.SimpleApp;

@Service
@Command(scope = "onos", name = "table0-insert", description = "Insert a flow rule into the table0")
public class Table0InsertCommand extends AbstractShellCommand {
    @Argument(index = 0, name = "uri", description = "Device ID", required = true, multiValued = false)
    @Completion(DeviceIdCompleter.class)
    String uri = null;

    @Argument(index = 1, name = "egressPort", description = "output port", required = true, multiValued = false)
    Integer egressPortNum = null;

    @Option(name = "-nw_src", description = "host ip source address", required = false, multiValued = false)
    String ipv4SrcAddr = null;

    @Option(name = "-nw_dst", description = "host ip dst address", required = false, multiValued = false)
    String ipv4DstAddr = null;

    @Option(name = "-nw_dst_mask", description = "host ip dst address", required = false, multiValued = false)
    String ipv4DstAddrMask;

    @Override
    protected void doExecute() throws Exception {
        DeviceService deviceService = get(DeviceService.class);
        SimpleApp simpleApp = get(SimpleApp.class);
        Device device = deviceService.getDevice(DeviceId.deviceId(uri));
        if (device == null) {
            print("Device %s is not found", uri);
            return;
        }

        // convert param to specified type
        Ip4Address _ipv4DstAddr = Ip4Address.valueOf(ipv4DstAddr);
        long _ipv4DstAddrMask = Long.decode(ipv4DstAddrMask);

        print("Installing forward rule on device %s", uri);

        simpleApp.insertForwardRule(device.id(), _ipv4DstAddr, _ipv4DstAddrMask, true, PortNumber.portNumber(egressPortNum));
    }
}
