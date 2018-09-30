package namesayer.model;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.file.Path;

public abstract class Recording {

    private Path recordingPath;


    public Recording(Path recordingPath) {
        this.recordingPath = recordingPath;
    }


    //Play audio using bash command
    public void playAudio() {
        Thread thread = new Thread(() -> {
            String command = "ffplay -nodisp -autoexit -loglevel quiet \"" + recordingPath.toAbsolutePath().toString() + "\"";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
            try {
                Process process = builder.start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    public Path getRecordingPath() {
        return recordingPath;
    }

    public void setRecordingPath(Path recordingPath) {
        this.recordingPath = recordingPath;
    }

    //Calculates the length
    public double getLength() {
        double durationInSeconds = 0.0;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(recordingPath.toFile());
            AudioFormat format = audioInputStream.getFormat();
            long frames = audioInputStream.getFrameLength();
            durationInSeconds = (frames + 0.0) / format.getFrameRate();

        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        return durationInSeconds;
    }


    @Override
    public String toString() {
        return recordingPath.getFileName()
                            .toString()
                            .replace(".wav", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Recording recording = (Recording) o;

        return recordingPath != null ? recordingPath.equals(recording.recordingPath) : recording.recordingPath == null;
    }

    @Override
    public int hashCode() {
        return recordingPath != null ? recordingPath.hashCode() : 0;
    }
}
