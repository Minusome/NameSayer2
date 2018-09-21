package namesayer.recording;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.media.MediaPlayer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static namesayer.recording.Config.CREATIONS_FOLDER;

public class Recording {

    private Path file;
    private boolean isCreatedByUser;
    private DoubleProperty rating = new SimpleDoubleProperty(0.0);


    //This needs to be here to prevent garbage collection
    private MediaPlayer player;

    public Recording(Path file) {
        this(file, false);
    }

//    public Recording(Path file, boolean isLoadingPreviousRating,boolean isCreatedByUser){
//        this(file, isCreatedByUser);
//        loadPreviousRating();
//    }



    public Recording(Path file, boolean isCreatedByUser) {
        this.file = file;
        this.isCreatedByUser = isCreatedByUser;
    }

    public void playAudio() {
        Thread thread = new Thread(() -> {
            String command = "ffplay -nodisp -autoexit -loglevel quiet \"" + file.toAbsolutePath().toString() + "\"";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
            try {
                Process process = builder.start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public double getRating() {
        return rating.get();
    }

    public DoubleProperty ratingProperty() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }

    @Override
    public String toString() {
        return file.getFileName()
                   .toString()
                   .replace(".wav", "");
    }


}
