package model;

import network.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class ContactCollection {

    private static Contact me; //représente l'utilisateur courant

    private ArrayList<Contact> collection;
    private Consumer<Contact> addCallback = contact -> {};

    // La contact collection est un singleton parce qu'il faut en instancier qu'une seule !

    //Le holder
    private static class ContactCollectionHolder {
        private final static ContactCollection instance = new ContactCollection();
    }

    //pour récupérer l'instance
    public static ContactCollection getInstance() {
        return ContactCollectionHolder.instance;
    }

    private ContactCollection() {
        this.collection = new ArrayList<>();
    }

    public void setAddCallback(Consumer<Contact> addCallback) {
        this.addCallback = addCallback ;
    }

    public void addContact(Contact c) {
        if(!contactExists(c)) {
            System.out.println("ajout contact " + c.getPseudo());
            addCallback.accept(c);
            collection.add(c);
        }
    }

    /*public Contact getContact(String fullPseudo) {
        return collection.get(fullPseudo);
    }*/

    public boolean contactExists(Contact c) {
        for (Contact object : collection) {
            if (object.getFullPseudo().equals(c.getFullPseudo())) {
                return true;
            }
        }
        return false;
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
