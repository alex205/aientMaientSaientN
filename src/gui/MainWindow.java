package gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow {
    private Stage stage;

    public MainWindow() throws IOException {
        this.stage = new Stage();
        MainWindowController controller = new MainWindowController(this.stage);
        stage.setTitle("MSN Messenger");
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
