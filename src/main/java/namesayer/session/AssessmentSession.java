package namesayer.session;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;
import namesayer.persist.NameStorageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssessmentSession extends Session {


    public void playUserRecording() {
        if (!currentName.getUserAttempts().isEmpty()) {
            currentName.getUserAttempts().get(0).playAudio();
        }
    }

    @Override
    public void makeNewRecording(String recordingName, EventHandler<ActionEvent> onFinished){
        if (currentName.getUserAttempts().isEmpty()) {
            super.makeNewRecording(recordingName, onFinished);
        }
    }

    public void compareUserAttemptWithExemplar() {
        Thread thread = new Thread(() -> {
            try {
                currentName.getExemplar().playAudio();
                Thread.sleep(new Double(currentName.getExemplar().getLength() * 1000).longValue());
                playUserRecording();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public boolean hasUserMadeRecording() {
        return !currentName.getUserAttempts().isEmpty();
    }

    public void saveUserRecording() {
        NameStorageManager.getInstance().persistCompleteRecordingsForName(currentName);
    }

    public CompositeRecording getCurrentRecording() {
        if (!currentName.getUserAttempts().isEmpty()) {
            return currentName.getUserAttempts().get(0);
        } else {
            throw new RuntimeException("No recordings have been made");
        }
    }




}
