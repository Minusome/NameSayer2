package namesayer.persist;

import namesayer.model.CompleteName;
import namesayer.model.CompleteRecording;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static namesayer.util.Config.DATABSE_FOLDER;
import static namesayer.util.Config.USER_ATTEMPTS;
import static namesayer.util.Config.WAV_EXTENSION;

public class AssessmentSession extends Session {

    //User can only make one attempt at each name
    private Map<CompleteName, CompleteRecording> userAttempts = new HashMap<>();


    public void makeNewRecording(String recordingName) {
        Thread thread = new Thread(() -> {
            Path newRecordingPath = DATABSE_FOLDER.resolve(USER_ATTEMPTS).resolve(recordingName + WAV_EXTENSION).toAbsolutePath();
            String command = "ffmpeg -loglevel \"quiet\" -f alsa -i default -t " + currentName.getExemplar().getLength() + " -acodec pcm_s16le -ar 16000 -ac 1 -y \"" +
                    newRecordingPath.toString() + "\"";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
            try {
                Process process = builder.start();
                process.waitFor();
                userAttempts.put(currentName, new CompleteRecording(newRecordingPath));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }


    public void playUserRecording() {
        userAttempts.get(currentName).playAudio();
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
        List<CompleteRecording> recording = new ArrayList<>();
        recording.add(userAttempts.get(currentName));
        NameStorageManager.getInstance().persistCompleteRecordingsForName(currentName, recording);
    }


}
