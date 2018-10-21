package namesayer.view.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import namesayer.persist.NameStorageManager;
import namesayer.persist.NewDatabaseLoader;
import namesayer.session.Session;

import java.io.File;
import java.io.IOException;

import static namesayer.session.Session.SessionType.ASSESSMENT;
import static namesayer.session.Session.SessionType.PRACTISE;
import static namesayer.util.Screen.BROWSE_DATABASE_SCREEN;
import static namesayer.util.Screen.NAME_SELECT_SCREEN;
import static namesayer.util.Screen.STATS_SCREEN;
import static namesayer.util.Screen.MENU_HELP_SCREEN;


public class MenuScreenController {

    @FXML private JFXButton loadNewDataBaseButton;
    @FXML private JFXButton practiceButton;
    @FXML private JFXButton loadExistingDataBaseButton;
    @FXML private JFXButton browseButton;
    @FXML private GridPane parentPane;
    private JFXSnackbar bar;

    private NameStorageManager nameStorageManager = NameStorageManager.getInstance();

    public void initialize() {
        practiceButton.setDisable(false);
        bar = new JFXSnackbar(parentPane);
        bar.getStylesheets().addAll("/css/Material.css");
    }

    public void onPracticeModeClicked(MouseEvent mouseEvent) throws IOException {
        loadSelection(PRACTISE);
    }


    public void onAssessModeClicked(MouseEvent mouseEvent) throws IOException {
        loadSelection(ASSESSMENT);
    }

    public void loadSelection(Session.SessionType type) throws IOException {
        FXMLLoader loader =  NAME_SELECT_SCREEN.getLoader();
        practiceButton.getScene().setRoot(loader.load());
        NameSelectScreenController controller = loader.getController();
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

    public void loadNewDatabase(MouseEvent e) {
        //FileChooser chooser = new FileChooser();
        DirectoryChooser chooser= new DirectoryChooser();
        chooser.setTitle("Select folder containing recordings");
        //chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".wav", "*.wav"));
        //File selectedFile = chooser.showOpenDialog(browseButton.getScene().getWindow());
        File selectedFolder = chooser.showDialog(null);
        if (selectedFolder != null) {
            boolean succeed = (new NewDatabaseLoader().editFile(selectedFolder));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }

            if(succeed){
                bar.enqueue(new JFXSnackbar.SnackbarEvent("Successfully load folder: "+ selectedFolder.getName()));

            }else{
                bar.enqueue(new JFXSnackbar.SnackbarEvent("Can't load folder: " + selectedFolder.getName()));
            }

        }
    }

    public void onBrowseModeClicked(MouseEvent e) throws IOException {
        BROWSE_DATABASE_SCREEN.loadWithNode(practiceButton);
    }

    public void onStatisticsClicked(MouseEvent mouseEvent) throws IOException {
        STATS_SCREEN.loadWithNode(practiceButton);
    }
    
    public void onHelpButtonClicked(MouseEvent e) throws IOException {
    	MENU_HELP_SCREEN.loadWithNode(practiceButton);
    }
}
