package model;

import java.util.ArrayList;

public class ContactCollection {

    public ArrayList<Contact> collection;

    public ContactCollection() {
        this.collection = new ArrayList<>();
    }

    public void addContact(Contact c) {
        this.collection.add(c);
    }

    public ArrayList<Contact> getCollection() {
        return this.collection;
    }
}
