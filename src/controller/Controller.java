package controller;

import model.ContactCollection;
import network.NetworkInterface;
import network.Notification;

public class Controller {

    public Controller() {

    }

    public void connect(String pseudo) {
        NetworkInterface ni = NetworkInterface.getInstance();
        ContactCollection.createMe(pseudo);

        ni.broadcastNotification(Notification.Notification_type.CONNECT);
    }
}
