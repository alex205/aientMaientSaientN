package Test;

import model.Contact;
import org.junit.Test;

import java.net.InetAddress;


/**
 * Created by toon on 24/04/17.
 */
public class ContactTest {
    @Test
    public void getFullPseudo() throws Exception {
        Contact test = new Contact("MSN", InetAddress.getByName("127.0.0.1"));
        assert(test.getFullPseudo().equals("MSN@/127.0.0.1"));
    }

}