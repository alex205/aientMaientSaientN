package network;

import gui.ViewController;
import model.Contact;
import model.ContactCollection;

import java.net.DatagramSocket;

/**
 * @author alex205
 */
public class BroadcastListener extends DatagramListener{

    public BroadcastListener(DatagramSocket socket) {
        super(socket);
    }

    @Override
    protected void managePacket(Packet p) {
        NetworkInterface ni = NetworkInterface.getInstance();
        ViewController viewController = ViewController.getInstance();
        if(p instanceof Notification) {
            Notification n = (Notification) p;
                ContactCollection cc = ContactCollection.getInstance();
                if(!n.getAddrSource().equals(ContactCollection.getMe().getIp())) {
                    switch (n.getType()) {
                        case CONNECT:
                            System.out.println(n.getPseudoSource() + " vient de se connecter");
                            Contact c = new Contact(n.getPseudoSource(), n.getAddrSource());
                            if(!cc.contactExists(c)) {
                                System.out.println("contact ajouté dans la table");
                                cc.addContact(c);
                            }
                            System.out.println("on répond de notre présence à " + c.getFullPseudo());
                            ni.sendNotification(c, Notification.Notification_type.ACK_CONNECT);
                            break;
                        case DISCONNECT:
                            System.out.println(n.getPseudoSource() + " vient de se déconnecter");
                            cc.delContact(cc.getContact(n.getPseudoSource() + "@" + n.getAddrSource().toString()));
                            break;
                        case STATUS_CHANGE:
                            System.out.println(n.getPseudoSource() + " est maintenant " + n.getData());
                            Contact.Status_t status = Contact.Status_t.ONLINE; //valeur par défaut au cas où
                            //un peu sale mais efficace ! (pour envoyer l'enum dans le paquet en string)
                            switch (n.getData()) {
                                case "Disponible":
                                    status = Contact.Status_t.ONLINE;
                                    break;
                                case "Absent":
                                    status = Contact.Status_t.AWAY;
                                    break;
                                case "Occupé":
                                    status = Contact.Status_t.BUSY;
                                    break;
                                case "Hors-ligne":
                                    status = Contact.Status_t.OFFLINE;
                                    break;
                            }
                            Contact contact = cc.getContact(n.getPseudoSource() + "@" + n.getAddrSource().toString());
                            contact.setStatus(status); // changement du statut dans le contact
                            //changement du statut dans la vue si elle est ouverte
                            if(viewController.viewExists(contact)) {
                                viewController.updateView(viewController.getView(contact, false), ViewController.Update_type.STATUS_CHANGE, "");
                            }
                            break;
                        case ALIVE:
                            System.out.println(n.getPseudoSource() + " est en vie !!");
                            break;

                        default:
                            System.out.println("Can't read this notification");
                    }
                }
        }
    }
}
