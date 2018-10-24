package namesayer.persist;


import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Defines constants which describe the required folder structure
 */
public class Config {
    public static final Path DATABASE_FOLDER = Paths.get("database");
    public static final Path GENERATED_FOLDER = Paths.get("generated");
    public static final Path USER_ATTEMPTS = GENERATED_FOLDER.resolve("attempts");
    public static final Path SAVED_RECORDINGS = GENERATED_FOLDER.resolve("saved");
    public static final Path SAVED_PRACTISE_SESSIONS = GENERATED_FOLDER.resolve("session").resolve("practise");
    public static final Path SAVED_ASSESSMENT_SESSIONS = GENERATED_FOLDER.resolve("session").resolve("assessment");
    public static final String WAV_EXTENSION = ".wav";
    public static final Path BAD_QUALITY_FILE = GENERATED_FOLDER.resolve("bad_quality_names.txt");
    public static final Path STATS_FOLDER = GENERATED_FOLDER.resolve("stats");
    public static final Path STATS_FILE = STATS_FOLDER.resolve("data.ser");

    public static final double BUFFER_TIME = 1;
}
