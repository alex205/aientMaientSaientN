package model;

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
        MESSAGE_PERSO_CHANGE,
        NUDGE,
        IMAGE_PERSO_CHANGED
    }

    private Notification_type type;
    private String data;

    public Notification(String pseudoSource, String pseudoDestination, InetAddress addrSource, InetAddress addrDestination, Notification_type type, String data) {
        super(pseudoSource, pseudoDestination, addrSource, addrDestination);
        this.type = type;
        this.data = data;
    }

    public Notification_type getType() {
        return this.type;
    }

    public String getData() {
        return this.data;
    }

    public String toString() {
        switch (this.type) {
            case CONNECT :
                return this.getPseudoSource() + " s'est connecté.";
            case DISCONNECT :
                return this.getPseudoSource() + " s'est déconnecté.";
            case STATUS_CHANGE :
                switch (this.data) {
                    case "ONLINE" :
                        return this.getPseudoSource() + " est passé \"en ligne\".";
                    case "BUSY" :
                        return this.getPseudoSource() + " est passé \"occupé\".";
                    case "AWAY" :
                        return this.getPseudoSource() + " est passé \"absent\".";
                    case "OFFLINE" :
                        return this.getPseudoSource() + " est passé \"hors ligne\".";
                }
                break;
        }
        return "Notification inconnue";
    }
}