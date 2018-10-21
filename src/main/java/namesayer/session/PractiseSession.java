package namesayer.session;

import javafx.collections.ObservableList;
import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;

import java.util.Collections;
import java.util.List;

public class PractiseSession extends Session {

    public PractiseSession(List<CompositeName> namesList) {
        super(namesList);
        this.type = SessionType.PRACTISE;
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

    public List<CompositeName> getSuggestions() {
        return Collections.unmodifiableList(namesList);
    }

    public boolean jumpTo(String text) {
        int i = 0;
        for (CompositeName name : namesList) {
            if (name.toString().toLowerCase().equals(text.toLowerCase())) {
                currentName = name;
                currentIndex = i;
                return true;
            }
            i++;
        }
        return false;
    }
}
