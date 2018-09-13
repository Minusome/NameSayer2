package namesayer;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuScreenController implements Initializable {

    @FXML
    public JFXButton practiceButton;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void onPracticeModeClicked(MouseEvent mouseEvent) throws IOException {
        Scene scene = practiceButton.getScene();
        Parent root = FXMLLoader.load(getClass().getResource("/NameSelectScreen.fxml"));
        scene.setRoot(root);
    }
}
