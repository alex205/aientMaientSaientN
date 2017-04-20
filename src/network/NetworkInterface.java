package network;

import javafx.collections.ObservableList;
import model.Contact;
import model.ContactCollection;

import java.io.*;
import java.net.*;
import java.util.HashMap;

import static controller.Controller.readBytesFromFile;


/**
 * @author alex205
 */
public class NetworkInterface {
    private static int anoukPort = 20573;
    private static int bcastPort = 20574;
    public static int basePort = 20575;

    //Les sockets de base pour négocier sur quel port se fera la communication, sockets de rencontre
    private DatagramSocket anouk; //socket de rencontre
    private DatagramSocket hello; //sert à écouter les broadcast

    //Listener pour anouk
    private HelloListener helloListener;
    //Listener pour le bcast
    private BroadcastListener bcastListener;

    //Table de correspondance entre pseudo du contact et socket
    //private HashMap<String, Socket> socketMap;
    private HashMap<String, Integer> socketMap;

    //Table des sockets temporaires (utilisés pour le transfert d'image perso ou de fichier par exemple)
    private HashMap<String, Integer> tmpSocketMap;

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
        //Initialisation des tables vides
        socketMap = new HashMap<>();
        tmpSocketMap = new HashMap<>();
        //On met déjà le socket hello en écoute
       try {
           hello = new DatagramSocket(bcastPort);
           //Lancement du thread d'écoute pour broadcast
           bcastListener = new BroadcastListener(hello);
           //idem pour Anouk
           anouk = new DatagramSocket(anoukPort);
           helloListener = new HelloListener(anouk);
        } catch (IOException e) {
            System.out.println("Can't bind sockets");
            e.printStackTrace();
        }
    }

    private synchronized Integer negotiatePort(Contact dest, boolean tmp) {
        System.out.println("Je vais négocier le port (temporaire -> " + tmp + ")");
        try {
            System.out.println("anouk ok");
            ServerSocket com = new ServerSocket(basePort);
            System.out.println("socket com ok");
            CommunicationListener listener = new CommunicationListener(com);
            listener.start();
            System.out.println("listener com ok");
            if(tmp) {
                sendControl(ContactCollection.getMe(), dest, Control.Control_t.TMP_SOCKET, basePort);
            } else {
                sendControl(ContactCollection.getMe(), dest, Control.Control_t.HELLO, basePort);
            }
            basePort++;
            wait();
            System.out.println("ok up to date");
            System.out.println("full pseudo de recherche : " + dest.getFullPseudo());
            return (tmp ? tmpSocketMap.get(dest.getFullPseudo()) : socketMap.get(dest.getFullPseudo()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private Socket getSocket(Contact dest, boolean tmp) {
       Integer port = (tmp ? tmpSocketMap.get(dest.getFullPseudo()) : socketMap.get(dest.getFullPseudo()));
        if(port == null) {
            port = negotiatePort(dest, tmp);
        } else {
            System.out.println("pas besoin de négocier, déjà en mémoire"); //FIXME else pour test uniquement
        }
        System.out.println("Port négocié, tvb");
        try {
            return new Socket(dest.getIp(), port.intValue());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void launchNetwork() {
        bcastListener.start();
        helloListener.start();
    }
    //surcharge aussi
    public void sendNotification( Contact dest, Notification.Notification_type type) {
        sendNotification(dest, type, "");
    }

    public void sendNotification( Contact dest, Notification.Notification_type type, String data) {
        //On traite les notifications particulières
        if(type == Notification.Notification_type.IMAGE_PERSO_CHANGED) {
            //Image perso particulière car on simule un broadcast en tcp et on utilise un socket temporaire
            ContactCollection cc = ContactCollection.getInstance();
            //On récupère la liste de tous les contacts et on leur envoie la nouvelle image perso
            //On réalise l'opération sur un thread séparé pour éviter de bloquer le réseau
            ((Runnable) () -> {
                ObservableList<Contact> collection = cc.getCollection();
                Socket s = null;
                for(Contact c : collection) {
                    s = getSocket(c, true);
                    Notification n = new Notification(ContactCollection.getMe().getPseudo(), c.getPseudo(), ContactCollection.getMe().getIp(), c.getIp(), type, data);
                    try {
                        ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
                        os.writeObject(n);
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Thread.currentThread().interrupt();
            }).run();


        } else {
            Socket s = getSocket(dest, false);
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
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, NetworkUtils.getBroadcastAddress(), bcastPort);
            anouk.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void transmitMessage(String message, Contact dest) {
        Socket s = getSocket(dest, false);
        System.out.println("prêt à lancer le message");
        Text textMessage = new Text(ContactCollection.getMe().getPseudo(), dest.getPseudo(), ContactCollection.getMe().getIp(), dest.getIp(), message);
        try {
            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
            os.writeObject(textMessage);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void transmitFile(java.io.File file, Contact dest) throws IOException {
        Socket s = getSocket(dest, false);
        System.out.println("Plus qu'à s'occuper du fichier !!");
        byte [] content = readBytesFromFile(file);

        File filemessage = new File(ContactCollection.getMe().getPseudo(), dest.getPseudo(),  ContactCollection.getMe().getIp(), dest.getIp(), file.getName(), URLConnection.guessContentTypeFromName(file.getName()), file.length(), content);
        try {
            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
            os.writeObject(filemessage);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void transmitImagePerso(java.io.File file, Contact dest) throws IOException {
        Socket s = getSocket(dest, false);
        System.out.println("On s'occupe d'envoyer l'image perso !!");
        byte [] content = readBytesFromFile(file);

        File filemessage = new File(ContactCollection.getMe().getPseudo(), dest.getPseudo(),  ContactCollection.getMe().getIp(), dest.getIp(), file.getName(), URLConnection.guessContentTypeFromName(file.getName()), file.length(), content);
        try {
            ObjectOutputStream os = new ObjectOutputStream(s.getOutputStream());
            os.writeObject(filemessage);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    protected void sendControl(Contact me, Contact dest, Control.Control_t type, int data) {
        try {
            Control control_packet = new Control(me.getPseudo(), dest.getPseudo(), me.getIp(), dest.getIp(), type, data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream os = new ObjectOutputStream(outputStream);
            os.writeObject(control_packet);
            byte[] buffer = outputStream.toByteArray();
            DatagramPacket sendPacket = new DatagramPacket(buffer, buffer.length, dest.getIp(), anoukPort);
            anouk.send(sendPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addMap(String fullPseudo, int port) {
        socketMap.put(fullPseudo, port);
    }

    public void delMap(String fullPseudo) {
        socketMap.remove(fullPseudo);
    }

    public void addTmpMap(String fullPseudo, int port) {
        tmpSocketMap.put(fullPseudo, port);
    }

    public void delTmpMap(String fullPseudo) {
        tmpSocketMap.remove(fullPseudo);
    }

    public synchronized void fireUpdate() {
        notify();
    }




}
