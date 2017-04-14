package model;

import java.net.InetAddress;

public class Contact {

    public enum Status_t {
        ONLINE ("Disponible"),
        AWAY ("Absent"),
        BUSY ("Occupé"),
        OFFLINE ("Hors-ligne");

        private String status_name;
        Status_t(String status_name) {
            this.status_name = status_name;
        }

        @Override
        public String toString() {
            return status_name;
        }
    }

    private String pseudo;
    private InetAddress ip;
    private Status_t status;
    private String text_color;


    public Contact(String pseudo, InetAddress ip) {
        this.pseudo = pseudo;
        this.ip = ip;
        this.status = Status_t.ONLINE;
        this.text_color = "000000";
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getFullPseudo() {
        return pseudo + "@" + ip.toString();
    }

    public InetAddress getIp() {
        return ip;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getTextColor() {
        return text_color;
    }

    public void setTextColor(String text_color) {
        this.text_color = text_color;
    }

    public Status_t getStatus() {
        return status;
    }

    public void setStatus(Status_t status) {
        this.status = status;
    }
}
