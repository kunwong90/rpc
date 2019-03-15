package com.learn.rpc.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class IpAddressUtil {

    private IpAddressUtil() {

    }


    public static String getLocalHostIpAddress() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }



}
