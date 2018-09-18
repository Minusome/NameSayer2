package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import namesayer.recording.NameStorageManager;

import javax.sound.sampled.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuScreenController implements Initializable {

    @FXML public JFXProgressBar MicrophoneVolume;
    @FXML public JFXButton MicrophoneButton;
    @FXML private JFXButton practiceButton;
    private boolean isDirectorySelected = false;
    private boolean isFirstTimeClickMic = true;
    @FXML private JFXButton microphoneTestingButton;
    private Thread tesetingMicThread;
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        MicrophoneButton.setOnMouseClicked(event -> isFirstTimeClickMic = !isFirstTimeClickMic);
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
    public void onMicrophoneButtonClicked(){
        if(isFirstTimeClickMic) {
            MicrophoneVolume.setVisible(true);
            Thread thread = new Thread(() -> {
                TestMicrophone();
            });
            thread.start();
            tesetingMicThread = thread;
        }else{
            MicrophoneVolume.setVisible(false);
                tesetingMicThread.interrupt();
        }
    }

    private void TestMicrophone() {
        AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 16, 2, 4,
                44100F, true);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format is an AudioFormat object //info from computer audio system

        if (!AudioSystem.isLineSupported(info)) {
            // Handle the error ...

        }
// Obtain and open the line.
        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);

        try {
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetLine.open(format);
            targetLine.start();

            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceLine.open(format);
            sourceLine.start();

            int numBytesRead;
            byte[] targetData = new byte[targetLine.getBufferSize() / 5];

            while (true) {
                numBytesRead = targetLine.read(targetData, 0, targetData.length);

                if (numBytesRead == -1)	break;

                sourceLine.write(targetData, 0, numBytesRead);
                if(isFirstTimeClickMic){
                    sourceLine.stop();
                    sourceLine.close();
                }
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }

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
