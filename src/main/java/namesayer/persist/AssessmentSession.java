package namesayer.persist;

import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssessmentSession extends Session {

    //User can only make one attempt at each name
    private Map<CompositeName, CompositeRecording> userAttempts = new HashMap<>();

    @Override
    protected void addNewRecording(CompositeRecording recording) {
        userAttempts.put(currentName, recording);
    }


    public void playUserRecording() {
        CompositeRecording recording = userAttempts.get(currentName);
        if (recording != null){
            userAttempts.get(currentName).playAudio();
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
        return userAttempts.keySet().contains(currentName);
    }

    public void saveUserRecording() {
        List<CompositeRecording> recording = new ArrayList<>();
        recording.add(userAttempts.get(currentName));
        NameStorageManager.getInstance().persistCompleteRecordingsForName(currentName, recording);
    }

    public CompositeRecording getCurrentRecording() {
        return userAttempts.get(currentName);
    }




}
