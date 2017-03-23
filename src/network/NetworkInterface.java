package network;

import model.Contact;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;


/**
 * @author alex205
 */
public class NetworkInterface {
    private static int helloPort = 20573;
    private static int basePort = 20574;

    //Les sockets de base pour négocier sur quel port se fera la communication
    private Socket anouk; //sert à parler
    private ServerSocket hello; //sert à écouter

    //Listener pour hello
    private NetworkListener helloListener;

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
            hello = new ServerSocket(helloPort);
            //Lancement du thread d'écoute pour hello
            helloListener = new HelloListener(hello);
            helloListener.start();
        } catch (IOException e) {
            System.out.println("Can't bind hello socket");
            e.printStackTrace();
        }
    }

    private void negotiatePort(Contact dest) {
        System.out.println("Je vais négotier le port");
        try {
            anouk = new Socket(dest.getIp(), helloPort);
            System.out.println("anouk ok");
            Control control_packet = new Control(basePort);
            ObjectOutputStream os = new ObjectOutputStream(anouk.getOutputStream());
            os.writeObject(control_packet);
            os.close();
            basePort++;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Socket getSocket(Contact dest) {
        Socket s = socketMap.get(dest);
        if(s == null) {
            negotiatePort(dest);
        }

        return s;
    }

    public void sendNotification(Contact dest, Notification.Notification_type type, String data) {
        Socket s = getSocket(dest);
    }

    public void broadcastNotification(Notification.Notification_type type, String data) {

    }

    public void transmitMessage(String message, Contact dest) {

    }

    public void transmitFile(String filename, Contact dest) {

    }

}
