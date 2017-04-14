package controller;


import model.Contact;
import model.ContactCollection;
import network.NetworkInterface;
import network.Notification;


public class Controller {

    private NetworkInterface ni;

    public Controller() {
        ni = NetworkInterface.getInstance();
    }

    public void connect(String pseudo) {
        ContactCollection.createMe(pseudo);
        ni.launchNetwork();
        ni.broadcastNotification(Notification.Notification_type.CONNECT);
    }

    public void sendMessage(Contact dest, String message) {
        System.out.println("Envoi d'un message");
        ni.transmitMessage(message, dest);
    }

    public void changeTextColor(Contact dest, String color) {
        ni.sendNotification(dest, Notification.Notification_type.TEXT_COLOR_CHANGE, color);
    }
}
