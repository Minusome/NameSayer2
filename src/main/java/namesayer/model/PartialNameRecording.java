package namesayer.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.nio.file.Path;

public class PartialNameRecording extends Recording {

    private BooleanProperty isBadQuality = new SimpleBooleanProperty(false);

    public PartialNameRecording(Path recordingPath) {
        super(recordingPath);
    }

    public boolean getIsBadQuality() {
        return isBadQuality.get();
    }

    public BooleanProperty isBadQualityProperty() {
        return isBadQuality;
    }

    public void setIsBadQuality(boolean isBadQuality) {
        this.isBadQuality.set(isBadQuality);
    }

}
