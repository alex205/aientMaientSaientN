package controller;

import model.Contact;
import model.ContactCollection;

import java.net.*;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        Controller controller = new Controller();
        ContactCollection cc = ContactCollection.getInstance();
        cc.addContact(new Contact("alex205", InetAddress.getByName("127.0.0.1")));

        //Lancement interface graphique
        gui.Main.setController(controller);
        gui.Main.launch(gui.Main.class);
    }
}

