package network;

import java.net.InetAddress;

/**
 * @author
 */
public class Control extends Packet {

    private int data;

    public Control(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination, int data) {
        super(pseudoSource, pseudoDestination, addrSource, addrDestination);
        this.data = data;
    }

    public int getData() {
        return data;
    }
}
