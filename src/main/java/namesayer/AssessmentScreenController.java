package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import namesayer.model.CompleteName;
import namesayer.model.Recording;
import namesayer.util.NameStorageManager;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import static namesayer.view.TransitionFactory.Direction.LEFT;
import static namesayer.view.TransitionFactory.Direction.RIGHT;
import static namesayer.view.TransitionFactory.cardDoubleSlideTransition;

public class AssessmentScreenController {

    @FXML private JFXButton prevButton;
    @FXML private JFXButton nextButton;
    @FXML private StackPane cardPane;
    @FXML private Label label;
    @FXML private JFXSpinner spinner;
    private Recording recording;
    private ListIterator<CompleteName> iterator;
    private int listSize;

    public void initialize() throws IOException {
        List<CompleteName> names = NameStorageManager.getInstance().getCompleteNames();
        iterator = names.listIterator();
        listSize = names.size();
        spinner.setProgress(1);
        loadNewCard(true);
    }


    public void onPrevButtonClicked(MouseEvent mouseEvent) {
        SequentialTransition transition = cardDoubleSlideTransition(cardPane, RIGHT, event -> loadNewCard(false));
        transition.play();
    }


    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
        SequentialTransition transition = cardDoubleSlideTransition(cardPane, LEFT, event -> loadNewCard(true));
        transition.play();
    }

    //enable loading cards backwards and forwards
    //disable the button if its the last card
    private void loadNewCard(boolean isNext) {
        CompleteName name = (isNext) ?  iterator.next(): iterator.previous();
        if (isNext) {
            prevButton.setDisable(iterator.previousIndex() == 0);
            nextButton.setDisable(iterator.nextIndex() == listSize);
        } else {
            prevButton.setDisable(iterator.previousIndex() == -1);
            nextButton.setDisable(iterator.nextIndex() == listSize - 1);
        }

        label.setText(name.getName());
        recording = name.getExemplar();
    }

    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        recording.playAudio();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(spinner.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(recording.getLength()), new KeyValue(spinner.progressProperty(), 1)));
        timeline.play();
    }
}
