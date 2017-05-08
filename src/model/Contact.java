package model;

import javafx.beans.Observable;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.util.Callback;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.InetAddress;
import java.util.Base64;


/**
 * Represents a contact in the chat system
 *
 * @author alex205
 * @author toon
 */
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
    private SimpleStringProperty message_perso;




    public Contact(String pseudo, InetAddress ip) {
        this.pseudo = pseudo;
        this.ip = ip;
        this.status = new SimpleObjectProperty(Status_t.ONLINE);
        this.text_color = "000000";
        this.message_perso = new SimpleStringProperty("");

        try {
            InputStream is = getClass().getResourceAsStream("/resources/images/default.png");
            this.image_perso = Base64.getEncoder().encodeToString(IOUtils.toByteArray(is));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Callback for update the ObservableArrayList in the view
     * Sets the parameters to be observed
     * @param Callback<Contact, Observable[] >
     * @return Observable[]
     *
     * @see javafx.collections.ObservableList
     */
    public static Callback<Contact, Observable[]> extractor() {
        return param -> new Observable[]{param.status, param.message_perso};
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

    public String getMessage_perso() {
        return message_perso.getValue();
    }

    public void setMessage_perso(String message_perso) {
        this.message_perso.set(message_perso);
    }
}
