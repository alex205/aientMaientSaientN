package gui;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class MSNFeatures {

    public enum Sound_t {
        NEW_MESSAGE
    }

    public static void nudge(Stage stage) {
       // Platform.runLater(MSNFeatures);
        System.out.println("TEST");
        //for(int i=0; i<7; i++) {
            stage.setX(stage.getX()+10.0);
            stage.setY(stage.getY()+5.0);
        //}
    }

    public static void sound(Sound_t type) {
        String musicFile = "";
        switch (type) {
            case NEW_MESSAGE:
                musicFile = "src/resources/sound/type.wav";
        }
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
