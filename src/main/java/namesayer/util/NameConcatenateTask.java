package namesayer.util;

import javafx.concurrent.Task;
import namesayer.model.*;
import namesayer.persist.NameStorageManager;
import namesayer.session.Session;

import java.util.ArrayList;
import java.util.List;


/**
 * Parses user requested names into CompleteNames where available
 */
public class NameConcatenateTask extends Task<Void> {

    private String userRequestedName;
    private Session session;

    public NameConcatenateTask(Session session, String userRequestedNames) {
        this.userRequestedName = userRequestedNames;
        this.session = session;
    }


    /**
     * Invoked when the Task is executed, the call method must be overridden and
     * implemented by subclasses. The call method actually performs the
     * background thread logic. Only the updateProgress, updateMessage, updateValue and
     * updateTitle methods of Task may be called from code within this method.
     * Any other interaction with the Task from the background thread will result
     * in runtime exceptions.
     *
     * @return The result of the background work, if any.
     * @throws Exception an unhandled exception which occurred during the
     *                   background operation
     */
    @Override
    protected Void call() throws Exception {
        NameStorageManager manager = NameStorageManager.getInstance();
        String[] components = userRequestedName.split("[\\s-]+");
        List<PartialRecording> discovered = new ArrayList<>();
        for (String s : components) {
            PartialName name = manager.findPartialNameFromString(s);
            if (name != null) {
                //TODO just getting first recording for now, change to quality
                discovered.add(name.getRecordings().get(0));
            }
        }
        if (discovered.size() == 0) {
            return null;
        }
        CompositeName compositeName = new CompositeName(userRequestedName);
        compositeName.setExemplar(new Exemplar(discovered));
        session.addName(compositeName);
        return null;
    }


}
