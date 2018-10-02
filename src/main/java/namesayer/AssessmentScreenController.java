package namesayer;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class AssessmentScreenController {

    @FXML private Pane cardPane;

    public void initialize() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/AssessmentCard.fxml"));
        cardPane.getChildren().addAll(root);
    }


}
