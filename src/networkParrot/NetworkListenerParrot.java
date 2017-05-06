package networkParrot;

import model.Packet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author toon
 */
abstract class NetworkListenerParrot extends Thread {
    protected ServerSocket server;
    private Socket s;

    public NetworkListenerParrot(ServerSocket server) {
        this.server = server;
    }

    public void run() {
        try {
            while(true) {
                s = server.accept();
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                Packet p = (Packet) is.readObject();
                managePacket(p);
            }
        } catch (EOFException e) {
            System.out.println("connection ended");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void closeConnection() {
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract void managePacket(Packet p);
}
