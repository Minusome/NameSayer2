package namesayer;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import namesayer.persist.AssessmentSession;
import namesayer.view.TransitionFactory;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static namesayer.view.TransitionFactory.Direction.LEFT;
import static namesayer.view.TransitionFactory.cardDoubleSlideTransition;

public class AssessmentScreenController {

    @FXML private JFXButton replayButton;
    @FXML private JFXButton ratingButton;
    @FXML private JFXButton saveButton;
    @FXML private GridPane ratingCard;
    @FXML private Rating rating;
    @FXML private JFXSpinner recordingSpinner;
    @FXML private JFXButton nextButton;
    @FXML private StackPane cardPane;
    @FXML private Label label;
    @FXML private JFXSpinner playingSpinner;

    //Must set session when initializing this scene
    private AssessmentSession session;

    private boolean recordingComplete = false;
    private boolean editButtonToggle = false;

    public void injectSession(AssessmentSession session) {
        this.session = session;
        label.setText(session.getCurrentName().toString());
        disableButtons(true, false);
    }

    public void initialize() {
        playingSpinner.setProgress(1);
        recordingSpinner.setProgress(1);
    }


    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
        SequentialTransition transition = cardDoubleSlideTransition(cardPane, LEFT, event -> loadNewCard());
        transition.play();
        disableButtons(true, false);
    }

    //enable loading cards backwards and forwards
    //disable the button if its the last card
    private void loadNewCard() {
        session.next();
        label.setText(session.getCurrentName().toString());
        nextButton.setDisable(!session.hasNext());
    }

    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        session.getExemplar().playAudio();
        disableButtons(true, true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(playingSpinner.progressProperty(), 0)),
                new KeyFrame(
                        Duration.seconds(session.getExemplar().getLength()),
                        event -> disableButtons(false, false),
                        new KeyValue(playingSpinner.progressProperty(), 1)
                )
        );
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
        disableButtons(true, true);
        String temp = "Recording on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String recordingName = temp.replace(" ", "_");
        session.makeNewRecording(
                recordingName,
                event -> rating.ratingProperty().bindBidirectional(session.getCurrentRecording().ratingProperty())
        );
        recordingSpinner.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(recordingSpinner.progressProperty(), 0)),
                new KeyFrame(
                        Duration.seconds(session.getExemplar().getLength()),
                        event -> disableButtons(false, false),
                        new KeyValue(recordingSpinner.progressProperty(), 1)
                )
        );
        timeline.play();
        rating.ratingProperty().unbind();
        recordingComplete = true;
    }


    public void onReplayButtonClicked(MouseEvent mouseEvent) {
        if (recordingComplete) {
            session.compareUserAttemptWithExemplar();
        }
    }


    public void onEditButtonClicked(MouseEvent mouseEvent) {
        TranslateTransition animation;
        if (!editButtonToggle) {
            animation = TransitionFactory.slideUpTransition(ratingCard);
            editButtonToggle = true;
        } else {
            animation = TransitionFactory.slideDownTransition(ratingCard);
            editButtonToggle = false;
        }
        animation.play();
    }

    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        session.saveUserRecording();
    }


    private void disableButtons(boolean disableButtons, boolean disableArrows) {
        if (disableArrows) {
            nextButton.setDisable(true);
        } else {
            nextButton.setDisable(!session.hasNext() || !recordingComplete);
        }
        replayButton.setDisable(disableButtons);
        ratingButton.setDisable(disableButtons);
        saveButton.setDisable(disableButtons);
    }


}
