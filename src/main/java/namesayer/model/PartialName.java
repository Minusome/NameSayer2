package namesayer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PartialName extends Name{

    private ObservableList<PartialRecording> partialRecordings = FXCollections.observableArrayList();

    public PartialName(String name) {
        super(name);
    }

    public void addRecording(PartialRecording recording) {
        partialRecordings.add(recording);
    }

    public ObservableList<PartialRecording> getRecordings() {
        return partialRecordings;
    }



}
