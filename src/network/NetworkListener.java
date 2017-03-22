package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author alex205
 */
class NetworkListener extends Thread {
    private ServerSocket server;

    public NetworkListener(ServerSocket server) {
        this.server = server;
    }

    public void run() {
        try {
            Socket s = server.accept();
            while(true) {
                System.out.println("OKAY SOCKET");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
