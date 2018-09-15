package namesayer.recording;

import java.util.ArrayList;
import java.util.List;

public class RecordingManager {

    private static RecordingManager instance = null;

    public static RecordingManager getInstance() {
        if (instance == null) {
            instance = new RecordingManager();
        }
        return instance;
    }

    private RecordingManager() {
        //make folders and stuff
    }

    //return a list of all the names
    public List<Name> getListOfNames() {
        List<Name> names = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            names.add(new Name("test" + i));
        }
        return names;
    }
}
