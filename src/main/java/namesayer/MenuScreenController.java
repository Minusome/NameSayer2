package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import namesayer.persist.NameStorageManager;
import namesayer.session.Session;
//import namesayer.persist.NameStorageManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;

import static namesayer.session.Session.SessionType.ASSESSMENT;
import static namesayer.session.Session.SessionType.PRACTISE;

public class MenuScreenController{

    @FXML public JFXProgressBar MicrophoneVolume;
    @FXML public ImageView MicrophoneButton;
    @FXML private JFXButton loadNewDataBaseButton;
    @FXML private JFXButton practiceButton;
    @FXML private JFXButton loadExistingDataBaseButton;
    @FXML private JFXButton browseButton;
    private boolean isFirstTimeClickMic = true;
    private NameStorageManager nameStorageManager = NameStorageManager.getInstance();
    @FXML private ImageView microphoneTestingButton;


    public void initialize() {
        practiceButton.setDisable(false);
    }

    public void onPracticeModeClicked(MouseEvent mouseEvent) throws IOException {
        loadNextScreen(PRACTISE);
    }

    //reveal the progressbar after microphone button being clicked
    public void onMicrophoneButtonClicked() {
        if (isFirstTimeClickMic) {
            MicrophoneVolume.setVisible(true);
            Thread thread = new Thread(() -> {
                testMicrophone();
            });
            thread.start();
            isFirstTimeClickMic = !isFirstTimeClickMic;

        } else {
            MicrophoneVolume.setVisible(false);
            isFirstTimeClickMic = !isFirstTimeClickMic;
        }
    }

    /**
     * Set the microphone level using a moving average from TargetLine buffer
     */
    private void testMicrophone() {
        try {
            AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
            TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
            microphone.open();
            microphone.start();

            double highestVolume = 0;
            byte tempBuffer[] = new byte[1000];
            while (!isFirstTimeClickMic) {
                if (microphone.read(tempBuffer, 0, tempBuffer.length) > 0) {
                    double sumVolume = 0;
                    for (byte aTempBuffer : tempBuffer) {
                        double absoluteVolume = Math.abs(aTempBuffer);
                        sumVolume = sumVolume + absoluteVolume;
                        if (absoluteVolume > highestVolume) {
                            highestVolume = absoluteVolume;
                        }
                    }
                    double volume = (sumVolume / tempBuffer.length) / highestVolume;
                    Platform.runLater(() -> MicrophoneVolume.setProgress(volume));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAssessModeClicked(MouseEvent mouseEvent) throws IOException {
        loadNextScreen(ASSESSMENT);
    }

    private void loadNextScreen(Session.SessionType assessment) throws IOException {
        Scene scene = practiceButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NameSelectScreen.fxml"));
        Parent root = loader.load();
        NameSelectScreenController controller = loader.getController();
        controller.setSessionType(assessment);
        scene.setRoot(root);
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
    public void onBrowseModeClicked(MouseEvent e)throws IOException {
        Scene scene = browseButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RecordingScreen.fxml"));
        Parent root = loader.load();
        scene.setRoot(root);
}

}
