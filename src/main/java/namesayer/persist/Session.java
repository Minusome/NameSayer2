package namesayer.persist;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import namesayer.model.CompleteName;
import namesayer.model.CompleteRecording;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static namesayer.util.Config.DATABSE_FOLDER;
import static namesayer.util.Config.USER_ATTEMPTS;
import static namesayer.util.Config.WAV_EXTENSION;

public abstract class Session {

    protected List<CompleteName> namesList = new LinkedList<>();
    protected int currentIndex = 0;
    protected CompleteName currentName;

    public enum SessionType {
        ASSESSMENT, PRACTISE
    }


    public void addName(CompleteName completeName) {
        namesList.add(completeName);
    }

    public CompleteName getCurrentName() {
        currentName = namesList.get(currentIndex);
        return currentName;
    }

    public CompleteName next() {
        currentName = namesList.get(++currentIndex);
        return currentName;
    }

    public void makeNewRecording(String recordingName, EventHandler<ActionEvent> onFinished) {
        Thread thread = new Thread(() -> {
            Path newRecordingPath = DATABSE_FOLDER.resolve(USER_ATTEMPTS).resolve(recordingName + WAV_EXTENSION).toAbsolutePath();
            String command = "ffmpeg -loglevel \"quiet\" -f alsa -i default -t " + currentName.getExemplar().getLength() + " -acodec pcm_s16le -ar 16000 -ac 1 -y \"" +
                    newRecordingPath.toString() + "\"";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
            try {
                Process process = builder.start();
                process.waitFor();
                Platform.runLater(() -> {
                    addNewRecording(new CompleteRecording(newRecordingPath));
                    onFinished.handle(new ActionEvent());
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    protected abstract void addNewRecording(CompleteRecording recording);

    public boolean hasNext() {
        return !(currentIndex == namesList.size() - 1);
    }

    public CompleteRecording getExemplar() {
        return currentName.getExemplar();
    }

}
