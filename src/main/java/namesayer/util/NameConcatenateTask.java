package namesayer.util;

import javafx.concurrent.Task;
import namesayer.model.CompleteName;

import java.util.List;

/**
 * Parses user requested names into CompleteNames where available
 */
public class NameConcatenateTask extends Task<CompleteName> {

    private String userRequestedNames;

    public NameConcatenateTask(String  userRequestedNames) {
        this.userRequestedNames = userRequestedNames;
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
    protected CompleteName call() throws Exception {
        return null;
    }
}
