package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
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
            System.out.println("waiting connection");
            Socket s = server.accept();
            System.out.println("client connecté");
            while(true) {
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                Packet p = (Packet) is.readObject();
                Control c = (Control) p;
                System.out.println("Le port de l'autre "  + c.getData());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
