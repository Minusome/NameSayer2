package namesayer.session;

import javafx.collections.ObservableList;
import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;

import java.util.List;

public class PractiseSession extends Session {

    public PractiseSession(List<CompositeName> namesList) {
        super(namesList);
    }

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
