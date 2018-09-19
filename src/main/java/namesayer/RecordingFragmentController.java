package namesayer;

import javafx.scene.input.MouseEvent;
import namesayer.recording.Name;

public class RecordingFragmentController {

    private Name name;

    void injectName(Name name) {
        this.name = name;
    }

    public void onRecordingButtonClicked(MouseEvent mouseEvent) {
        name.makeNewRecording();
    }

}
