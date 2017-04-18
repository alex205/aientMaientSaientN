package controller;

import gui.ViewController;
import model.Contact;
import model.ContactCollection;

import java.net.*;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        Controller controller = new Controller(); //instantiation du contrôleur

        /*ContactCollection cc = ContactCollection.getInstance();
        cc.addContact(new Contact("alex205", InetAddress.getByName("127.0.0.1")));
        cc.addContact(new Contact("Patrick974", InetAddress.getByName("127.0.0.1")));
        cc.addContact(new Contact("Toonu", InetAddress.getByName("127.0.0.1")));
        cc.addContact(new Contact("Drosik", InetAddress.getByName("127.0.0.1")));*/

        //On passe le contrôleur principal au contrôleur de vues pour que les vues de chat y ait accès
        ViewController viewController = ViewController.getInstance();
        viewController.setController(controller);

        //Lancement interface graphique
        //On passe le contrôleur aux vues de bases (ie toutes les fenêtres hors chat)
        gui.Main.setController(controller);
        gui.Main.launch(gui.Main.class);
    }
}

