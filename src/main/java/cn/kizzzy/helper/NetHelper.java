package cn.kizzzy.helper;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetHelper {

    /**
     * 获得内网IP
     */
    public static String getIntranetIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获得外网IP
     */
    public static String getInternetIp() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            Enumeration<InetAddress> addrs;
            String localIP = getIntranetIp();
            while (networks.hasMoreElements()) {
                addrs = networks.nextElement().getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress ip = addrs.nextElement();
                    if (ip instanceof Inet4Address && ip.isSiteLocalAddress() && !ip.getHostAddress().equals(localIP)) {
                        return ip.getHostAddress();
                    }
                }
            }

            return localIP;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
