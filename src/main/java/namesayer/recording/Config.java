package namesayer.recording;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {

    public static final Path CREATIONS_FOLDER = Paths.get(System.getProperty("user.home")).resolve("recordings");
    public static final Path TEMP_RECORDINGS = Paths.get("temp");
    public static final Path SAVED_RECORDINGS = Paths.get("saved");
    public static final String WAV_EXTENSION = ".wav";
}
