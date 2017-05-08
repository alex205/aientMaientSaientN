package Test.networkParrot;

import gui.ChatWindow;
import gui.ViewController;
import model.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toon
 */
public class CommunicationListenerParrot extends NetworkListenerParrot{

    public CommunicationListenerParrot(ServerSocket server) {
        super(server);
    }


    @Override
    protected void managePacket(Packet p) {
        ContactCollection cc = ContactCollection.getInstance();
        NetworkInterfaceParrot ni = NetworkInterfaceParrot.getInstance();
        //Réception de message
        if(p instanceof Message) {
            ViewController viewController = ViewController.getInstance();
            //On regarde si c'est du texte ou un fichier
            if(p instanceof Text) {
                Text message = (Text) p;
                ChatWindow view = viewController.getView(cc.getContact(message.getPseudoSource() + "@" + message.getAddrSource()), false);
                viewController.updateView(view, ViewController.Update_type.NEW_MESSAGE, message.getData());
                // RENVOIE !!
                ni.transmitMessage(message.getData(), cc.getContact(p.getPseudoSource()+"@"+p.getAddrSource()));
                viewController.updateView(view, ViewController.Update_type.NEW_MESSAGE_ME, message.getData());
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
                    //PERROQUET
                    ni.sendNotification(cc.getContact(n.getPseudoSource() + "@" + n.getAddrSource()), Notification.Notification_type.NUDGE);
                    break;

                //devrait être un broadcast mais on utilise tcp pour transmettre le fichier image et on simule le broadcast
                case IMAGE_PERSO_CHANGED:
                    System.out.println("changement de l'image perso pour le contact");
                    Contact contact1 = cc.getContact(n.getPseudoSource() + "@" + n.getAddrSource().toString());
                    contact1.setImage_perso(n.getData());
                    if(viewController.viewExists(contact1)) {
                        viewController.updateView(viewController.getView(contact1, false), ViewController.Update_type.IMAGE_PERSO_CHANGE, "");
                    }

                    //fin d'utilisation du socket temporaire
                    ni.delTmpMap(n.getPseudoSource() + "@" + n.getAddrSource().toString());
                    ni.delListener(ni.getListener(server.getLocalPort()));


                    //PERROQUET
                    ContactCollection.getMe().setImage_perso(n.getData());
                    HashMap<String, ChatWindow> map = ViewController.getInstance().getAllViews();
                    for(Map.Entry<String, ChatWindow> entry : map.entrySet()) {
                        ChatWindow view = entry.getValue();

                        try {
                            view.getChatWindowController().changeImagePerso(true);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("envoi image");
                    ni.sendNotification(null, Notification.Notification_type.IMAGE_PERSO_CHANGED, n.getData());
                    break;

            }
        }
    }
}
