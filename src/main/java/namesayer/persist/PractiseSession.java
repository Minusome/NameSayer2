package namesayer.persist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import namesayer.model.CompleteName;
import namesayer.model.CompleteRecording;

import java.util.HashMap;
import java.util.Map;

public class PractiseSession extends Session {

    //User can make a number of attempts at the name
    private Map<CompleteName, ObservableList<CompleteRecording>> userAttempts = new HashMap<>();


    @Override
    public void addName(CompleteName completeName) {
        super.addName(completeName);
        userAttempts.put(completeName, FXCollections.observableArrayList());
    }

    public CompleteName prev() {
        currentName = namesList.get(--currentIndex);
        return currentName;
    }

    public boolean hasPrev() {
        return !(currentIndex == 0);
    }

    @Override
    protected void addNewRecording(CompleteRecording recording) {
        userAttempts.get(currentName).add(recording);
    }

    public ObservableList<CompleteRecording> getRecordingsForCurrentName() {
        return userAttempts.get(currentName);
    }



}
