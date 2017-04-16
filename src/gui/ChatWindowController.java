package gui;

import controller.Controller;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import model.Contact;
import model.ContactCollection;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.ResourceBundle;

public class ChatWindowController  extends BorderPane implements Initializable {

    private Stage stage;
    private Controller controller;
    private Contact contact;

    private SimpleDateFormat date_format;
    private SimpleDateFormat heure_format;

    private boolean was_me; //optimisation graphique, on ne répète pas le nom de la personne si plusieurs message à la suite

    @FXML
    protected StackPane dest_image_perso_pane;
    @FXML
    protected StackPane me_image_perso_pane;
    @FXML
    protected VBox box_type_message;
    @FXML
    protected TextFlow messages_received;
    @FXML
    protected TextArea message_write;
    @FXML
    protected ScrollPane messages_received_pane;
    @FXML
    protected Label pseudo_label;
    @FXML
    protected Label status_label;
    @FXML
    protected Label last_message_date_label;
    @FXML
    protected ImageView color_button;




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

        //Pour scroller automatiquement en bas du scrollpane
        messages_received.getChildren().addListener(
                (ListChangeListener<Node>) ((change) -> {
                    messages_received.layout();
                    messages_received_pane.layout();
                    messages_received_pane.setVvalue(1.0f);
                })
        );

        //Gestion de la fermeture de la fenêtre
        stage.setOnCloseRequest(we -> {
            ViewController vc = ViewController.getInstance();
            vc.delView(contact);
        });

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stage.setTitle(contact.getPseudo() + " - Conversation");
        pseudo_label.setText(contact.getPseudo());
        status_label.setText("\"" + contact.getStatus().toString() + "\"");
        message_write.setStyle("-fx-text-inner-color: #" + ContactCollection.getMe().getTextColor());

        //Envoi de message
        message_write.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                controller.sendMessage(contact, message_write.getText());
                addMessage(true, message_write.getText());
                message_write.setText("");
                ke.consume();
            }
        });

        //Actions de la toolbar

        //Choix de la couleur du texte
        color_button.addEventHandler(MouseEvent.MOUSE_CLICKED, (EventHandler<MouseEvent>) event -> {
            event.consume();
            System.out.println("choix couleur");
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Modifier la couleur");
            alert.initStyle(StageStyle.UTILITY);

            ButtonType valider = new ButtonType("OK");
            alert.getButtonTypes().setAll(valider);

            String current_color = message_write.getStyle().split(":")[1].substring(1);
            ColorPicker picker = new ColorPicker(Color.web(current_color));
            alert.getDialogPane().setContent(picker);
            Optional<ButtonType> res =  alert.showAndWait();
            try {
                if (res.get() == valider) {
                    String color = picker.getValue().toString().substring(2, 8).toUpperCase();
                    message_write.setStyle("-fx-text-inner-color: #" + color);
                    ContactCollection.getMe().setTextColor(color);
                    controller.changeTextColor(contact, color);
                }
            } catch(NoSuchElementException e) {}
        });
    }

    public void addMessage(boolean me, String message) {
        Text caption = new Text();
        Text msg = new Text();
        Text bullet = new Text();
        bullet.setText("•  ");
        bullet.setStyle("-fx-fill: #828282;");
        caption.setStyle("-fx-fill: #828282;");
        if(me) {
            msg.setStyle("-fx-fill: #" + ContactCollection.getMe().getTextColor());
            if(!was_me || messages_received.getChildren().isEmpty()) {
                caption.setText(System.lineSeparator() + ContactCollection.getMe().getPseudo() + " dit :" + System.lineSeparator());
                Platform.runLater(() -> messages_received.getChildren().add(caption));
                was_me = true;
            }
        } else {
            msg.setStyle("-fx-fill: #" + contact.getTextColor());
            Platform.runLater(() -> last_message_date_label.setText("Dernier message reçu à " + heure_format.format(new Date()) + " le " + date_format.format(new Date())));
            if(was_me) {
                caption.setText(System.lineSeparator() + contact.getPseudo() + " dit :" + System.lineSeparator());
                Platform.runLater(() -> messages_received.getChildren().add(caption));
                was_me = false;
            }
        }
        msg.setText(message + System.lineSeparator());
        Platform.runLater(() -> messages_received.getChildren().addAll(bullet, msg));
    }

    public void refreshStatus(boolean me) {
        StackPane pane = (me ? me_image_perso_pane : dest_image_perso_pane);
        pane.getStyleClass().clear();
            switch (contact.getStatus()) {
                case ONLINE:
                    pane.getStyleClass().add("image_perso");
                    break;
                case AWAY:
                    pane.getStyleClass().add("image_perso_away");
                    break;
                case BUSY:
                    pane.getStyleClass().add("image_perso_busy");
                    break;
                case OFFLINE:
                    pane.getStyleClass().add("image_perso_offline");
                    break;
            }
            if(!me) {
                Platform.runLater(() -> status_label.setText("\"" + contact.getStatus().toString() + "\""));
            }
    }
}
