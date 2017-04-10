package controller;

import model.Contact;
import model.ContactCollection;

import java.net.*;

import network.NetworkInterface;
import network.Notification;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        ContactCollection.createMe("alex205");
        NetworkInterface ni = NetworkInterface.getInstance();
        try {
            Contact c = new Contact("alex205", InetAddress.getByName("192.168.1.91"));
            ni.sendNotification(c, Notification.Notification_type.ACK, "bite");
            ni.sendNotification(c, Notification.Notification_type.ACK, "bite");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        //Lancement interface graphique
        //gui.Main.launch(gui.Main.class);
    }
}

