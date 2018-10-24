package namesayer.model;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Abstract base class of any type of Recording
 * Represents a WAV audio file
 */

public abstract class Recording implements Serializable {

    private String recordingPath;
    private String filename;

    public Recording(Path recordingPath) {
        this.recordingPath = recordingPath.toAbsolutePath().toString();
        this.filename = recordingPath.getFileName().toString();
    }


    /**
     * Play audio using FFplay in separate thread
     */
    public void playAudio() {
        Thread thread = new Thread(() -> {
            String command = "ffplay -nodisp -autoexit -loglevel quiet \"" + recordingPath + "\"";
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
        return Paths.get(recordingPath);
    }

    public void setRecordingPath(Path newPath) {
        recordingPath = newPath.toAbsolutePath().toString();
    }


    /**
     * Calculates the length using Java AudioStream API
     */
    public double getLength() {
        double durationInSeconds = 0.0;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Paths.get(recordingPath).toFile());
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
        return filename.replace(".wav", "");
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
