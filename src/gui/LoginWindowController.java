package gui;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginWindowController extends BorderPane {

    private Stage stage;
    private Controller controller;

    @FXML
    private TextField pseudo;
    @FXML
    private Button connect_btn;

    public LoginWindowController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginwindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getSource() == connect_btn) {
            controller.connect(pseudo.getText());
            System.out.println("connexion");
            Stage stage = (Stage) connect_btn.getScene().getWindow();
            ContactWindowController contactController = new ContactWindowController(stage);
            stage.setResizable(true);
            stage.setHeight(800);
            stage.setWidth(400);
            stage.setScene(new Scene(contactController));
            stage.show();
        }
    }

}
