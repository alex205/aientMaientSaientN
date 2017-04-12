package gui;


import controller.Controller;
import javafx.application.Platform;
import model.Contact;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class ViewController {

    public enum Update_type {
        NEW_MESSAGE
    }

    private HashMap<String, ChatWindow> viewMap;
    private Controller controller;

    //Singleton pour pouvoir récupérer la même instance dans tout le code
    private static class ViewControllerHolder {
        private final static ViewController instance = new ViewController();
    }

    //pour récupérer l'instance
    public static ViewController getInstance() {
        return ViewControllerHolder.instance;
    }

    private ViewController() {
        this.viewMap = new HashMap<>();
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public ChatWindow getView(Contact c) {
        /*
         * Pour résoudre le problème concurrent suivant :  il faut attendre que le thread graphique ait crée la fenêtre
         * et qu'il nous renvoie son instance avant de continuer, on utilise le countdownlatch
         */
        final CountDownLatch latch = new CountDownLatch(1);
        if(viewMap.get(c.getFullPseudo()) == null) //si on ne connaît pas encore la vue (ie fenêtre pas ouverte)
        {
            //on demande au thread graphique de la créer
            Platform.runLater(() -> {
                try {
                    ChatWindow view = new ChatWindow(controller, c);
                    viewMap.put(c.getFullPseudo(), view);
                    view.show();
                    latch.countDown(); //on notifie que c'est fait
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            try {
                latch.await(); //attente de la notification du thread graphique
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return viewMap.get(c.getFullPseudo());
    }

    public void delView(Contact c) {
        viewMap.remove(c.getFullPseudo());
    }

    public void updateView(ChatWindow view, Update_type type, String toUpdate) {
        switch (type) {
            case NEW_MESSAGE:
                view.getChatWindowController().addMessage(false, toUpdate);
        }
    }
}
