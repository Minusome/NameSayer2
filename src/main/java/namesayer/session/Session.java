package namesayer.session;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import namesayer.model.CompositeName;

import java.util.LinkedList;
import java.util.List;

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

    public void removeName(CompositeName compositeName) {
        namesList.remove(compositeName);
        if (namesList.size() == 0) {
            currentName = null;
        }
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

    public int getCurrentIndex() {
        return currentIndex;
    }

    public int getNumberOfNames() {
        return namesList.size();
    }

}
