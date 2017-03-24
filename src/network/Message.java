package network;

import java.net.InetAddress;

/**
 * @author alex205
 */
public abstract class Message extends Packet {
    public Message(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination) {
        super(pseudoSource, pseudoDestination, addrSource, addrDestination);
    }
}
