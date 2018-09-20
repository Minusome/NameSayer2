package namesayer;

import javafx.scene.input.MouseEvent;
import namesayer.recording.Name;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class RecordingFragmentController {

    private Name name;

    void injectName(Name name) {
        this.name = name;
    }

    public void onRecordingButtonClicked(MouseEvent mouseEvent) {
        name.makeNewRecording("Recording on " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

}
