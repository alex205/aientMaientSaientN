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
        stage.setResizable(false);
        stage.setWidth(800);
        stage.setHeight(600);
    }

    public void show() {
        this.stage.show();
    }
}
