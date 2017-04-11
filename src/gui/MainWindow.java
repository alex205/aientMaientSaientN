package gui;

import controller.Controller;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow {
    public static Stage stage;

    public MainWindow(Controller cont) throws IOException {
        this.stage = new Stage();
        LoginWindowController controller = new LoginWindowController(this.stage, cont);
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
