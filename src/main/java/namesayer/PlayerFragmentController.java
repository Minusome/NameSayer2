package namesayer;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import namesayer.recording.Recording;

public class PlayerFragmentController {

    private Recording recording;

    void injectRecording(Recording recording){
        this.recording = recording;
    }

    @FXML
    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        recording.playAudio();
    }

}
