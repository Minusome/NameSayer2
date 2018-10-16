package namesayer.session;

import javafx.collections.ObservableList;
import namesayer.model.CompositeRecording;

public class PractiseSession extends Session {

    public void prev() {
        currentName = namesList.get(--currentIndex);
    }

    public boolean hasPrev() {
        return !(currentIndex == 0);
    }

    public ObservableList<CompositeRecording> getRecordingsForCurrentName() {
        return currentName.getUserAttempts();
    }

    public void removeRecordingForCurrentName(CompositeRecording recording) {
        currentName.getUserAttempts().remove(recording);
    }
}
