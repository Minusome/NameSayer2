package namesayer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class PartialName extends Name {

    private List<PartialRecording> partialRecordings = new ArrayList<>();

    public PartialName(String name) {
        super(name);
    }

    public void addRecording(PartialRecording recording) {
        partialRecordings.add(recording);
    }

    public ObservableList<PartialRecording> getRecordings() {
        
        return FXCollections.observableArrayList(partialRecordings);
    }


}
