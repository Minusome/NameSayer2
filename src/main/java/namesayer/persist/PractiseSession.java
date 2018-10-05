package namesayer.persist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;

import java.util.HashMap;
import java.util.Map;

public class PractiseSession extends Session {

    //User can make a number of attempts at the name
    private Map<CompositeName, ObservableList<CompositeRecording>> userAttempts = new HashMap<>();


    @Override
    public void addName(CompositeName compositeName) {
        super.addName(compositeName);
        userAttempts.put(compositeName, FXCollections.observableArrayList());
    }

    public CompositeName prev() {
        currentName = namesList.get(--currentIndex);
        return currentName;
    }

    public boolean hasPrev() {
        return !(currentIndex == 0);
    }

    @Override
    protected void addNewRecording(CompositeRecording recording) {
        userAttempts.get(currentName).add(recording);
    }

    public ObservableList<CompositeRecording> getRecordingsForCurrentName() {
        return FXCollections.unmodifiableObservableList(userAttempts.get(currentName));
    }

    public void removeRecordingForCurrentName(CompositeRecording recording) {
        userAttempts.get(currentName).remove(recording);
    }





}
