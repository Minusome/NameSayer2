package namesayer.recording;


import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.nio.file.Path;

public class Recording {

    private Path file;
    private boolean isCreatedByUser;
    //This needs to be here to prevent garbage collection
    private MediaPlayer player;

    public Recording(Path file) {
        this(file, false);
    }

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

    @Override
    public String toString() {
        return file.getFileName().toString();
    }


}
