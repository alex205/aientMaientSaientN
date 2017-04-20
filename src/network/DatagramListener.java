package network;

import model.ContactCollection;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.security.spec.ECField;

/**
 * @author alex205
 */
public abstract class DatagramListener extends Thread {

    private DatagramSocket socket;

    public DatagramListener(DatagramSocket socket) {
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
                        //if(!p.getAddrSource().equals(ContactCollection.getMe().getIp())) {
                        managePacket(p);
                        //}


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void managePacket(Packet p) throws IOException;
}
