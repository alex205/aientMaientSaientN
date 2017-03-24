package model;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ContactCollection {

    private static Contact me;

    private ArrayList<Contact> collection;

    public ContactCollection() {
        this.collection = new ArrayList<>();
    }

    public void addContact(Contact c) {
        this.collection.add(c);
    }

    public ArrayList<Contact> getCollection() {
        return this.collection;
    }

    public static void createMe(String pseudo) {
        try {
            me = new Contact(pseudo, InetAddress.getLocalHost());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static Contact getMe() {
        return me;
    }
}
