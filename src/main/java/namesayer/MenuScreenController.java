package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import namesayer.model.PartialName;
import namesayer.util.NameStorageManager;
//import namesayer.util.NameStorageManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MenuScreenController {

    @FXML public JFXProgressBar MicrophoneVolume;
    @FXML public ImageView MicrophoneButton;
    @FXML private JFXButton loadNewDataBaseButton;
    @FXML private JFXButton practiceButton;
    @FXML private JFXButton loadExistingDataBaseButton;
    private boolean isFirstTimeClickMic = true;
    private NameStorageManager nameStorageManager = NameStorageManager.getInstance();
    @FXML private ImageView microphoneTestingButton;


    public void initialize() {
        practiceButton.setDisable(false);
        nameStorageManager.load();
        List<PartialName> namesList = nameStorageManager.checkOK();
        System.out.println(namesList.size());
    }

    public void onPracticeModeClicked(MouseEvent mouseEvent) throws IOException {
        Scene scene = practiceButton.getScene();
        Parent root = FXMLLoader.load(getClass().getResource("/NameSelectScreen.fxml"));
        scene.setRoot(root);
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


}
