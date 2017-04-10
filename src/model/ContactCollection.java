package model;

import network.NetworkUtils;


import java.util.HashMap;

public class ContactCollection {

    private static Contact me; //représente l'utilisateur courant

    private HashMap<String, Contact> collection;

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
        this.collection = new HashMap<>();
    }

    public void addContact(Contact c) {
        this.collection.put(c.getFullPseudo(), c);
    }

    public Contact getContact(String fullPseudo) {
        return collection.get(fullPseudo);
    }

    public boolean contactExists(Contact c) {
        return getContact(c.getFullPseudo()) != null;
    }

    public HashMap<String, Contact> getCollection() {
        return this.collection;
    }

    public static void createMe(String pseudo) {
            me = new Contact(pseudo, NetworkUtils.getMyIp());
    }

    public static Contact getMe() {
        return me;
    }
}
