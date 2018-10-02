package namesayer;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import namesayer.view.TransitionFactory;

import static namesayer.view.TransitionFactory.Direction.*;

import java.io.IOException;

public class AssessmentScreenController {

    @FXML private StackPane cardPane;

    public void initialize() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AssessmentCard.fxml"));
        cardPane.getChildren().addAll(root);
    }


    public void onPrevButtonClicked(MouseEvent mouseEvent) {
        SequentialTransition transition = TransitionFactory.cardDoubleSlideTransition(cardPane, RIGHT, event -> loadNewCard());
        transition.play();
    }


    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
        SequentialTransition transition = TransitionFactory.cardDoubleSlideTransition(cardPane, LEFT, event -> loadNewCard());
        transition.play();
    }

    public void loadNewCard() {
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("/AssessmentCard.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        cardPane.getChildren().setAll(root);
    }

}
