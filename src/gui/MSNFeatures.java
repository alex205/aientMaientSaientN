package gui;

import javafx.application.Platform;
import javafx.stage.Stage;

public class MSNFeatures {

    public static void nudge(Stage stage) {
       // Platform.runLater(MSNFeatures);
        System.out.println("TEST");
        //for(int i=0; i<7; i++) {
            stage.setX(stage.getX()+10.0);
            stage.setY(stage.getY()+5.0);
        //}
    }
}
