package namesayer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompositeName extends Name {

    /**
     * These recordings are permanently associated with this name
     * NameStorageManager accesses this list, any other class will only be able to see an empty list
     */
    private ObservableList<CompositeRecording> userAttempts = FXCollections.observableArrayList();
    private Exemplar exemplar;

    public CompositeName(String name) {
        super(name);
    }

    public void addUserAttempt(CompositeRecording recording) {
        userAttempts.add(recording);
    }

    public ObservableList<CompositeRecording> getUserAttempts() {
        return userAttempts;
    }

    public Exemplar getExemplar() {
        return exemplar;
    }

    public void setExemplar(Exemplar exemplar) {
        this.exemplar = exemplar;
    }



}
