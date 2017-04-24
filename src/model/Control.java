package model;

import java.net.InetAddress;

/**
 * @author
 */
public class Control extends Packet {

    public enum Control_t {
        HELLO,
        ACK,
        TMP_SOCKET,
        TMP_SOCKET_ACK
    }

    private Control_t type;
    private int data;

    public Control(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination, Control_t type, int data) {
        super(pseudoSource, pseudoDestination, addrSource, addrDestination);
        this.type = type;
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public Control_t getType() {
        return type;
    }
}
