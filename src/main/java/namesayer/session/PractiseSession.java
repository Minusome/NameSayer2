package namesayer.session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import namesayer.model.CompositeRecording;
import namesayer.persist.NameStorageManager;

public class PractiseSession extends Session {

    public void prev() {
        currentName = namesList.get(--currentIndex);
    }

    public boolean hasPrev() {
        return !(currentIndex == 0);
    }

    public ObservableList<CompositeRecording> getRecordingsForCurrentName() {
        return FXCollections.unmodifiableObservableList(currentName.getUserAttempts());
    }

    public void removeRecordingForCurrentName(CompositeRecording recording) {
        currentName.getUserAttempts().remove(recording);
    }

    public void saveUserRecording() {
        NameStorageManager.getInstance().persistCompleteRecordingsForName(currentName);
    }

}
