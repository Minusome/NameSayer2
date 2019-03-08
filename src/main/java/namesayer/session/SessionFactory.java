package namesayer.session;

import namesayer.model.CompositeName;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Creates sessions at runtime.
 * Populates the session with names.
 */

public class SessionFactory {

    private List<CompositeName> namesList = new ArrayList<>();

    public void addName(CompositeName compositeName) {
        namesList.add(compositeName);
    }

    public void removeName(CompositeName compositeName) {
        namesList.remove(compositeName);
    }

    public void randomize() {
        Collections.shuffle(namesList);
    }

    public AssessmentSession makeAssessmentSession() {
        return new AssessmentSession(namesList);
    }

    public PractiseSession makePractiseSession() {
        return new PractiseSession(namesList);
    }


}
