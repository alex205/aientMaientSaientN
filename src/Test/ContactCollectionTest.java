package Test;

import model.Contact;
import model.ContactCollection;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;

import static org.junit.Assert.*;

/**
 * Created by toon on 24/04/17.
 */
public class ContactCollectionTest {

    ContactCollection collection;

    @Test
    public void getInstance() throws Exception {
        collection = ContactCollection.getInstance();
        assert collection != null;
    }

    @Test
    public void addContact() throws Exception {
        collection = ContactCollection.getInstance();
        Contact test = new Contact("MSN", InetAddress.getByName("127.0.0.1"));
        collection.addContact(test);

        assert (collection.getCollection().get(0)== test);
    }

    @Test
    public void delContact() throws Exception {
        collection = ContactCollection.getInstance();
        Contact test = new Contact("MSN", InetAddress.getByName("127.0.0.1"));
        collection.addContact(test);
        collection.delContact(test);


    }

    @Test
    public void contactExists() throws Exception {
        collection = ContactCollection.getInstance();
        Contact test = new Contact("MSN", InetAddress.getByName("127.0.0.1"));
        collection.addContact(test);

        assert (collection.contactExists(test));
    }

    @Test
    public void getContact() throws Exception {
        collection = ContactCollection.getInstance();
        Contact test = new Contact("MSN", InetAddress.getByName("127.0.0.1"));
        collection.addContact(test);

       assert (collection.getContact(test.getFullPseudo()).getFullPseudo().equals(test.getFullPseudo()));
    }


}