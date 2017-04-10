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
public class HelloListener extends Thread {

    private DatagramSocket socket;

    public HelloListener(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {
        byte[] incomingData = new byte[1024];
        while(true) {
            DatagramPacket incomingPacket = new DatagramPacket(incomingData, incomingData.length);
            try {
                socket.receive(incomingPacket);
                byte[] data = incomingPacket.getData();
                ByteArrayInputStream in = new ByteArrayInputStream(data);
                ObjectInputStream is = new ObjectInputStream(in);
                try {
                    Packet p = (Packet) is.readObject();
                    managePacket(p);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    protected void managePacket(Packet p) {
        NetworkInterface ni = NetworkInterface.getInstance();
        if(p instanceof Control) {
            Control c = (Control) p;
            System.out.println("RECEIVED, le port :" + c.getData());
            try {
                    System.out.println("l'ip de " + c.getPseudoSource() + " " + c.getAddrSource().toString());
                    ni.addMap(c.getPseudoSource() + "@" + c.getAddrSource().toString(), new Socket(c.getAddrSource(), c.getData()));
                    System.out.println("le pseudo dans la table " + c.getPseudoSource() + "@" + c.getAddrSource().toString());
                    ni.fireUpdate();
                    if(c.getType() == Control.Control_t.HELLO) {
                        ServerSocket com = new ServerSocket(basePort);
                        CommunicationListener listener = new CommunicationListener(com);
                        listener.start();
                        ni.sendControl(ContactCollection.getMe(), new Contact(c.getPseudoSource(), c.getAddrSource()), Control.Control_t.ACK, basePort);
                        basePort++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else if(p instanceof Notification) {
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
