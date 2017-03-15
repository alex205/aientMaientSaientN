package gui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatWindowController  extends BorderPane{

    private Stage stage;

    @FXML
    protected VBox box_type_message;
    @FXML
    protected TextArea messages_received;
    @FXML
    protected ImageView nudge_button;
   /* @FXML
    TextArea message_write;*/



    public ChatWindowController(Stage stage) {
        this.stage = stage;
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

        //Actions de la toolbar
        nudge_button.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                nudgeAction();
                event.consume();
            }
        });
    }

    @FXML
    protected void nudgeAction() {
        MSNFeatures.nudge(this.stage);
        System.out.println("wizz");
    }

}
