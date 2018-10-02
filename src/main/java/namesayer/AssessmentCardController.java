package namesayer;

import com.jfoenix.controls.JFXSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import namesayer.model.Recording;

public class AssessmentCardController {


    @FXML private Label label;
    @FXML private JFXSpinner spinner;
    private Recording recording;

    public void injectRecording(Recording recording, String name) {
        this.recording = recording;
        spinner.setProgress(1);
        label.setText(name);
    }

    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        recording.playAudio();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(spinner.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(recording.getLength()), new KeyValue(spinner.progressProperty(), 1)));
        timeline.play();
    }
}
