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
            ViewController viewController = ViewController.getInstance();
            switch (n.getType()) {
                case ACK_CONNECT:
                    System.out.println(n.getPseudoSource() + " est également sur le réseau");
                    Contact c = new Contact(n.getPseudoSource(), n.getAddrSource());
                    if(!cc.contactExists(c)) {
                        System.out.println("contact ajouté dans la table");
                        cc.addContact(c);
                    }
                    break;

                case NUDGE:
                    System.out.println("J'ai reçu un wizz !");
                    viewController.updateView(viewController.getView(cc.getContact(n.getPseudoSource() + "@" + n.getAddrSource()), false), ViewController.Update_type.NEW_NUDGE, "");
                    break;

                    //devrait être un broadcast mais on utilise tcp pour transmettre le fichier image et on simule le broadcast
                case IMAGE_PERSO_CHANGED:
                    System.out.println("changement de l'image perso pour le contact");
                    Contact contact1 = cc.getContact(n.getPseudoSource() + "@" + n.getAddrSource().toString());
                    contact1.setImage_perso(n.getData());
                    if(viewController.viewExists(contact1)) {
                        viewController.updateView(viewController.getView(contact1, false), ViewController.Update_type.IMAGE_PERSO_CHANGE, "");
                    }
                    break;

            }
        }
    }




}
