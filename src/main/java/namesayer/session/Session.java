package namesayer.session;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import namesayer.model.CompositeName;
import namesayer.persist.NameStorageManager;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


public abstract class Session implements Serializable {


    protected List<CompositeName> namesList = new LinkedList<>();
    protected int currentIndex = 0;
    protected CompositeName currentName;
    protected String sessionName;
    protected String id;


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

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String input) {
        sessionName = input;
    }

    public void resetToFirst() {
        currentIndex = 0;
        currentName = namesList.get(0);
    }

    public Session() {
        id = UUID.randomUUID().toString();
    }

    public String getCurrentNameString() {
        return currentName.toString();
    }

    public void next() {
        currentName = namesList.get(++currentIndex);
    }

    public void makeNewRecording() {
        currentName.makeNewRecording();
    }

    public void makeNewRecording(EventHandler<ActionEvent> onFinished) {
        currentName.makeNewRecording(onFinished);
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

    public void saveUserRecording() {
        NameStorageManager.getInstance().persistCompleteRecordingsForName(currentName);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        return id.equals(session.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}