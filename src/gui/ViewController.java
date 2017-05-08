package gui;


import controller.Controller;
import javafx.application.Platform;
import model.Contact;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

/**
 * Manages all views actions
 * This class is the link between back and front office code
 *
 * @author alex205
 * @author toon
 */
public class ViewController {

    public enum Update_type {
        NEW_MESSAGE,
        NEW_MESSAGE_ME,
        STATUS_CHANGE,
        NEW_NUDGE,
        IMAGE_PERSO_CHANGE,
        MESSAGE_PERSO_CHANGE
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

    /**
     * Return the chat window of the contact, if not exists this method will create the
     * view and register it into the ViewController map
     *
     * @param c
     * @param graphicThread
     * @return ChatWindow
     */
    public ChatWindow getView(Contact c, boolean graphicThread) {
        /*
         * Pour résoudre le problème concurrent suivant :  il faut attendre que le thread graphique ait crée la fenêtre
         * et qu'il nous renvoie son instance avant de continuer, on utilise le countdownlatch
         * Si on est dans le thread graphique, inutile, cela crée un interbloquage, d'où le booléen
         */
        final CountDownLatch latch = new CountDownLatch(1);
        if(viewMap.get(c.getFullPseudo()) == null) //si on ne connaît pas encore la vue (ie fenêtre pas ouverte)
        {
            //on demande au thread graphique de la créer
            Platform.runLater(() -> {
                try {
                    ChatWindow view = new ChatWindow(controller, c);
                    viewMap.put(c.getFullPseudo(), view);
                    //on rafraîchit le statut pour nous et le contact distant
                    view.getChatWindowController().refreshStatus(true);
                    view.getChatWindowController().refreshStatus(false);
                    view.show();
                    if(!graphicThread) {
                        latch.countDown(); //on notifie que c'est fait
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
           try {
               if(!graphicThread) {
                   latch.await(); //attente de la notification du thread graphique
               }
                System.out.println("okay attente");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return viewMap.get(c.getFullPseudo());
    }

    /**
     * Delete the view, called when a contact gets offline
     *
     * @param c
     */
    public void delView(Contact c) {
        viewMap.remove(c.getFullPseudo());
    }

    public void updateView(ChatWindow view, Update_type type, String toUpdate) {
        switch (type) {
            case NEW_MESSAGE:
                view.getChatWindowController().addMessage(false, toUpdate);
                break;
            case NEW_MESSAGE_ME:
                view.getChatWindowController().addMessage(true, toUpdate);
                break;
            case STATUS_CHANGE:
                System.out.println("changement dans la vue");
                view.getChatWindowController().refreshStatus(false);
                break;
            case NEW_NUDGE:
                view.getChatWindowController().shakeStage();
                view.getChatWindowController().addDialogWizz(false);
                break;
            case IMAGE_PERSO_CHANGE:
                try {
                    view.getChatWindowController().changeImagePerso(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case MESSAGE_PERSO_CHANGE:
                view.getChatWindowController().changeMessagePerso();
                break;
        }
    }

    public boolean viewExists(Contact c) {
        return viewMap.get(c.getFullPseudo()) != null;
    }

    public HashMap<String, ChatWindow> getAllViews() {
        return viewMap;
    }
}
