package namesayer.view.controller;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import namesayer.persist.StatsManager;
import namesayer.session.AssessmentSession;
import namesayer.util.SnackBarLoader;
import namesayer.util.TransitionFactory;
import namesayer.view.alert.MicTestAlert;
import namesayer.view.controller.RewardCardController;
import namesayer.view.alert.SaveAlert;
import org.controlsfx.control.Rating;

import java.io.IOException;

import static namesayer.util.TransitionFactory.Direction.LEFT;

public class AssessmentScreenController {

    @FXML private JFXButton micTestButton;
    @FXML private GridPane parentPane;
    @FXML private JFXButton recordingButton;
    @FXML private Label cardNumber;
    @FXML private JFXButton replayButton;
    @FXML private JFXButton saveButton;
    @FXML private GridPane ratingCard;
    @FXML private Rating ratingStars;
    @FXML private JFXSpinner recordingSpinner;
    @FXML private JFXButton nextButton;
    @FXML private StackPane cardPane;
    @FXML private Label label;



    //Must set session when initializing this scene
    private AssessmentSession session;
    private DoubleProperty ratingProperty;
    private StatsManager statsManager = StatsManager.getInstance();
    private SaveAlert saveAlertCache;
    private MicTestAlert micTestAlertCache;

    public void injectSession(AssessmentSession session) {
        this.session = session;
        label.setText(session.getCurrentName().toString());
        if (session.hasUserMadeRecording()) {
            reBindProperties();
            disableButtons(false, true);
        } else {
            disableButtons(true, true);
        }
        refreshCardNumber();

    }

    public void initialize() {
        recordingSpinner.setProgress(1);
        recordingButton.setFocusTraversable(false);
    }


    public void onNextButtonClicked(MouseEvent mouseEvent) {
        TransitionFactory.cardDoubleSlideTransition(cardPane, LEFT, event -> loadNewCard()).play();
        TransitionFactory.slideDownTransition(ratingCard).play();
    }

    //enable loading cards backwards and forwards
    //disable the button if its the last card
    private void loadNewCard() {
        int ratingValue = (int) Math.round(ratingStars.getRating());
        statsManager.updateRatingFreq(ratingValue);
        statsManager.updateDifficultName(session.getCurrentName(), ratingValue);
        if (session.getCurrentIndex() + 1 == session.getNumberOfNames()) {
            session.resetToFirst();
            statsManager.updateAvgAssessRating(session.getAverageRating());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RewardCard.fxml"));
            Parent root = null;
            try {
                root = loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            RewardCardController controller = loader.getController();
            controller.setRating(session.getAverageRating());
            cardPane.getChildren().setAll(root);
        } else {
            session.next();
            label.setText(session.getCurrentName().toString());
            refreshCardNumber();
        }
        disableButtons(true, true);
    }


    public void onRecordingButtonClicked(MouseEvent mouseEvent) {
        if (!session.hasUserMadeRecording()) {
            disableButtons(true, true);
            session.getCurrentName().makeNewRecording(event -> reBindProperties());
            recordingSpinner.setVisible(true);
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0), new KeyValue(recordingSpinner.progressProperty(), 0)),
                    new KeyFrame(
                            Duration.seconds(session.getCurrentName().getExemplar().getLength()),
                            event -> disableButtons(false, true),
                            new KeyValue(recordingSpinner.progressProperty(), 1)
                    )
            );
            timeline.play();
        } else {
            SnackBarLoader.displayMessage(parentPane, "Only a single recording is made in assessment mode");
        }
    }


    public void onReplayButtonClicked(MouseEvent mouseEvent) {
        if (session.hasUserMadeRecording()) {
            session.compareUserAttemptWithExemplar(event -> {
                TransitionFactory.slideUpTransition(ratingCard).play();
                disableButtons(false, false);
            });
        }
    }


    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        session.saveUserRecordings();
        saveButton.setDisable(true);
        SnackBarLoader.displayMessage(parentPane, "Recording is saved");
    }


    private void disableButtons(boolean disableButtons, boolean disableArrows) {
        if (disableArrows || (!session.hasNext() && session.getCurrentIndex() < session.getNumberOfNames() - 1)) {
            nextButton.setDisable(true);
        } else {
            nextButton.setDisable(!session.hasUserMadeRecording());
        }
        replayButton.setDisable(disableButtons);
        saveButton.setDisable(disableButtons);
    }

    public void refreshCardNumber() {
        cardPane.requestFocus();
        cardNumber.setText(session.getCurrentIndex() + 1 + "/" + session.getNumberOfNames());
    }


    public void reBindProperties() {
        if (ratingProperty != null) {
            ratingStars.ratingProperty().unbindBidirectional(ratingProperty);
        }
        ratingProperty = session.getCurrentRecording().ratingProperty();
        ratingStars.ratingProperty().bindBidirectional(ratingProperty);
    }

    public void onBackButtonClicked(MouseEvent mouseEvent) {
        if (saveAlertCache == null){
            saveAlertCache = new SaveAlert((Stage) parentPane.getScene().getWindow(), session);
        }
        saveAlertCache.show();
        StatsManager.getInstance().save();
    }

    public void onMicTestButtonClicked() {
        if (micTestAlertCache == null) {
            micTestAlertCache = new MicTestAlert((Stage) parentPane.getScene().getWindow());
        }
        micTestAlertCache.show();
        micTestButton.setDisableVisualFocus(true);
    }
}
