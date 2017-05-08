package controller;

import gui.ViewController;
import network.NetworkInterface;
//import Test.networkParrot.NetworkInterfaceParrot;

import java.net.*;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        Controller controller = new Controller(); //instantiation du contrôleur
        NetworkInterface.getInstance().setController(controller);
        //NetworkInterfaceParrot.getInstance().setController(controller);

        //On passe le contrôleur principal au contrôleur de vues pour que les vues de chat y ait accès
        ViewController viewController = ViewController.getInstance();
        viewController.setController(controller);

        //Lancement interface graphique
        //On passe le contrôleur aux vues de bases (ie toutes les fenêtres hors chat)
        gui.Main.setController(controller);
        gui.Main.launch(gui.Main.class);
    }
}

