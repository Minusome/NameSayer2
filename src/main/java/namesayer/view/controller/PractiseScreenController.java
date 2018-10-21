package namesayer.view.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import namesayer.model.CompositeRecording;
import namesayer.persist.StatsManager;
import namesayer.session.PractiseSession;
import namesayer.util.EmptySelectionModel;
import namesayer.util.SnackBarLoader;
import namesayer.view.alert.MicTestAlert;
import namesayer.view.cell.PractiseListCell;
import namesayer.view.alert.SaveAlert;

import static namesayer.util.TransitionFactory.Direction.LEFT;
import static namesayer.util.TransitionFactory.Direction.RIGHT;
import static namesayer.util.TransitionFactory.cardDoubleSlideTransition;


public class PractiseScreenController {


    @FXML private JFXButton micTestButton;
    @FXML private GridPane parentPane;
    @FXML private Label cardNumber;
    @FXML private JFXListView<CompositeRecording> practiseListView;
    @FXML private JFXSpinner recordingSpinner;
    @FXML private JFXButton nextButton;
    @FXML private JFXButton prevButton;
    @FXML private StackPane cardPane;
    @FXML private Label label;
    @FXML private JFXSpinner playingSpinner;

    private PractiseSession session;


    public void injectSession(PractiseSession session) {
        this.session = session;
        disableArrows(false);
        label.setText(session.getCurrentName().toString());
        practiseListView.setCellFactory(param -> new PractiseListCell(session));
        practiseListView.setSelectionModel(new EmptySelectionModel<>());
        Label label = new Label("Listen to the sample and make your own recording!");
        label.setFont(new Font(15));
        practiseListView.setPlaceholder(label);
        refreshList();
    }


    public void initialize() {
        playingSpinner.setProgress(1);
        recordingSpinner.setProgress(1);
    }


    private void loadNewCard(boolean isNext) {
        if (isNext) {
            session.next();
        } else {
            session.prev();
        }
        label.setText(session.getCurrentName().toString());
        disableArrows(false);
        refreshList();
    }


    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        session.getCurrentName().getExemplar().playAudio();
        disableArrows(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(playingSpinner.progressProperty(), 0)),
                new KeyFrame(
                        Duration.seconds(session.getCurrentName().getExemplar().getLength()),
                        event -> disableArrows(false),
                        new KeyValue(playingSpinner.progressProperty(), 1)
                )
        );
        timeline.play();
    }


    public void onRecordingButtonClicked(MouseEvent mouseEvent) {
        disableArrows(true);
        session.getCurrentName().makeNewRecording(event -> {
            disableArrows(false);
            refreshList();
        });
        recordingSpinner.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(recordingSpinner.progressProperty(), 0)),
                new KeyFrame(
                        Duration.seconds(session.getCurrentName().getExemplar().getLength()),
                        new KeyValue(recordingSpinner.progressProperty(), 1)
                )
        );
        timeline.play();
    }


    public void onNextButtonClicked(MouseEvent mouseEvent) {
        SequentialTransition transition = cardDoubleSlideTransition(cardPane, LEFT, event -> loadNewCard(true));
        transition.play();
    }


    public void onPrevButtonClicked(MouseEvent mouseEvent) {
        SequentialTransition transition = cardDoubleSlideTransition(cardPane, RIGHT, event -> loadNewCard(false));
        transition.play();
    }


    private void disableArrows(boolean disableArrows) {
        if (disableArrows) {
            nextButton.setDisable(true);
            prevButton.setDisable(true);
        } else {
            nextButton.setDisable(!session.hasNext());
            prevButton.setDisable(!session.hasPrev());
        }
    }


    public void refreshList() {
        ObservableList<CompositeRecording> recordings = session.getRecordingsForCurrentName();
        practiseListView.setItems(recordings);
        practiseListView.refresh();
        cardNumber.setText(session.getCurrentIndex() + 1 + "/" + session.getNumberOfNames());
        cardPane.requestFocus();
    }


    public void onBackButtonClicked(MouseEvent mouseEvent) {
        StatsManager.getInstance().save();
        SaveAlert saveAlert = new SaveAlert((Stage) parentPane.getScene().getWindow(), session);
        saveAlert.show();
    }


    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        session.saveUserRecordings();
        SnackBarLoader.displayMessage(parentPane, "Recordings have been saved");
    }

    public void onMicTestButtonClicked(MouseEvent mouseEvent) {
        MicTestAlert micTestAlert = new MicTestAlert((Stage) parentPane.getScene().getWindow());
        micTestAlert.show();
        micTestButton.setDisableVisualFocus(true);
    }
}