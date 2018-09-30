package namesayer.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Path;

import static namesayer.model.Config.TEMP_RECORDINGS;
import static namesayer.model.Config.WAV_EXTENSION;

public class PractiseName extends Name {

    private ObservableList<PractiseNameRecording> userMadeRecordings = FXCollections.observableArrayList();
    private ObservableList<PractiseNameRecording> savedRecordings = FXCollections.observableArrayList();


    public PractiseName(String name, Path directory) {
        super(name, directory);
    }

    public void makeNewRecording(String recordingName) {
        Thread thread = new Thread(() -> {
            Path newRecordingPath = directory.resolve(TEMP_RECORDINGS).resolve(recordingName + WAV_EXTENSION).toAbsolutePath();
            String command = "ffmpeg -loglevel \"quiet\" -f alsa -i default -t 3 -acodec pcm_s16le -ar 16000 -ac 1 -y \"" +
                    newRecordingPath.toString() + "\"";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
            try {
                Process process = builder.start();
                process.waitFor();
                Platform.runLater(() -> userMadeRecordings.add(new PractiseNameRecording(newRecordingPath)));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
