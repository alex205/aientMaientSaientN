package gui;


import controller.Controller;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javafx.util.Callback;
import model.Contact;
import model.ContactCollection;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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
        Contact c = ContactCollection.getInstance().getContact("alex205@/127.0.0.1");
        c.setStatus(Contact.Status_t.AWAY);
        System.out.println("statut : " + c.getStatus().toString());
    }

    @FXML
    private void handleListAction(MouseEvent event) {
        ChatWindow view = ViewController.getInstance().getView((Contact) online_contacts.getSelectionModel().getSelectedItem(), true);
        try {
            view.requestFocus();
        } catch (NullPointerException e) {}
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactCollection cc = ContactCollection.getInstance();
        pseudo_label.setText(ContactCollection.getMe().getPseudo());

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
                                    setGraphic(new ImageView(new Image("file:out/production/aientMaientSaientN/res/img/connected.png")));
                                    break;
                                case AWAY:
                                    setGraphic(new ImageView(new Image("file:out/production/aientMaientSaientN/res/img/away.png")));
                                    break;
                                case BUSY:
                                    setGraphic(new ImageView(new Image("file:out/production/aientMaientSaientN/res/img/busy.png")));
                                    break;
                                case OFFLINE:
                                    setGraphic(new ImageView(new Image("file:out/production/aientMaientSaientN/res/img/offline.png")));
                                    break;
                            }

                            setText(c.getPseudo());
                        }
                    }
                };
                return cell;
            }
        });
    }
}
