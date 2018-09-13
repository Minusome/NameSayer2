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
    public List<String> getListOfNames() {
        //dummy values for now
        return new ArrayList<String>() {{
            add("test1");
            add("test2");
            add("test3");
            add("test4");
            add("test5");
            add("test6");
            add("test1");
            add("test2");
            add("test3");
            add("test4");
            add("test5");
            add("test6");
            add("test1");
            add("test2");
            add("test3");
            add("test4");
            add("test5");
            add("test6");
            add("test1");
            add("test2");
            add("test3");
            add("test4");
            add("test5");
            add("test6");

        }};
    }
}
