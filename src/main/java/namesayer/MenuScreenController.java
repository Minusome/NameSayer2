package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import namesayer.recording.NameStorageManager;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class MenuScreenController {

    @FXML public JFXProgressBar MicrophoneVolume;
    @FXML public ImageView MicrophoneButton;
    @FXML private JFXButton practiceButton;
    @FXML private JFXButton loadExistingDataBaseButton;
    private boolean isDirectorySelected = false;
    private boolean isFirstTimeClickMic = true;
    @FXML private ImageView microphoneTestingButton;
    private Thread tesetingMicThread;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

//        MicrophoneButton.setOnMouseClicked(event -> isFirstTimeClickMic = !isFirstTimeClickMic);
//        MicrophoneButton.setGraphic(new ImageView("C:\\Users\\zhugu\\Documents\\GitHub\\NameSayer\\src\\icon\\microphone2.png"));
        //unale to load the icon for the microphone for some reason
//        Image image = new Image(getClass().getResourceAsStream("src/icon/microphone2.png"));
//        MicrophoneButton.setGraphic(new ImageView(image));
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
                TestMicrophone();
            });
            thread.start();
            isFirstTimeClickMic = !isFirstTimeClickMic;

        } else {
            MicrophoneVolume.setVisible(false);
            isFirstTimeClickMic = !isFirstTimeClickMic;
        }

    }

    private void TestMicrophone() {

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
                    System.out.println(volume);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onSelectLoadPreviousFolder(MouseEvent mouseEvent){
        //TO BE IMPELEMENTED
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.setTitle("Select the existing database for your names");
        File selectedDirectory = chooser.showDialog(practiceButton.getScene().getWindow());
        if (selectedDirectory != null) {
            NameStorageManager storageManager = NameStorageManager.getInstance();
            storageManager.loadExistingHierarchy(selectedDirectory.toPath());
            isDirectorySelected = true;
            practiceButton.setDisable(false);
        }
    }


    public void printl(){
        System.out.println("Mouse Detected!");
    }

    public void onSelectAudioDatabaseFolder(MouseEvent mouseEvent) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select the audio database for your names");
        File selectedDirectory = chooser.showDialog(practiceButton.getScene().getWindow());
        if (selectedDirectory != null) {
            NameStorageManager storageManager = NameStorageManager.getInstance();
            storageManager.initialize(selectedDirectory.toPath());
            isDirectorySelected = true;
            practiceButton.setDisable(false);
        }
    }


}
