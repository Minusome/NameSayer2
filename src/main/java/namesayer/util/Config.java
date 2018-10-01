package namesayer.util;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Defines constants which are mainly folder names
 */
public class Config {
    public static final Path DATABSE_FOLDER = Paths.get("database");
    public static final Path COMBINED_NAMES = Paths.get("combined");
    public static final Path USER_ATTEMPTS = Paths.get("attempts");
    public static final Path TEMP_RECORDINGS = Paths.get("temp");
    public static final Path SAVED_RECORDINGS = Paths.get("saved");
    public static final String WAV_EXTENSION = ".wav";
    public static final Path RATINGS = Paths.get("ratings.txt");
}
