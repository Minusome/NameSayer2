package namesayer.model;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Exemplar {

    private List<PartialRecording> exemplarComponents;

    public Exemplar(List<PartialRecording> exemplarComponents) {
        this.exemplarComponents = exemplarComponents;
    }

    public void playAudio() {
        new Thread(() -> {
            for (PartialRecording recording : exemplarComponents) {
                recording.playAudio();
                try {
                    Thread.sleep((long)(recording.getLength() * 1000));
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
