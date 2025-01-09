package me.mathewcibi.quickshare.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPAddressConverter {
    public static String convertIPTo6DigitCode(String ipAddress) {
        try {
            InetAddress inetAddress = InetAddress.getByName(ipAddress);
            byte[] ipBytes = inetAddress.getAddress();
            long ipLong = 0;
            for (byte b : ipBytes) {
                ipLong = (ipLong << 8) | (b & 0xFF);
            }
            return String.format("%06d", ipLong % 1000000);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convert6DigitCodeToIP(String code) {
        try {
            long ipLong = Long.parseLong(code);
            byte[] ipBytes = new byte[4];
            for (int i = 3; i >= 0; i--) {
                ipBytes[i] = (byte) (ipLong & 0xFF);
                ipLong >>= 8;
            }
            return InetAddress.getByAddress(ipBytes).getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
