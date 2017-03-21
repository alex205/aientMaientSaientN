package network;

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
        MISC
    }

    private Notification_type type;
    private String data;

    public Notification(Notification_type type, String data) {
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
