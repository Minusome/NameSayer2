package namesayer;

import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import namesayer.model.CompleteName;
import namesayer.util.NameStorageManager;
import namesayer.view.TransitionFactory;

import java.io.IOException;
import java.util.Iterator;

import static namesayer.view.TransitionFactory.Direction.LEFT;
import static namesayer.view.TransitionFactory.Direction.RIGHT;

public class AssessmentScreenController {

    @FXML private StackPane cardPane;
    private Iterator<CompleteName> iterator;

    public void initialize() throws IOException {
        iterator = NameStorageManager.getInstance().getCompleteNames().iterator();
        loadNewCard();
    }


    public void onPrevButtonClicked(MouseEvent mouseEvent) {
        SequentialTransition transition = TransitionFactory.cardDoubleSlideTransition(cardPane, RIGHT, event -> loadNewCard());
        transition.play();
    }


    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
        SequentialTransition transition = TransitionFactory.cardDoubleSlideTransition(cardPane, LEFT, event -> loadNewCard());
        transition.play();
    }

    //enable loading cards backwards and fowards
    //disable the button if its the last card
    private void loadNewCard() {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AssessmentCard.fxml"));
            root = loader.load();
            AssessmentCardController controller = loader.getController();
            CompleteName name = iterator.next();
            controller.injectRecording(name.getExemplar(), name.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        cardPane.getChildren().setAll(root);
    }

}
