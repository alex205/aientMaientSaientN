package gui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class Sound {

    public enum Sound_t {
        NEW_MESSAGE,
        NUDGE
    }

    public static void play(Sound_t type) {
        URL snd  = null;
        switch (type) {
            case NEW_MESSAGE:
                snd = Sound.class.getResource("/resources/sound/type.wav");
                break;
            case NUDGE:
                snd = Sound.class.getResource("/resources/sound/nudge.wav");
        }

        try {
            Media sound = new Media(snd.toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
