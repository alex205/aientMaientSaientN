package network;

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
                            break;
                        case STATUS_CHANGE:
                            System.out.println(n.getPseudoSource() + " est maintenant " + n.getData());
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
