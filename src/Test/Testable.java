package Test;

import model.Contact;
import model.File;
import model.Notification.Notification_type;


/**
 *
 * @author Timothee Ficat et Noemy Artigouha
 *
 */
public interface Testable {

    /**
     * Lance la connexion de l'utilisateur courant
     */
    public void connect();

    /**
     * Deconnecter l'utilisateur courant
     */
    public void disconnect();

    /**
     * Envoie un messsage de type texte
     * @param string - Le message a envoyer
     * @param contact - Le contact destination
     */
    public void sendMessageText(String string, Contact contact);

    /**
     * Envoie un message de type fichier
     * @param file - Le fichier a envoyer
     * @param contact - Le contact destination
     */
    public void sendMessageFile(File file, Contact contact);

    /**
     * Change le statut de l'utilisateur courant
     * @param type - Le type de notification a envoyer aux autres utilisateurs
     * @param status - Le nouveau statut
     */
    public void changeStatus(Notification_type type, Contact.Status_t status);

}