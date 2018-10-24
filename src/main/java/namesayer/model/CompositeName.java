package namesayer.model;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static namesayer.persist.Config.*;


/**
 * Represents a full-name created by the user.
 * This name combines various PartialNames which come bundled with the Names Corpus Database.
 */

public class CompositeName extends Name implements Serializable {


    private List<CompositeRecording> userAttempts = new ArrayList<>();
    private Exemplar exemplar;

    public CompositeName(String name) {
        super(name);
    }

    public void addUserAttempt(CompositeRecording recording) {
        userAttempts.add(recording);
    }

    public List<CompositeRecording> getUserAttempts() {
        return userAttempts;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    /**
     * Creates a new recording for this name using FFmpeg.
     * This command is executed by a bash shell in a separate thread.
     *
     *
     * @param onFinished The EventHandler executed after recording is complete
     */
    public void makeNewRecording(EventHandler<ActionEvent> onFinished) {
        String temp = "se206_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("d-M-yyyy_HH:mm:ss")) +
                " " +
                name;
        String recordingName = temp.trim().replace(" ", "_");

        Thread thread = new Thread(() -> {
            Path newRecordingPath = USER_ATTEMPTS.resolve(recordingName + WAV_EXTENSION).toAbsolutePath();
            String command = "ffmpeg -loglevel \"quiet\" -f alsa -i default -t " + (exemplar.getLength() + BUFFER_TIME) + " -acodec pcm_s16le -ar 16000 -ac 1 -y \"" +
                    newRecordingPath.toString() + "\"";
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", command);
            try {
                Process process = processBuilder.start();
                process.waitFor();
                Platform.runLater(() -> {
                    userAttempts.add(new CompositeRecording(newRecordingPath));
                    onFinished.handle(new ActionEvent());
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

}
