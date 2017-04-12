package gui;


import model.Contact;

import java.io.IOException;
import java.util.HashMap;

public class ViewController {

    private HashMap<String, ChatWindow> viewMap;

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

    public ChatWindow getView(Contact c) {
        ChatWindow view = viewMap.get(c.getFullPseudo());
        if(view == null) {
            try {
                view = new ChatWindow();
                viewMap.put(c.getFullPseudo(), view);
                view.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return view;
    }
}
