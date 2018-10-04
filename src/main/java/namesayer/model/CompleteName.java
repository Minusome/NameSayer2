package namesayer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CompleteName extends Name{

    private ObservableList<CompleteRecording> completeRecordings = FXCollections.observableArrayList();

    private CompleteRecording exemplar;

    public CompleteName(String name) {
        super(name);
    }

    public void addRecording(CompleteRecording recording) {
        completeRecordings.add(recording);
    }

    public List<CompleteRecording> getRecordings() {
        return completeRecordings;
    }

    public CompleteRecording getExemplar() {
        return exemplar;
    }

    public void setExemplar(CompleteRecording exemplar) {
        this.exemplar = exemplar;
    }

}
