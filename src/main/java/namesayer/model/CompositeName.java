package namesayer.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static namesayer.persist.Config.USER_ATTEMPTS;
import static namesayer.persist.Config.WAV_EXTENSION;

public class CompositeName extends Name {

    /**
     * These recordings are permanently associated with this name
     * NameStorageManager accesses this list, any other class will only be able to see an empty list
     */
    private ObservableList<CompositeRecording> userAttempts = FXCollections.observableArrayList();
    private Exemplar exemplar;

    public CompositeName(String name) {
        super(name);
    }

    public void addUserAttempt(CompositeRecording recording) {
        userAttempts.add(recording);
    }

    public ObservableList<CompositeRecording> getUserAttempts() {
        return userAttempts;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }

    public void makeNewRecording(EventHandler<ActionEvent> onFinished) {
        //TODO Assume that the name provided is in a suitable format, will provide need regex to check later
        String temp = "se206_" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("d-M-yyyy_HH:mm:ss")) +
                " " +
                name;
        String recordingName = temp.trim().replace(" ", "_");

        Thread thread = new Thread(() -> {
            Path newRecordingPath = USER_ATTEMPTS.resolve(recordingName + WAV_EXTENSION).toAbsolutePath();
            String command = "ffmpeg -loglevel \"quiet\" -f alsa -i default -t " + exemplar.getLength() + " -acodec pcm_s16le -ar 16000 -ac 1 -y \"" +
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

    public void makeNewRecording() {
        this.makeNewRecording(event -> {});
    }


}
