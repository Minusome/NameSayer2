package namesayer;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import namesayer.model.CompleteName;
import namesayer.model.Recording;
import namesayer.persist.NameStorageManager;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static namesayer.view.TransitionFactory.Direction.LEFT;
import static namesayer.view.TransitionFactory.cardDoubleSlideTransition;

public class AssessmentScreenController {

    @FXML private JFXSpinner recordingSpinner;
    @FXML private JFXButton nextButton;
    @FXML private StackPane cardPane;
    @FXML private Label label;
    @FXML private JFXSpinner playingSpinner;


    private Recording recording;
    private CompleteName name;
    private List<CompleteName> completeNames;
    private int listIndex;
    private boolean recordingComplete = false;

    public void initialize() throws IOException {
        completeNames = NameStorageManager.getInstance().getCompleteNames();
        listIndex = -1;
        playingSpinner.setProgress(1);
        loadNewCard(true);
    }


    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
        SequentialTransition transition = cardDoubleSlideTransition(cardPane, LEFT, event -> loadNewCard(true));
        transition.play();
    }

    //enable loading cards backwards and forwards
    //disable the button if its the last card
    private void loadNewCard(boolean isNext) {
        name = (isNext) ? completeNames.get(++listIndex) : completeNames.get(--listIndex);
        nextButton.setDisable(listIndex == completeNames.size() - 1);
        label.setText(name.getName());
        recording = name.getExemplar();
    }

    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        recording.playAudio();
        boolean isNextDisable = nextButton.isDisable();
        nextButton.setDisable(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(playingSpinner.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(recording.getLength()), event -> {
                    nextButton.setDisable(isNextDisable);
                }, new KeyValue(playingSpinner.progressProperty(), 1)));
        timeline.play();
    }

    public void onRecordingButtonClicked(MouseEvent mouseEvent) {
        if (recordingComplete) {
            JFXAlert alert = new JFXAlert((Stage) cardPane.getScene().getWindow());
            alert.initModality(Modality.NONE);
            alert.setOverlayClose(false);
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setHeading(new Label("Warning"));
            layout.setBody(new Label("Are you sure you want to overwrite your previous recording?"));
            JFXButton closeButton = new JFXButton("Cancel");
            JFXButton okButton = new JFXButton("Start Recording");
            closeButton.getStyleClass().add("dialog-accept");
            okButton.getStyleClass().add("dialog-accept");
            closeButton.setOnAction(event -> {
                alert.hideWithAnimation();
            });
            okButton.setOnAction(event -> {
                name.deleteAllTempRecording();
                startRecording();
                alert.hideWithAnimation();
            });
            layout.setActions(closeButton, okButton);
            alert.setContent(layout);
            alert.show();
        } else {
            startRecording();
        }
    }

    public void startRecording() {
        String temp = "Recording on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String recordingName = temp.replace(" ", "_");
        name.makeNewTempRecording(recordingName);
        recordingSpinner.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(recordingSpinner.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(recording.getLength()), event -> recordingSpinner.setVisible(false), new KeyValue(recordingSpinner.progressProperty(), 1)));
        timeline.play();
        recordingComplete = true;
    }


    public void onReplayButtonClicked(MouseEvent mouseEvent) {
        if (recordingComplete) {
            name.compareUserAttemptWithExemplar();
        }
    }

    public void onEditButtonClicked(MouseEvent mouseEvent) {

    }

    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        name.saveTempRecordings();
    }
}
