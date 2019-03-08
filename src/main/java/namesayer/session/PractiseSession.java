package namesayer.session;

import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;

import java.util.Collections;
import java.util.List;

/**
 * An instance of a Practice Mode - in progress or saved as File
 */

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

    /**
     * Deletes a recording which the user created
     *
     * @param recording To be deleted
     */
    public void removeRecordingForCurrentName(CompositeRecording recording) {
        currentName.getUserAttempts().remove(recording);
    }

    /**
     * Unmodifiable version of the list to populate  auto-suggestions
     */
    public List<CompositeName> getSuggestions() {
        return Collections.unmodifiableList(namesList);
    }

    /**
     * Allows skipping of names for ease of navigation
     *
     * @param text Name to skip to
     * @return true if names is found, otherwise false
     */
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
