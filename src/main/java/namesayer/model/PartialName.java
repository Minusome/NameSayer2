package namesayer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * PartialNames are those which came bundled with the Names Corpus DB.
 */

public class PartialName extends Name {

    //Ony-to-many relationship PartialNames and PartialRecordings
    private List<PartialRecording> partialRecordings = new ArrayList<>();

    public PartialName(String name) {
        super(name);
    }

    public void addRecording(PartialRecording recording) {
        partialRecordings.add(recording);
    }

    public List<PartialRecording> getRecordings() {
        return partialRecordings;
    }


}
