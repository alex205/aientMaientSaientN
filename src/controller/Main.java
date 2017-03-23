package controller;

import model.Contact;
import network.NetworkInterface;
import network.Notification;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {

    public static void main(String[] args) {
        /*NetworkInterface ni = NetworkInterface.getInstance();
        try {
            Contact c = new Contact("alex", InetAddress.getByName("localhost"));
            ni.sendNotification(c, Notification.Notification_type.ACK, "bite");
            ni.sendNotification(c, Notification.Notification_type.ACK, "bite");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }*/

        //Lancement interface graphique
        gui.Main.launch(gui.Main.class);
    }
}
