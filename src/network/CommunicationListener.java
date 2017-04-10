package network;

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
        if(p instanceof Notification) {
            Notification n = (Notification) p;
            switch (n.getType()) {
                case ACK_CONNECT:
                    System.out.println(n.getPseudoSource() + " est également sur le réseau");
                    break;
            }
        }
    }
}
