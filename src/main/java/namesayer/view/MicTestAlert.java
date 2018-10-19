package namesayer.view;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXProgressBar;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import java.io.IOException;

public class MicTestAlert extends JFXAlert {

    @FXML private StackPane rootPane;
    @FXML private JFXProgressBar microphoneVolume;

    public MicTestAlert(Stage stage) {
        super(stage);
        loadContent(stage);
    }

    private void loadContent(Stage stage) {
        this.initModality(Modality.WINDOW_MODAL);
        this.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Microphone Volume"));

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MicTestAlert.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(this::testMicrophone);
        thread.start();
        JFXButton doneButton = new JFXButton("Done");
        layout.setBody(rootPane);
        doneButton.setOnAction(event -> {
            thread.interrupt();
            this.hideWithAnimation();
        });
        layout.setActions(doneButton);
        this.setContent(layout);
    }


    private void testMicrophone() {
        try {
            AudioFormat format = new AudioFormat(8000.0f, 16, 1, true, true);
            TargetDataLine microphone = AudioSystem.getTargetDataLine(format);
            microphone.open();
            microphone.start();

            double highestVolume = 0;
            byte tempBuffer[] = new byte[1000];
            while (true){
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
