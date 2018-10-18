package namesayer.persist;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Defines constants which are mainly folder names
 */
public class Config {
    public static final Path DATABASE_FOLDER = Paths.get("database");
    public static final Path GENERATED_FOLDER = Paths.get("generated");
    public static final Path USER_ATTEMPTS = GENERATED_FOLDER.resolve("attempts");
    public static final Path SAVED_RECORDINGS = GENERATED_FOLDER.resolve("saved");
    public static final Path SAVED_PRACTISE_SESSIONS = GENERATED_FOLDER.resolve("session").resolve("practise");
    public static final Path SAVED_ASSESSMENT_SESSIONS = GENERATED_FOLDER.resolve("session").resolve("assessment");
    public static final String WAV_EXTENSION = ".wav";
    public static final Path RATINGS = Paths.get("ratings.txt");
    public static final Path STATS_FILE = GENERATED_FOLDER.resolve("stats").resolve("data.ser");
}
