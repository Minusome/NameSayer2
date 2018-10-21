package namesayer.view.alert;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;

import static namesayer.util.Screen.MIC_TEST_ALERT;

public class MicTestAlert extends JFXAlert {

    @FXML private JFXProgressBar microphoneVolume;

    private Parent root;
    private boolean isSampling = true;

    public MicTestAlert(Stage stage) {
        super(stage);
        loadContent();
    }

    private void loadContent() {
        this.initModality(Modality.WINDOW_MODAL);
        FXMLLoader loader = MIC_TEST_ALERT.getLoader();
        loader.setController(this);
        try {
            root =  loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Microphone Volume"));
        layout.setBody(root);
        JFXButton doneButton = new JFXButton("Done");

        doneButton.setOnAction(event -> {
            isSampling = false;
            this.hideWithAnimation();
        });
        layout.setActions(doneButton);
        this.setContent(layout);
        Thread thread = new Thread(this::testMicrophone);
        thread.start();
    }


    private void testMicrophone() {
        try {
            AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
            TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
            microphone.open();
            microphone.start();

            double highestVolume = 0;
            byte tempBuffer[] = new byte[1000];
            while (isSampling){
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
                    Platform.runLater(() -> microphoneVolume.setProgress(volume));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
