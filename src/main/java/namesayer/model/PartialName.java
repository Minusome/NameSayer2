package namesayer.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;

public class PartialName extends Name{

    private ObservableList<PartialNameRecording> recordings = FXCollections.observableArrayList();

    public PartialName(String name, Path directory) {
        super(name, directory);
    }


}
