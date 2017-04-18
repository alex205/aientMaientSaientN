package network;

import gui.ChatWindow;
import gui.ViewController;
import model.Contact;
import model.ContactCollection;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
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
        ContactCollection cc = ContactCollection.getInstance();
        //Réception de message
        if(p instanceof Message) {
            ViewController viewController = ViewController.getInstance();
            //On regarde si c'est du texte ou un fichier
            if(p instanceof Text) {
                Text message = (Text) p;
                ChatWindow view = viewController.getView(cc.getContact(message.getPseudoSource() + "@" + message.getAddrSource()), false);
                viewController.updateView(view, ViewController.Update_type.NEW_MESSAGE, message.getData());
            }
            if(p instanceof File) {
                File file = (File) p;

                try (FileOutputStream fileOuputStream = new FileOutputStream(file.getFileName())) {
                    fileOuputStream.write(file.getContent());
                    System.out.println(file.getFileName() + " a bien été reçu !!");
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
        if(p instanceof Notification) {
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

                case TEXT_COLOR_CHANGE:
                    System.out.println("changement de couleur pour le contact");
                    cc.getContact(n.getPseudoSource() + "@" + n.getAddrSource().toString()).setTextColor(n.getData());
                    break;
                case NUDGE:
                    ViewController viewController = ViewController.getInstance();
                    System.out.println("J'ai reçu un wizz !");
                    ChatWindow view = viewController.getView(cc.getContact(n.getPseudoSource() + "@" + n.getAddrSource()), false);
                    view.getChatWindowController().shakeStage();
            }
        }
    }




}
