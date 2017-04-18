package controller;


import gui.ChatWindow;
import gui.ViewController;
import model.Contact;
import model.ContactCollection;
import network.NetworkInterface;
import network.Notification;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Controller {

    public enum App_State_t {
        CONNECTED,
        DISCONNECTED
    }

    private NetworkInterface ni;
    private App_State_t state;

    public Controller() {
        state = App_State_t.DISCONNECTED;
        ni = NetworkInterface.getInstance();
    }

    public void connect(String pseudo) {
        state = App_State_t.CONNECTED;
        ContactCollection.createMe(pseudo);
        ni.launchNetwork();
        ni.broadcastNotification(Notification.Notification_type.CONNECT);
    }

    public void sendMessage(Contact dest, String message) {
        System.out.println("Envoi d'un message");
        ni.transmitMessage(message, dest);
    }
    public void sendFile(Contact dest, File file) throws IOException {
        System.out.println("Envoi d'un fichier");
        ni.transmitFile(file, dest);
    }

    public void sendNudge(Contact dest, Notification.Notification_type notification) throws IOException {
        System.out.println("Envoi d'un wizz");
        ni.sendNotification(dest, notification);
    }

    public void changeTextColor(Contact dest, String color) {
        ni.sendNotification(dest, Notification.Notification_type.TEXT_COLOR_CHANGE, color);
    }

    public void changeStatus(String status) {
        Contact.Status_t st = null;
        switch (status) {
            case "Disponible":
                st = Contact.Status_t.ONLINE;
                break;
            case "Absent":
                st = Contact.Status_t.AWAY;
                break;
            case "Occupé":
                st = Contact.Status_t.BUSY;
                break;
            case "Hors-ligne":
                st = Contact.Status_t.OFFLINE;
                break;
        }
        ContactCollection.getMe().setStatus(st);
        HashMap<String, ChatWindow> map = ViewController.getInstance().getAllViews();
        for(Map.Entry<String, ChatWindow> entry : map.entrySet()) {
            ChatWindow view = entry.getValue();

            view.getChatWindowController().refreshStatus(true);
        }
        System.out.println("envoi du status change");
        ni.broadcastNotification(Notification.Notification_type.STATUS_CHANGE, status);
    }

    public void disconnect() {
        ni.broadcastNotification(Notification.Notification_type.DISCONNECT);
    }

    public App_State_t getState() {
        return state;
    }
}
