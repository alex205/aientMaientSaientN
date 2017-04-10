package network;

import model.Contact;
import model.ContactCollection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.HashMap;


/**
 * @author alex205
 */
public class NetworkInterface {
    private static int helloPort = 20573;
    public static int basePort = 20574;

    //Les sockets de base pour négocier sur quel port se fera la communication
    private DatagramSocket anouk; //sert à parler
    private DatagramSocket hello; //sert à écouter

    //Listener pour hello
    private HelloListener helloListener;

    //Table de correspondance entre pseudo du contact et socket
    private HashMap<String, Socket> socketMap;

    // La network interface est un singleton parce qu'il faut en instancier qu'une seule !

    //Le holder
    private static class NetworkInterfaceHolder {
        private final static NetworkInterface instance = new NetworkInterface();
    }

    //pour récupérer l'instance
    public static NetworkInterface getInstance() {
        return NetworkInterfaceHolder.instance;
    }

    // Constructeur privé pour le singleton
    private NetworkInterface() {
        //Initialisation de la table vide
        socketMap = new HashMap<>();
        //On met déjà le socket hello en écoute
        try {
            hello = new DatagramSocket(helloPort);
            //Lancement du thread d'écoute pour hello
            helloListener = new HelloListener(hello);
            helloListener.start();
        } catch (IOException e) {
            System.out.println("Can't bind hello socket");
            e.printStackTrace();
        }

        //anouk est un datagram socket
        try {
            anouk = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private synchronized Socket negotiatePort(Contact dest) {
        System.out.println("Je vais négocier le port");
        try {
            System.out.println("anouk ok");
            ServerSocket com = new ServerSocket(basePort);
            CommunicationListener listener = new CommunicationListener(com);
            listener.start();
            sendControl(ContactCollection.getMe(), dest, Control.Control_t.HELLO, basePort);
            basePort++;
            wait();
            System.out.println("ok up to date");
            System.out.println("full pseudo de recherche : " + dest.getFullPseudo());
            return socketMap.get(dest.getFullPseudo());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Socket getSocket(Contact dest) {
       Socket s; //= socketMap.get(dest);
        //if(s == null) {
            s = negotiatePort(dest);
        //}
        System.out.println("Port négocié, tvb");
        return s;
    }

    //surcharge aussi
    public void sendNotification( Contact dest, Notification.Notification_type type) {
        sendNotification(dest, type, "");
    }

    public void sendNotification( Contact dest, Notification.Notification_type type, String data) {
        Socket s = getSocket(dest);
        System.out.println("Okay j'ai la socket pour balancer la sauce");
        Notification notification = new Notification(ContactCollection.getMe().getPseudo(), dest.getPseudo(), ContactCollection.getMe().getIp(), dest.getIp(), type, data);
        try {
            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
            os.writeObject(notification);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //surcharge s'il n'y a pas de data
    public void broadcastNotification(Notification.Notification_type type) {
        broadcastNotification(type, "");
    }

    public void broadcastNotification(Notification.Notification_type type, String data) {
        Notification notification = new Notification(ContactCollection.getMe().getPseudo(), "bcast", ContactCollection.getMe().getIp(), NetworkUtils.getBroadcastAddress(), type, data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(outputStream);
            os.writeObject(notification);
            byte[] buffer = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, NetworkUtils.getBroadcastAddress(), helloPort);
            anouk.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void transmitMessage(String message, Contact dest) {

    }

    public void transmitFile(String filename, Contact dest) {

    }

    protected void sendControl(Contact me, Contact dest, Control.Control_t type, int data) {
        try {
            Control control_packet = new Control(me.getPseudo(), dest.getPseudo(), me.getIp(), dest.getIp(), type, data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(control_packet);
            byte[] buffer = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, dest.getIp(), helloPort);
            anouk.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMap(String fullPseudo, Socket s) {
        socketMap.put(fullPseudo, s);
    }

    public synchronized void fireUpdate() {
        notify();
    }

}
