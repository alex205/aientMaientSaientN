package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatWindow {

    private Stage stage;

    public ChatWindow() throws IOException {
        this.stage = new Stage();
        ChatWindowController controller = new ChatWindowController(this.stage);
        stage.setTitle("Fronçois - Conversation");
        stage.setScene(new Scene(controller));
        stage.setMinWidth(630);
        stage.setMinHeight(470);
        stage.setWidth(640);
        stage.setHeight(480);
    }


    public void show() {
        this.stage.show();
    }
}
