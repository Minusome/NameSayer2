package namesayer.util;

import javafx.concurrent.Task;
import namesayer.model.CompositeName;
import namesayer.model.Exemplar;
import namesayer.model.PartialName;
import namesayer.model.PartialRecording;
import namesayer.persist.NameStorageManager;
import namesayer.session.SessionFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Parses user requested names into CompleteNames where available
 */
public class NameConcatenateTask extends Task<Void> {

    private String userRequestedName;
    private SessionFactory sessionFactory;

    public NameConcatenateTask(SessionFactory session, String userRequestedNames) {
        this.userRequestedName = userRequestedNames;
        this.sessionFactory = session;
    }


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
        sessionFactory.addName(compositeName);
        return null;
    }


}
