package network;

import model.Contact;
import model.ContactCollection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;

import static network.NetworkInterface.basePort;

/**
 * @author alex205
 */
public class HelloListener extends DatagramListener {

    public HelloListener(DatagramSocket socket) {
        super(socket);
    }

    @Override
    protected void managePacket(Packet p) {
        NetworkInterface ni = NetworkInterface.getInstance();
        ContactCollection cc = ContactCollection.getInstance();
        if(p instanceof Control) {
            Control c = (Control) p;
            System.out.println("RECEIVED, le port :" + c.getData());
            try {
                if(c.getType() == Control.Control_t.HELLO || c.getType() == Control.Control_t.ACK) {
                    //Gestion du socket de conversation
                    System.out.println("l'ip de " + c.getPseudoSource() + " " + c.getAddrSource().toString());
                    ni.addMap(c.getPseudoSource() + "@" + c.getAddrSource().toString(), c.getData());
                    System.out.println("le pseudo dans la table " + c.getPseudoSource() + "@" + c.getAddrSource().toString());
                    ni.fireUpdate();
                    if (c.getType() == Control.Control_t.HELLO) {
                        ServerSocket com = new ServerSocket(basePort);
                        CommunicationListener listener = new CommunicationListener(com);
                        listener.start();
                        ni.addListener(basePort, listener);
                        System.out.println("listener lancé");
                        ni.sendControl(ContactCollection.getMe(), new Contact(c.getPseudoSource(), c.getAddrSource()), Control.Control_t.ACK, basePort);
                        basePort++;
                    }
                } else if(c.getType() == Control.Control_t.TMP_SOCKET || c.getType() == Control.Control_t.TMP_SOCKET_ACK) {
                    //Gestion de la demande de socket temporaire
                    System.out.println("on demande un socket temporaire");
                    ni.addTmpMap(c.getPseudoSource() + "@" + c.getAddrSource().toString(), c.getData());
                    ni.fireUpdate();
                    System.out.println("c'est à jour");
                    if(c.getType() == Control.Control_t.TMP_SOCKET) {
                        ServerSocket com = new ServerSocket(basePort);
                        CommunicationListener listener = new CommunicationListener(com);
                        listener.start();
                        ni.addListener(basePort, listener);
                        ni.sendControl(ContactCollection.getMe(), cc.getContact(c.getPseudoSource() + "@" + c.getAddrSource()), Control.Control_t.TMP_SOCKET_ACK, basePort);
                        basePort++;
                    }
                }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
