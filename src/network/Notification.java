package network;

import java.net.InetAddress;

/**
 * @author alex205
 */
public final class Notification extends Packet {
    public enum Notification_type {
        CONNECT,
        DISCONNECT,
        STATUS_CHANGE,
        ACK,
        ACK_CONNECT,
        ALIVE,
        TEXT_COLOR_CHANGE,
        MESSAGE_PERSO_CHANGE
    }

    private Notification_type type;
    private String data;

    public Notification(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination, Notification_type type, String data) {
        super(pseudoSource, pseudoDestination, addrSource, addrDestination);
        this.type = type;
        this.data = data;
    }

    public Notification_type getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
