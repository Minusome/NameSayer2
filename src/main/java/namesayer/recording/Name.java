package namesayer.recording;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Name {

    private String name;
    private Path directory;
    private BooleanProperty selected = new SimpleBooleanProperty(false);
    private List<Recording> savedRecordings = new ArrayList<>();
    private List<Recording> tempRecordings = new ArrayList<>();

    public Name(String name, Path directory) {
        this.name = name;
        this.directory = directory;
    }

    public void makeNewRecording() {

    }

    public void addSavedRecording(Recording recording) {
        savedRecordings.add(recording);
    }


    public List<Recording> getSavedRecordings() {
        return Collections.unmodifiableList(savedRecordings);
    }

    public List<Recording> getTempRecordings() {
        return Collections.unmodifiableList(tempRecordings);
    }


    public void saveTempRecordings() {

    }

    public boolean getSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public String getName() {
        return name;
    }

    public Path getDirectory() {
        return directory;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Name name1 = (Name) o;

        return name.equals(name1.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
