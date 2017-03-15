package gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*Parent root = FXMLLoader.load(getClass().getResource("chatwindow.fxml"));
        primaryStage.setTitle("MSN Messenger");
        primaryStage.setScene(new Scene(root, 640, 480));
        primaryStage.show();*/

        /*primaryStage.setTitle("MSN Messenger");
        System.setProperty("gui.Main", "MSN Messenger");
        Button btn = new Button();
        btn.setText("Fronçois");
        btn.setOnAction((ActionEvent ev) -> {
                ChatWindow cw;
                try {
                    cw = new ChatWindow();
                    cw.show();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
        });

        StackPane root = new StackPane();

        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();*/
        MainWindow mw;

        mw = new MainWindow();
        mw.show();
    }

    /*public static void main(String[] args) {
        launch(args);
    }*/
}
