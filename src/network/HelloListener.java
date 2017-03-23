package network;

import java.net.ServerSocket;

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
            closeConnection();
            }
        }
}
