package gui;

import controller.Controller;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Contact;

import java.io.IOException;



public class LoginWindowController extends BorderPane {

    private Stage stage;
    private Controller controller;
    private Contact.Status_t myStatus = Contact.Status_t.ONLINE;

    @FXML
    private TextField pseudo;
    @FXML
    private Button connect_btn;
    @FXML
    private ChoiceBox status_change_list;
    @FXML
    private ImageView login_status_img;


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

        status_change_list.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                if(status_change_list.getItems().get((Integer) number2 ).equals("Disponible")){
                    login_status_img.setImage(new Image(getClass().getResourceAsStream("/resources/images/connected.png")));
                    myStatus = Contact.Status_t.ONLINE;
                } else if (status_change_list.getItems().get((Integer) number2 ).equals("Absent")){
                    login_status_img.setImage(new Image(getClass().getResourceAsStream("/resources/images/away.png")));
                    myStatus = Contact.Status_t.AWAY;
                } else if (status_change_list.getItems().get((Integer) number2 ).equals("Occupé")){
                    login_status_img.setImage(new Image(getClass().getResourceAsStream("/resources/images/busy.png")));
                    myStatus = Contact.Status_t.BUSY;
                } else if (status_change_list.getItems().get((Integer) number2 ).equals("Hors-ligne")){
                    login_status_img.setImage(new Image(getClass().getResourceAsStream("/resources/images/offline.png")));
                    myStatus = Contact.Status_t.OFFLINE;
                }
            }
        });
    }

    @FXML
    public void handleButtonAction(ActionEvent event) throws IOException {
        if(event.getSource() == connect_btn) {
            controller.connect(pseudo.getText());
            controller.changeStatus(myStatus.toString());
            System.out.println("connexion");
            Stage stage = (Stage) connect_btn.getScene().getWindow();
            ContactWindowController contactController = new ContactWindowController(stage, controller);
            stage.setResizable(false);
            stage.setHeight(800);
            stage.setWidth(500);
            stage.setScene(new Scene(contactController));
            stage.show();
        }
    }






}
