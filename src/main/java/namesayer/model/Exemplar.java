package namesayer.model;

import java.io.Serializable;
import java.util.List;


/**
 * Represents the exemplar of a CompositeName
 * This Exemplar is created by extracting PartialNames from the Names Corpus Database
 */

public class Exemplar implements Serializable {

    private List<PartialRecording> exemplarComponents;

    public Exemplar(List<PartialRecording> exemplarComponents) {
        this.exemplarComponents = exemplarComponents;
    }

    /**
     * Plays the exemplar on a new thread
     */
    public void playAudio() {
        new Thread(() -> {
            for (PartialRecording recording : exemplarComponents) {
                recording.playAudio();
                try {
                    //Wait for the PartialRecording to finish playing before playing the next one
                    Thread.sleep((long) (recording.getLength() * 1000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public double getLength() {
        double length = 0;
        for (PartialRecording recording : exemplarComponents) {
            length += recording.getLength();
        }
        return length;
    }
}
