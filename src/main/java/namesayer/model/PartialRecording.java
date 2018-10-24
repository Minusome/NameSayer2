package namesayer.model;

import java.nio.file.Path;

/**
 *  Represents the audio file of a PartialName
 */

public class PartialRecording extends Recording {

    private boolean isBadQuality = false;

    public boolean isBadQuality() {
        return isBadQuality;
    }

    public void setBadQuality(boolean badQuality) {
        isBadQuality = badQuality;
    }

    public PartialRecording(Path recordingPath) {
        super(recordingPath);
    }

}
