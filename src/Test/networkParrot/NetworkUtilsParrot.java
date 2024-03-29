package Test.networkParrot;

import java.net.*;
import java.util.Enumeration;

/**
 * @author toon
 */
public class NetworkUtilsParrot {

    public static InetAddress getMyIp() {
        String ip;
        try {
            Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                java.net.NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();
                    if (addr instanceof Inet4Address) {
                        ip = addr.getHostAddress();
                        return InetAddress.getByName(ip);
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static InetAddress getBroadcastAddress() {
        String ip;
        try {
            Enumeration<java.net.NetworkInterface> interfaces = java.net.NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                java.net.NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                for(InterfaceAddress ifaceAddr : iface.getInterfaceAddresses()) {
                    InetAddress bcast = ifaceAddr.getBroadcast();
                    if(bcast == null)
                        continue;
                    return bcast;
                }

            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
