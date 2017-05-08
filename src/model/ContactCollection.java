package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import network.NetworkUtils;

import java.util.function.Consumer;

/**
 * Internal representation of the contacts
 * The Contact view observes the list to perform updates
 *
 * @author alex205
 * @author toon
 */
public class ContactCollection {

    private static Contact me; //représente l'utilisateur courant

    private ObservableList<Contact> collection;
    private Consumer<Contact> addCallback = contact -> {};
    private Consumer<Contact> delCallback = contact -> {};

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
        this.collection = FXCollections.observableArrayList(Contact.extractor());
    }

    public void setAddCallback(Consumer<Contact> addCallback) {
        this.addCallback = addCallback ;
    }

    public void setDelCallback(Consumer<Contact> delCallback) {
        this.delCallback = delCallback ;
    }

    public void addContact(Contact c) {
        if(!contactExists(c)) {
            System.out.println("ajout contact " + c.getPseudo());
            collection.add(c);
        }
    }

    public void delContact(Contact c) {
        if(contactExists(c)) {
            System.out.println("suppression de " + c.getPseudo());
            delCallback.accept(c);
            collection.remove(c);
            System.out.println("okay supprimé");
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

    public Contact getContact(String fullPseudo) {
        for (Contact object : collection) {
            if (object.getFullPseudo().equals(fullPseudo)) {
                return object;
            }
        }
        return null;
    }

    public ObservableList<Contact> getCollection() {
        return this.collection;
    }

    public static void createMe(String pseudo) {
            me = new Contact(pseudo, NetworkUtils.getMyIp());
    }

    public static Contact getMe() {
        return me;
    }
}
