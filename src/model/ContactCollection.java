package model;

import network.NetworkUtils;

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
            me = new Contact(pseudo, NetworkUtils.getMyIp());
    }

    public static Contact getMe() {
        return me;
    }
}
