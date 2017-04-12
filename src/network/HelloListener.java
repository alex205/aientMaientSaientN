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
        if(p instanceof Control) {
            Control c = (Control) p;
            System.out.println("RECEIVED, le port :" + c.getData());
            try {
                    System.out.println("l'ip de " + c.getPseudoSource() + " " + c.getAddrSource().toString());
                    ni.addMap(c.getPseudoSource() + "@" + c.getAddrSource().toString(), c.getData());
                    System.out.println("le pseudo dans la table " + c.getPseudoSource() + "@" + c.getAddrSource().toString());
                    ni.fireUpdate();
                    if(c.getType() == Control.Control_t.HELLO) {
                        ServerSocket com = new ServerSocket(basePort);
                        CommunicationListener listener = new CommunicationListener(com);
                        listener.start();
                        System.out.println("listener lancé");
                        ni.sendControl(ContactCollection.getMe(), new Contact(c.getPseudoSource(), c.getAddrSource()), Control.Control_t.ACK, basePort);
                        basePort++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
