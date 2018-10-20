package namesayer;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import namesayer.persist.NameStorageManager;
import namesayer.session.Session;

import java.io.IOException;

import static namesayer.session.Session.SessionType.ASSESSMENT;
import static namesayer.session.Session.SessionType.PRACTISE;
import static namesayer.util.Screen.BROWSE_DATABASE_SCREEN;
import static namesayer.util.Screen.NAME_SELECT_SCREEN;
import static namesayer.util.Screen.STATS_SCREEN;
import static namesayer.util.Screen.HELP_SCREEN;


public class MenuScreenController {

    @FXML private JFXButton loadNewDataBaseButton;
    @FXML private JFXButton practiceButton;
    @FXML private JFXButton loadExistingDataBaseButton;
    @FXML private JFXButton browseButton;

    private NameStorageManager nameStorageManager = NameStorageManager.getInstance();

    public void initialize() {
        practiceButton.setDisable(false);
    }

    public void onPracticeModeClicked(MouseEvent mouseEvent) throws IOException {
        loadSelection(PRACTISE);
    }


    public void onAssessModeClicked(MouseEvent mouseEvent) throws IOException {
        loadSelection(ASSESSMENT);
    }

    public void loadSelection(Session.SessionType type) throws IOException {
        NAME_SELECT_SCREEN.loadWithNode(practiceButton);
        NameSelectScreenController controller = NAME_SELECT_SCREEN.getController();
        controller.setSessionType(type);
    }

//    //imports the files hierarchy
//    public void onSelectLoadPreviousFolder(MouseEvent mouseEvent) {
//        DirectoryChooser chooser = new DirectoryChooser();
//        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
//        chooser.setTitle("Select the existing database for your names");
//        File selectedDirectory = chooser.showDialog(practiceButton.getScene().getWindow());
//        if (selectedDirectory != null) {
//            NameStorageManager storageManager = NameStorageManager.getInstance();
//            storageManager.clear();
//            storageManager.loadExistingHierarchy(selectedDirectory.toPath(), practiceButton);
//            loadNewDataBaseButton.setDisable(true);
//            loadExistingDataBaseButton.setDisable(true);
//        }
//    }

    //    public void onSelectAudioDatabaseFolder(MouseEvent mouseEvent) {
//        DirectoryChooser chooser = new DirectoryChooser();
//        chooser.setTitle("Select the audio database for your names");
//        File selectedDirectory = chooser.showDialog(practiceButton.getScene().getWindow());
//        if (selectedDirectory != null) {
//            NameStorageManager storageManager = NameStorageManager.getInstance();
//            storageManager.clear();
//            storageManager.initialize(selectedDirectory.toPath(), practiceButton);
//            loadNewDataBaseButton.setDisable(true);
//            loadExistingDataBaseButton.setDisable(true);
//            practiceButton.setDisable(false);
//        }
//    }
    public void onBrowseModeClicked(MouseEvent e) throws IOException {
        BROWSE_DATABASE_SCREEN.loadWithNode(practiceButton);
    }

    public void onStatisticsClicked(MouseEvent mouseEvent) throws IOException {
        STATS_SCREEN.loadWithNode(practiceButton);
    }
    
    public void onHelpButtonClicked(MouseEvent e) throws IOException {
    	HELP_SCREEN.loadWithNode(practiceButton);
    }
}
