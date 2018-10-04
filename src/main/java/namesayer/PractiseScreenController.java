package namesayer;

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
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import namesayer.model.CompleteName;
import namesayer.model.CompleteRecording;
import namesayer.persist.PractiseSession;
import namesayer.view.EmptySelectionModel;
import namesayer.view.PractiseListCell;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static namesayer.view.TransitionFactory.Direction.LEFT;
import static namesayer.view.TransitionFactory.Direction.RIGHT;
import static namesayer.view.TransitionFactory.cardDoubleSlideTransition;

public class PractiseScreenController {

    @FXML private JFXListView<CompleteRecording> practiseListView;
    @FXML private JFXSpinner recordingSpinner;
    @FXML private JFXButton nextButton;
    @FXML private JFXButton prevButton;
    @FXML private StackPane cardPane;
    @FXML private Label label;
    @FXML private JFXSpinner playingSpinner;

    PractiseSession session;

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
        CompleteName name = (isNext) ? session.next() : session.prev();
        label.setText(name.toString());
        disableArrows(false);
        refreshList();
    }


    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        session.getExemplar().playAudio();
        disableArrows(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(playingSpinner.progressProperty(), 0)),
                new KeyFrame(
                        Duration.seconds(session.getExemplar().getLength()),
                        event -> disableArrows(false),
                        new KeyValue(playingSpinner.progressProperty(), 1)
                )
        );
        timeline.play();
    }


    public void onRecordingButtonClicked(MouseEvent mouseEvent) {
        disableArrows(true);
        String temp = "Recording on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String recordingName = temp.replace(" ", "_");
        session.makeNewRecording(
                recordingName,
                event -> {
                }
        );
        recordingSpinner.setVisible(true);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(recordingSpinner.progressProperty(), 0)),
                new KeyFrame(
                        Duration.seconds(session.getExemplar().getLength()),
                        event -> {
                            disableArrows(false);
                            refreshList();
                        },
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
        ObservableList<CompleteRecording> recordings = session.getRecordingsForCurrentName();
        for (CompleteRecording recording : recordings) {
            System.out.println(recording);
        }
        practiseListView.setItems(recordings);
        practiseListView.refresh();
        cardPane.requestFocus();
    }
}
