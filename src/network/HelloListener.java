package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
                System.out.println("l'ip de jlemme " + c.getAddrSource().toString());
                ni.addMap(c.getPseudoSource()+ "@" + c.getAddrSource().toString(), new Socket(c.getAddrSource(), c.getData()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            closeConnection();
            }
        }
}
