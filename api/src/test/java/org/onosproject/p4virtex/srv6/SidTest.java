package org.onosproject.p4virtex.srv6;

import com.google.common.testing.EqualsTester;
import org.junit.Test;
import org.onlab.packet.Ip6Address;

public class SidTest {

    @Test
    public void testSidFormat() {
        Short domainId = Short.decode("0x000a");
        Integer networkId = Integer.decode("0x00000101");
        Short deviceId = Short.decode("0x0022");
        Long funcType = 1L;

        Sid sid = new Sid(domainId, networkId, deviceId, funcType);
        Ip6Address ip6Address0 = sid.getIp6Address();
        Ip6Address ip6Address1 = Ip6Address.valueOf("a:0:101:0022::1");

        new EqualsTester().addEqualityGroup(ip6Address0, ip6Address1).testEquals();

    }
}
