package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

public class MenuScreenController {

    @FXML public JFXProgressBar MicrophoneVolume;
    @FXML public ImageView MicrophoneButton;
    @FXML private JFXButton practiceButton;
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
    public void onMicrophoneButtonClicked(){
        if(isFirstTimeClickMic) {
            MicrophoneVolume.setVisible(true);
            Thread thread = new Thread(() -> {
                TestMicrophone();
            });
            thread.start();
            isFirstTimeClickMic = !isFirstTimeClickMic;

        }else{
            MicrophoneVolume.setVisible(false);
            isFirstTimeClickMic = !isFirstTimeClickMic;
        }

    }

    private void TestMicrophone() {

// Obtain and open the line.

//        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);

        try {
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F, 16, 2, 4,
                    44100F, true);
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format); // format is an AudioFormat object //info from computer audio system
            if (!AudioSystem.isLineSupported(info)) {
                // Handle the error ...
                System.out.println("Fuck line not supported");
            }
            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetLine.open(format);
            targetLine.start();


//            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
//            sourceLine.open(format);
//            sourceLine.start();

            int numBytesRead;
            byte[] targetData = new byte[targetLine.getFormat().getFrameSize()];
            while (!isFirstTimeClickMic) {
                targetLine.read(targetData, 0, targetData.length);
                double  volume =  getLevel(format,targetData)/100;
//                Platform.runLater(() -> MicrophoneVolume.setProgress((float)volume));
                MicrophoneVolume.setProgress(volume);
                System.out.println(volume);
//                sourceLine.write(targetData, 0, targetData.length);
//                System.out.println(targetLine.isRunning());
                Thread.sleep(150);
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
        if (selectedDirectory != null) {
            NameStorageManager storageManager = NameStorageManager.getInstance();
            storageManager.initialize(selectedDirectory.toPath());
            isDirectorySelected = true;
            practiceButton.setDisable(false);
        }
    }

    public static double getLevel(AudioFormat af, byte[] chunk) throws IOException{
        PCMSigned8Bit converter = new PCMSigned8Bit(af);
        if(chunk.length != converter.getRequiredChunkByteSize())
            return -1;

        AudioInputStream ais = converter.convert(chunk);
        ais.read(chunk, 0, chunk.length);

        long lSum = 0;
        for(int i=0; i<chunk.length; i++)
            lSum = lSum + chunk[i];

        double dAvg = lSum / chunk.length;
        double sumMeanSquare = 0d;

        for(int j=0; j<chunk.length; j++)

            sumMeanSquare = sumMeanSquare + Math.pow(chunk[j] - dAvg, 2d);

        double averageMeanSquare = sumMeanSquare / chunk.length;

        return (Math.pow(averageMeanSquare,0.5d));
    }

}
