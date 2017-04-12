package gui;

import controller.Controller;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import model.Contact;
import model.ContactCollection;

import javax.swing.plaf.metal.MetalBorders;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class ChatWindowController  extends BorderPane implements Initializable {

    private Stage stage;
    private Controller controller;
    private Contact contact;

    private SimpleDateFormat date_format;
    private SimpleDateFormat heure_format;

    private boolean was_me; //optimisation graphique, on ne répète pas le nom de la personne si plusieurs message à la suite

    @FXML
    protected VBox box_type_message;
    @FXML
    protected TextFlow messages_received;
    @FXML
    protected Label pseudo_label;
    @FXML
    protected Label last_message_date_label;




    public ChatWindowController(Stage stage, Controller controller, Contact contact) {
        this.stage = stage;
        this.controller = controller;
        this.contact = contact;
        date_format = new SimpleDateFormat("dd/MM/yyyy");
        heure_format = new SimpleDateFormat("HH:mm");
        was_me = true;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("chatwindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        //Pour gérer le redimensionnement de la fenêtre
        VBox.setVgrow(messages_received, Priority.ALWAYS);
        HBox.setHgrow(box_type_message, Priority.ALWAYS);

        //Gestion de la fermeture de la fenêtre
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                ViewController vc = ViewController.getInstance();
                vc.delView(contact);
            }
        });

        //Actions de la toolbar
        /*nudge_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                nudgeAction();
                event.consume();
            }
        });*/
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.setTitle(contact.getPseudo() + " - Conversation");
        pseudo_label.setText(contact.getPseudo());
    }

    public void addMessage(boolean me, String message) {
        Text caption = new Text();
        Text msg = new Text();
        Text bullet = new Text();
        bullet.setText("•  ");
        caption.setStyle("-fx-fill: #828282;");
        if(me) {
            if(!was_me) {
                caption.setText(System.lineSeparator() + ContactCollection.getMe().getPseudo() + " dit :" + System.lineSeparator());
                Platform.runLater(() -> messages_received.getChildren().add(caption));
                was_me = true;
            }
        } else {
            if(was_me) {
                caption.setText(System.lineSeparator() + contact.getPseudo() + " dit :" + System.lineSeparator());
                Platform.runLater(() -> messages_received.getChildren().add(caption));
                was_me = false;
            }
        }
        msg.setText(message + System.lineSeparator());
        Platform.runLater(() -> messages_received.getChildren().addAll(bullet, msg));
        Platform.runLater(() -> last_message_date_label.setText("Dernier message reçu à " + heure_format.format(new Date()) + " le " + date_format.format(new Date())));
    }
}
