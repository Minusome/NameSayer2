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
 * A task which parses user requested names into CompositeNames where available
 */

public class NameConcatenateTask extends Task<Void> {

    private String userRequestedName;
    private SessionFactory sessionFactory;

    public NameConcatenateTask(SessionFactory session, String userRequestedNames) {
        this.userRequestedName = userRequestedNames;
        this.sessionFactory = session;
    }


    @Override
    protected Void call() {
        NameStorageManager manager = NameStorageManager.getInstance();
        String[] components = userRequestedName.split("[\\s-]+");
        List<PartialRecording> discovered = new ArrayList<>();
        for (String s : components) {
            PartialName name = manager.findPartialNameFromString(s);
            if (name != null) {
                List<PartialRecording> recordings = name.getRecordings();
                PartialRecording selected = null;
                for (PartialRecording r : recordings) {
                    if (!r.isBadQuality()){
                        selected = r;
                        break;
                    }
                }
                //TODO just getting first recording for now, change to quality
                discovered.add((selected != null) ? selected : recordings.get(0));
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
