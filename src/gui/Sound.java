package gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Sound {

    public enum Sound_t {
        NEW_MESSAGE,
        NUDGE
    }

    public static void play(Sound_t type) {
        String musicFile = "";
        switch (type) {
            case NEW_MESSAGE:
                musicFile = "out/production/aientMaientSaientN/res/sound/type.wav";
                break;
            case NUDGE:
                musicFile = "out/production/aientMaientSaientN/res/sound/nudge.wav";
        }
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
}
