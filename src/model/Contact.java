package model;

import java.net.InetAddress;

public class Contact {

    public enum Status_t {
        ONLINE,
        AWAY,
        BUSY,
        OFFLINE
    }

    private String pseudo;
    private InetAddress ip;
    private Status_t status;


    public Contact(String pseudo, InetAddress ip) {
        this.pseudo = pseudo;
        this.ip = ip;
        this.status = Status_t.ONLINE;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void changeStatus(Status_t status) {
        this.status = status;
    }
}
