package org.onosproject.p4virtex.srv6;

import org.onlab.packet.Ip6Address;

import java.util.Arrays;

public class Sid {

    private Ip6Address ip6Address;

    private Short domainId;

    private Integer networkId;

    private Short deviceId;

    private Long funcType;

    public Sid(Ip6Address ip6Address) {
        this.ip6Address = ip6Address;
    }

    public Sid(Short domainId, Integer networkId, Short deviceId, Long funcType) {
        this.domainId = domainId;
        this.networkId = networkId;
        this.deviceId = deviceId;
        this.funcType = funcType;
        this.ip6Address = translateToIpv6(domainId, networkId, deviceId, funcType);
    }

    public Ip6Address getIp6Address() {
        return ip6Address;
    }

    public void setIp6Address(Ip6Address ip6Address) {
        this.ip6Address = ip6Address;
    }


    private Ip6Address translateToIpv6(Short domainId, Integer networkId, Short deviceId, Long funcType) {
        byte[] b = new byte[16];
        System.arraycopy(shortToByte(domainId), 0, b, 0, 2);
        System.arraycopy(intToByte(networkId), 0, b, 2, 4);
        System.arraycopy(shortToByte(deviceId), 0, b, 6, 2);
        System.arraycopy(longToByte(funcType), 0, b, 8, 8);
        return Ip6Address.valueOf(b);
    }

    private byte[] shortToByte(short s) {
        int temp = s;
        byte[] b = new byte[2];
        for (int j = b.length - 1; j >= 0; j--) {
            b[j] = Integer.valueOf(temp & 0xff).byteValue();
            temp = temp >> 8;
        }
        return b;
    }

    private byte[] intToByte(int i) {
        byte[] b = new byte[4];
        for (int j = b.length - 1; j >= 0; j--) {
            b[j] = Integer.valueOf(i & 0xff).byteValue();
            i = i >> 8;
        }
        return b;
    }

    private byte[] longToByte(long l) {
        byte[] b = new byte[8];
        for (int j = b.length - 1; j >= 0; j--) {
            b[j] = Long.valueOf(l & 0xff).byteValue();
            l = l >> 8;
        }
        return b;
    }
}
