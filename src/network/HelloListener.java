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
            }

            else if(p instanceof Notification) {
                Notification n = (Notification) p;
                if(n.getAddrSource() != ContactCollection.getMe().getIp()) {
                    System.out.println("MDR g recu une notif de " + n.getPseudoSource());
                }
            }
        }
}
