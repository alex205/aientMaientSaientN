package network;

import gui.ChatWindow;
import gui.ViewController;
import model.Contact;
import model.ContactCollection;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
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
            ViewController viewController = ViewController.getInstance();
            //On regarde si c'est du texte ou un fichier
            if(p instanceof Text) {
                Text message = (Text) p;
                ChatWindow view = viewController.getView(new Contact(message.getPseudoSource(), message.getAddrSource()), false);
                viewController.updateView(view, ViewController.Update_type.NEW_MESSAGE, message.getData());
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

                case MISC:
                    //Désérialisation du misc
                    try {
                        byte b[] = n.getData().getBytes();
                        ByteArrayInputStream bi = new ByteArrayInputStream(b);
                        ObjectInputStream si = new ObjectInputStream(bi);
                        Misc misc = (Misc) si.readObject();
                        switch (misc.getType()) {
                            case TEXT_COLOR_CHANGE:
                                System.out.println("reçu un changement de couleur de texte --> " + misc.getData());
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
            }
        }
    }
}
