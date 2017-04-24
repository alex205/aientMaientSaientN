package gui;


import controller.Controller;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.util.Callback;
import model.Contact;
import model.ContactCollection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import static controller.Controller.readBytesFromFile;

public class ContactWindowController extends BorderPane implements Initializable {
    private Stage stage;
    private Controller controller;

    @FXML
    protected Label pseudo_label;
    @FXML
    protected HBox header;
    @FXML
    protected ListView online_contacts;
    @FXML
    protected ChoiceBox<String> status_change_list;
    @FXML
    protected StackPane image_perso_pane;
    @FXML
    protected ImageView image_perso_view;
    @FXML
    protected TextField msg_perso;

    public ContactWindowController(Stage stage, Controller controller) {
        this.stage = stage;
        this.controller = controller;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("contactwindow.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }

        HBox.setHgrow(header, Priority.ALWAYS);
    }

    @FXML
    private void handleButton() {
    }

    @FXML
    private void handleMessagePersoEdit() {
        Contact me = ContactCollection.getMe();
        msg_perso.setEditable(true);
        msg_perso.getStyleClass().clear();
        msg_perso.getStyleClass().add("msg_perso_edition");
        if(me.getMessage_perso().equals("")) {
            msg_perso.setText("");
        }
    }

    @FXML
    private void handleMessagePersoSend(KeyEvent event) {
        Contact me = ContactCollection.getMe();
        if (event.getCode() == KeyCode.ENTER) {
            if(!msg_perso.getText().equals(me.getMessage_perso()) && !msg_perso.getText().equals("")) {
                me.setMessage_perso(msg_perso.getText());
                controller.changeMessagePerso(msg_perso.getText());
            } else if(msg_perso.getText().equals("")){
                msg_perso.setText("Entrez votre message perso");
            }

            msg_perso.setEditable(false);
            msg_perso.getStyleClass().clear();
            msg_perso.getStyleClass().add("msg_perso_std");
        }
    }

    @FXML
    private void handleListAction(MouseEvent event) {
        ChatWindow view = ViewController.getInstance().getView((Contact) online_contacts.getSelectionModel().getSelectedItem(), true);
        try {
            view.requestFocus();
        } catch (NullPointerException e) {}
    }

    @FXML
    private void handleImagePersoClick() {
        System.out.println("Image perso");
        ViewController viewController = ViewController.getInstance();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Modifier mon image perso");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers image (jpg, png, gif, bmp)", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(stage);

        changeImagePerso(file);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactCollection cc = ContactCollection.getInstance();
        pseudo_label.setText(ContactCollection.getMe().getPseudo());

        File file = new File("src/resources/images/default.png");
        image_perso_view.setImage(new Image("file:" + file.getAbsolutePath()));

        // statut initial
        image_perso_pane.getStyleClass().clear();
        switch (ContactCollection.getMe().getStatus()) {
            case ONLINE:
                image_perso_pane.getStyleClass().add("image_perso");
                status_change_list.getSelectionModel().select(0);
                break;
            case AWAY:
                image_perso_pane.getStyleClass().add("image_perso_away");
                status_change_list.getSelectionModel().select(1);
                break;
            case BUSY:
                image_perso_pane.getStyleClass().add("image_perso_busy");
                status_change_list.getSelectionModel().select(2);
                break;
            case OFFLINE:
                image_perso_pane.getStyleClass().add("image_perso_offline");
                status_change_list.getSelectionModel().select(3);
                break;
        }

        // Changement de statut
        status_change_list.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, number2) -> {
            String status = status_change_list.getItems().get((Integer) number2);
            status = status.substring(1, status.length()-1);
            image_perso_pane.getStyleClass().clear();
            switch (status) {
                case "Disponible":
                    image_perso_pane.getStyleClass().add("image_perso");
                    break;
                case "Absent":
                    image_perso_pane.getStyleClass().add("image_perso_away");
                    break;
                case "Occupé":
                    image_perso_pane.getStyleClass().add("image_perso_busy");
                    break;
                case "Hors-ligne":
                    image_perso_pane.getStyleClass().add("image_perso_offline");
                    break;
                case "Modifier mon image perso...":
                    System.out.println("image perso");
            }
            controller.changeStatus(status);
        });

        // Liste des contacts
        ObservableList<Contact> contactObservableList = cc.getCollection();
        cc.setAddCallback(contact -> Platform.runLater(() -> contactObservableList.add(contact)));
        cc.setDelCallback(contact -> Platform.runLater(() -> contactObservableList.remove(contact)));
        online_contacts.setItems(contactObservableList);

        online_contacts.setCellFactory(new Callback<ListView<Contact>, ListCell<Contact>>() {

            @Override
            public ListCell<Contact> call(ListView<Contact> p) {
                ListCell<Contact> cell = new ListCell<Contact>() {

                    @Override
                    protected void updateItem(Contact c, boolean bln) {
                        super.updateItem(c, bln);
                        if (c != null) {
                            switch(c.getStatus()){
                                case ONLINE:
                                    Platform.runLater(() -> setGraphic(new ImageView(new Image("file:src/resources/images/connected.png"))));
                                    break;
                                case AWAY:
                                    Platform.runLater(() -> setGraphic(new ImageView(new Image("file:src/resources/images/away.png"))));
                                    break;
                                case BUSY:
                                    Platform.runLater(() -> setGraphic(new ImageView(new Image("file:src/resources/images/busy.png"))));
                                    break;
                                case OFFLINE:
                                    Platform.runLater(() -> setGraphic(new ImageView(new Image("file:src/resources/images/offline.png"))));
                                    break;
                            }
                            Platform.runLater(() -> setText(c.getPseudo()));
                        } else {
                            Platform.runLater(() -> setText(""));
                            Platform.runLater(() -> setGraphic(null));
                        }
                    }
                };
                return cell;
            }
        });
    }


    public void changeImagePerso(File file){
        try {
            String img_encoded = Base64.getEncoder().encodeToString(readBytesFromFile(file));
            image_perso_view.setImage(new Image("file:" + file.getAbsolutePath()));
            controller.changeImagePerso(img_encoded);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
