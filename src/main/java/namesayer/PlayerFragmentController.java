package namesayer;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import namesayer.recording.Recording;
import org.controlsfx.control.Rating;

public class PlayerFragmentController {

    @FXML private Rating rating;
    private Recording recording;

    void injectRecording(Recording recording){
        this.recording = recording;
        rating.ratingProperty().bindBidirectional(recording.ratingProperty());
    }

    @FXML
    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        recording.playAudio();
    }

}
