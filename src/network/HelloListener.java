package network;

import model.Contact;
import model.ContactCollection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static network.NetworkInterface.basePort;

/**
 * @author alex205
 */
public class HelloListener extends NetworkListener{

    public HelloListener(ServerSocket server) {
        super(server);
    }

    @Override
    protected void managePacket(Packet p) {
        if(p instanceof Control) {
            Control c = (Control) p;
            System.out.println("RECEIVED, le port :" + c.getData());
            NetworkInterface ni = NetworkInterface.getInstance();
            try {
                    System.out.println("l'ip de " + c.getPseudoSource() + " " + c.getAddrSource().toString());
                    ni.addMap(c.getPseudoSource() + "@" + c.getAddrSource().toString(), new Socket(c.getAddrSource(), c.getData()));
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

            closeConnection();
            }

            else if(p instanceof Notification) {
                System.out.println("MDR g recu une notif");
            }
        }
}
