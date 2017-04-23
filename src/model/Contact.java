package model;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Base64;

import static controller.Controller.readBytesFromFile;


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
    private SimpleObjectProperty status;
    private String text_color;
    private String image_perso;




    public Contact(String pseudo, InetAddress ip) {
        this.pseudo = pseudo;
        this.ip = ip;
        this.status = new SimpleObjectProperty(Status_t.ONLINE);
        this.text_color = "000000";

        File file = new File("src/resources/images/default.png");
        try{
            this.image_perso = Base64.getEncoder().encodeToString(readBytesFromFile(file));
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    public static Callback<Contact, Observable[]> extractor() {
        return param -> new Observable[]{param.status};
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
        return (Status_t) status.get();
    }

    public void setStatus(Status_t s) {
        status.set(s);
    }

    public void setImage_perso(String image_perso) { this.image_perso = image_perso; }

    public String getImage_perso() { return image_perso; }
}
