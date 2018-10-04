package namesayer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class CompleteName extends Name {

    /**
     * These recordings are permanently associated with this name
     * NameStorageManager accesses this list, any other class will only be able to see an empty list
     */
    private ObservableList<CompleteRecording> persistentRecordings = FXCollections.observableArrayList();

    private CompleteRecording exemplar;

    public CompleteName(String name) {
        super(name);
    }

    public void persistRecording(CompleteRecording recording) {
        persistentRecordings.add(recording);
    }

    public List<CompleteRecording> getPersistentRecordings() {
        return persistentRecordings;
    }

    public CompleteRecording getExemplar() {
        return exemplar;
    }

    public void setExemplar(CompleteRecording exemplar) {
        this.exemplar = exemplar;
    }

}
