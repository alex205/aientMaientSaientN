package network;

import model.Contact;
import model.ContactCollection;

import java.net.ServerSocket;

/**
 * @author alex205
 */
public class CommunicationListener extends NetworkListener{

    public CommunicationListener(ServerSocket server) {
        super(server);
    }

    @Override
    protected void managePacket(Packet p) {
        //Réception de message
        if(p instanceof Message) {
            //On regarde si c'est du texte ou un fichier
            if(p instanceof Text) {
                Text message = (Text) p;
                System.out.println("Message reçu : " + message.getData());
            }
        }
        if(p instanceof Notification) {
            ContactCollection cc = ContactCollection.getInstance();
            Notification n = (Notification) p;
            switch (n.getType()) {
                case ACK_CONNECT:
                    System.out.println(n.getPseudoSource() + " est également sur le réseau");
                    Contact c = new Contact(n.getPseudoSource(), n.getAddrSource());
                    if(!cc.contactExists(c)) {
                        System.out.println("contact ajouté dans la table");
                        cc.addContact(c);
                    }
                    break;
            }
        }
    }
}
