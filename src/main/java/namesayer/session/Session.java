package namesayer.session;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;
import namesayer.model.Exemplar;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import static namesayer.util.Config.DATABSE_FOLDER;
import static namesayer.util.Config.USER_ATTEMPTS;
import static namesayer.util.Config.WAV_EXTENSION;

public abstract class Session {

    protected List<CompositeName> namesList = new LinkedList<>();
    protected int currentIndex = 0;
    protected CompositeName currentName;

    public enum SessionType {
        ASSESSMENT, PRACTISE
    }


    public void addName(CompositeName compositeName) {
        namesList.add(compositeName);
        currentName = namesList.get(0);
    }

    public String getCurrentNameString() {
        return currentName.toString();
    }

    public void next() {
        currentName = namesList.get(++currentIndex);
    }

    public void makeNewRecording(String recordingName, EventHandler<ActionEvent> onFinished) {
        currentName.makeNewRecording(recordingName, onFinished);
    }

    public boolean hasNext() {
        return !(currentIndex == namesList.size() - 1);
    }

    public void playExemplar() {
        currentName.getExemplar().playAudio();
    }

    public double getExemplarLength() {
        return currentName.getExemplar().getLength();
    }

}
