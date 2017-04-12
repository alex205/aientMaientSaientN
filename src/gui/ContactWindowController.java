package gui;


import controller.Controller;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import javafx.util.Callback;
import model.Contact;
import model.ContactCollection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
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
    private void handleButtonAction(ActionEvent event) throws UnknownHostException {
        ContactCollection cc = ContactCollection.getInstance();
        controller.sendMessage(cc.getContact("letesteur"), "coucou");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ContactCollection cc = ContactCollection.getInstance();
        pseudo_label.setText(ContactCollection.getMe().getPseudo());
        ObservableList<Contact> contactObservableList = FXCollections.observableList(cc.getCollection());
        cc.setAddCallback(contact -> Platform.runLater(() -> contactObservableList.add(contact)));
        online_contacts.setItems(contactObservableList);
        online_contacts.setCellFactory(new Callback<ListView<Contact>, ListCell<Contact>>() {
            @Override
            public ListCell<Contact> call(ListView<Contact> p) {

                ListCell<Contact> cell = new ListCell<Contact>() {

                    @Override
                    protected void updateItem(Contact c, boolean bln) {
                        super.updateItem(c, bln);
                        if (c != null) {
                            setText(c.getPseudo());
                        }
                    }
                };
                return cell;
            }
        });
    }
}