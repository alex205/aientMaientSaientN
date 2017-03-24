package controller;

import model.Contact;
import model.ContactCollection;

import network.NetworkUtils;
import network.Notification;

import java.net.*;
import java.util.Enumeration;
import network.NetworkInterface;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        ContactCollection.createMe("alex205");
        NetworkInterface ni = NetworkInterface.getInstance();
        try {
            Contact c = new Contact("alex", InetAddress.getByName("10.32.2.53"));
            //ni.sendNotification(c, Notification.Notification_type.ACK, "bite");
            //ni.sendNotification(c, Notification.Notification_type.ACK, "bite");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


        //Lancement interface graphique
        //gui.Main.launch(gui.Main.class);
    }
}

