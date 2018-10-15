package namesayer.session;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;
import namesayer.persist.NameStorageManager;

public class AssessmentSession extends Session {

    public void makeNewRecording(EventHandler<ActionEvent> onFinished) {
        if (currentName.getUserAttempts().isEmpty()) {
            currentName.makeNewRecording(onFinished);
        }
    }

    public void compareUserAttemptWithExemplar(EventHandler<ActionEvent> onFinished) {
        Thread thread = new Thread(() -> {
            try {
                currentName.getExemplar().playAudio();
                Thread.sleep(new Double(currentName.getExemplar().getLength() * 1000).longValue());
                if (!currentName.getUserAttempts().isEmpty()) {
                    CompositeRecording userRecording = currentName.getUserAttempts().get(0);
                    userRecording.playAudio();
                    Thread.sleep(new Double(userRecording.getLength() * 1000).longValue());
                    onFinished.handle(new ActionEvent());
                }
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

    public double getAverageRating() {
        double rating = 0;
        for (CompositeName name : namesList) {
            rating += name.getUserAttempts().get(0).getRating();
            System.out.println(rating);
        }
        return rating / namesList.size();
    }


}
