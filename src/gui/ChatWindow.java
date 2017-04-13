package gui;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Contact;
import network.Control;

import java.io.IOException;

public class ChatWindow {

    private Stage stage;
    private Controller controller;
    private Contact contact;
    private ChatWindowController chatWindowController;

    public ChatWindow(Controller ctrl, Contact contact) throws IOException {
        this.stage = new Stage();
        this.controller = ctrl;
        this.contact = contact;
        chatWindowController = new ChatWindowController(this.stage, this.controller, contact);
        stage.setScene(new Scene(chatWindowController));
        stage.setMinWidth(630);
        stage.setMinHeight(470);
        stage.setWidth(640);
        stage.setHeight(480);
    }


    public void show() {
        this.stage.show();
    }

    public void requestFocus() {
        stage.requestFocus();
    }

    public ChatWindowController getChatWindowController() {
        return chatWindowController;
    }
}
