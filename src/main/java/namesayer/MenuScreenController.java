package namesayer;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import namesayer.recording.NameStorageManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuScreenController implements Initializable {

    @FXML private JFXButton practiceButton;
    private boolean isDirectorySelected = false;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void onPracticeModeClicked(MouseEvent mouseEvent) throws IOException {
        Scene scene = practiceButton.getScene();
        Parent root = FXMLLoader.load(getClass().getResource("/NameSelectScreen.fxml"));
        scene.setRoot(root);
    }


    public void onSelectAudioDatabaseFolder(MouseEvent mouseEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select the audio database for your names");
        File selectedDirectory = chooser.showDialog(practiceButton.getScene().getWindow());
        if (selectedDirectory != null){
            NameStorageManager storageManager = NameStorageManager.getInstance();
            storageManager.initialize(selectedDirectory.toPath());
            isDirectorySelected = true;
        }
    }
}
