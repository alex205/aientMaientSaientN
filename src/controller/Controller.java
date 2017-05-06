package controller;


import gui.ChatWindow;
import gui.ViewController;
import model.*;
import network.NetworkInterface;
import networkParrot.NetworkInterfaceParrot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class Controller implements Observer {

    @Override
    public void update(Observable o) {
        newUserRoutine();
    }

    public enum App_State_t {
        CONNECTED,
        DISCONNECTED
    }

    private NetworkInterfaceParrot ni;
    //private NetworkInterface ni;
    private App_State_t state;

    public Controller() {
        state = App_State_t.DISCONNECTED;
        //ni = NetworkInterface.getInstance();
        ni = NetworkInterfaceParrot.getInstance();
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

    public void changeTextColor(String color) {
        ni.broadcastNotification(Notification.Notification_type.TEXT_COLOR_CHANGE, color);
    }

    public void changeImagePerso(String image){
        System.out.println("Envoi d'un changement d'image");
        ContactCollection.getMe().setImage_perso(image);
        HashMap<String, ChatWindow> map = ViewController.getInstance().getAllViews();
        for(Map.Entry<String, ChatWindow> entry : map.entrySet()) {
            ChatWindow view = entry.getValue();

            try {
                view.getChatWindowController().changeImagePerso(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("envoi image");
        ni.sendNotification(null, Notification.Notification_type.IMAGE_PERSO_CHANGED, image);
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

    public void changeMessagePerso(String message) {
        System.out.println("envoi changement message perso");
        ni.broadcastNotification(Notification.Notification_type.MESSAGE_PERSO_CHANGE, message);
    }

    public void newUserRoutine() {
        changeStatus(ContactCollection.getMe().getStatus().toString());
        changeImagePerso(ContactCollection.getMe().getImage_perso());
        changeMessagePerso(ContactCollection.getMe().getMessage_perso());
        changeTextColor(ContactCollection.getMe().getTextColor());
    }

    public void disconnect() {
        ni.broadcastNotification(Notification.Notification_type.DISCONNECT);
    }

    public App_State_t getState() {
        return state;
    }


    public static byte[] readBytesFromFile(java.io.File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            throw new IOException("Could not completely read file " + file.getName() + " as it is too long (" + length + " bytes, max supported " + Integer.MAX_VALUE + ")");
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int)length];

        // Read in the bytes
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }

        // Ensure all the bytes have been read in
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }


}
