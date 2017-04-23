package gui;

import controller.Controller;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.stage.StageStyle;
import javafx.util.Duration;
import model.Contact;
import model.ContactCollection;
import network.Notification;
import sun.misc.BASE64Decoder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatWindowController  extends BorderPane implements Initializable {

    private Stage stage;
    private Controller controller;
    private Contact contact;

    private SimpleDateFormat date_format;
    private SimpleDateFormat heure_format;

    private boolean was_me; //optimisation graphique, on ne répète pas le nom de la personne si plusieurs message à la suite
    private boolean was_dialog;

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
    @FXML
    protected Button btn_fichiers;
    @FXML
    protected ImageView nudge_button;
    @FXML
    protected ImageView contact_image_perso;
    @FXML
    protected ImageView me_image_perso;
    @FXML
    protected Label message_perso_label;




    public ChatWindowController(Stage stage, Controller controller, Contact contact) {
        this.stage = stage;
        this.controller = controller;
        this.contact = contact;
        date_format = new SimpleDateFormat("dd/MM/yyyy");
        heure_format = new SimpleDateFormat("HH:mm");
        was_me = true;
        was_dialog = false;

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
        message_perso_label.setText(contact.getMessage_perso());
        message_write.setStyle("-fx-text-inner-color: #" + ContactCollection.getMe().getTextColor());

        try {
            changeImagePerso(true);
            changeImagePerso(false);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                    controller.changeTextColor(color);
                }
            } catch(NoSuchElementException e) {}
        });



    }

    public void changeImagePerso(boolean me) throws IOException {
        if(me){
            String image64 = ContactCollection.getMe().getImage_perso();
            BASE64Decoder base64Decoder = new BASE64Decoder();
            ByteArrayInputStream imageInputStream = new ByteArrayInputStream(base64Decoder.decodeBuffer(image64));
            Image image = new Image(imageInputStream);
            me_image_perso.setImage(image);

        } else {
            String image64 = contact.getImage_perso();
            BASE64Decoder base64Decoder = new BASE64Decoder();
            ByteArrayInputStream imageInputStream = new ByteArrayInputStream(base64Decoder.decodeBuffer(image64));
            Image image = new Image(imageInputStream);
            contact_image_perso.setImage(image);
        }
    }

    public void addMessage(boolean me, String message) {
        was_dialog = false;
        Text caption = new Text();
        Text msg = new Text();
        Text bullet = new Text();
        bullet.setText("•  ");
        bullet.setStyle("-fx-fill: #828282;");
        caption.setStyle("-fx-fill: #828282;");
        if(me) {
            msg.setStyle("-fx-fill: #" + ContactCollection.getMe().getTextColor());
            if(!was_me || messages_received.getChildren().isEmpty() || was_dialog) {
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

        //play sounds lel
        if(!stage.isFocused()) {
            Sound.play(Sound.Sound_t.NEW_MESSAGE);
        }
    }


    public void addDialogWizz(boolean me){
        was_me = false;
        Text lineB = new Text();
        Text caption = new Text();
        lineB.setText("           " + System.lineSeparator());
        lineB.setStyle("-fx-strikethrough: true;");

        Text lineT = new Text();
        lineT.setText("           " + System.lineSeparator());
        lineT.setStyle("-fx-strikethrough: true;");
        if(me) {
            caption.setText("Vous avez envoyé un wizz !" + System.lineSeparator());
        } else {
            caption.setText(contact.getPseudo()+" vous a envoyé un wizz." + System.lineSeparator());
        }

        Platform.runLater(() -> {
            if(!was_dialog){
               messages_received.getChildren().add(lineT);
            }
            messages_received.getChildren().add(caption);
            messages_received.getChildren().add(lineB);
            was_dialog = true;
        });


    }

    public void refreshStatus(boolean me) {
        StackPane pane = (me ? me_image_perso_pane : dest_image_perso_pane);
        Contact c = (me ? ContactCollection.getMe() : contact);
        pane.getStyleClass().clear();
            switch (c.getStatus()) {
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

    public void changeMessagePerso() {
        message_perso_label.setText(contact.getMessage_perso());
    }




    @FXML
    public void handleFileButton() throws IOException {
        System.out.println("FICHIERERERERER");

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        File file = fileChooser.showOpenDialog(stage);

        controller.sendFile(contact, file);
    }


    @FXML public void handleNudgeButton() throws IOException {
        System.out.println("On demande au controller d'envoyer un wizz !\n");
        addDialogWizz(true);
        controller.sendNudge(contact, Notification.Notification_type.NUDGE);
    }



    //C'est pas très propre mais c'est iun début... patapé
    //pour test unitaire on peut mettre sur le bouton nudge "ShakeStage" a executer.
    //Pour tester avec le réseau il faut mettre handleNudgeButton
    private int x = 0;
    private int y = 0;
    public void shakeStage() {
        Sound.play(Sound.Sound_t.NUDGE);

        Timeline timelineX = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (x == 0) {
                    stage.setX(stage.getX() +5);
                    x = 1;
                } else {
                    stage.setX(stage.getX() - 5);
                    x = 0;
                }
            }
        }));

        timelineX.setCycleCount(12);
        timelineX.setAutoReverse(false);
        timelineX.play();


        Timeline timelineY = new Timeline(new KeyFrame(Duration.seconds(0.05), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if (y == 0) {
                    stage.setY(stage.getY() + 5);
                    y = 1;
                } else {
                    stage.setY(stage.getY() - 5);
                    y = 0;
                }
            }
        }));

        timelineY.setCycleCount(12);
        timelineY.setAutoReverse(false);
        timelineY.play();
    }


}
