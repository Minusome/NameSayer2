package namesayer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Path;

import static namesayer.util.Config.DATABSE_FOLDER;
import static namesayer.util.Config.USER_ATTEMPTS;
import static namesayer.util.Config.WAV_EXTENSION;

public class CompleteName extends Name {


    private CompleteNameRecording exemplar;
    private ObservableList<CompleteNameRecording> tempRecordings = FXCollections.observableArrayList();
    private ObservableList<CompleteNameRecording> savedRecordings = FXCollections.observableArrayList();

    public CompleteName(String name) {
        super(name);
    }

    public void makeNewTempRecording(String recordingName) {
        Thread thread = new Thread(() -> {
            Path newRecordingPath = DATABSE_FOLDER.resolve(USER_ATTEMPTS).resolve(recordingName + WAV_EXTENSION).toAbsolutePath();
            String command = "ffmpeg -loglevel \"quiet\" -f alsa -i default -t " + exemplar.getLength() + " -acodec pcm_s16le -ar 16000 -ac 1 -y \"" +
                    newRecordingPath.toString() + "\"";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
            try {
                Process process = builder.start();
                process.waitFor();
                tempRecordings.add(new CompleteNameRecording(newRecordingPath));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void compareUserAttemptWithExemplar() {
        Thread thread = new Thread(() -> {
            try {
                exemplar.playAudio();
                Thread.sleep(new Double(exemplar.getLength() * 1000).longValue());
                tempRecordings.get(0).playAudio();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public void deleteAllTempRecording() {
        tempRecordings.clear();
    }

    public void saveTempRecordings() {
        savedRecordings.addAll(tempRecordings);
        tempRecordings.clear();
    }

    public void setExemplar(CompleteNameRecording exemplar) {
        this.exemplar = exemplar;
    }

    public CompleteNameRecording getExemplar() {
        return exemplar;
    }

}
