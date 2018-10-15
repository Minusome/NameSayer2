package namesayer.util;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Defines constants which are mainly folder names
 */
public class Config {
    public static final Path DATABSE_FOLDER = Paths.get("database");
    public static final Path GENERATED_FDOLER = Paths.get("generated");
    public static final Path USER_ATTEMPTS = GENERATED_FDOLER.resolve("attempts");
    public static final Path SAVED_RECORDINGS = GENERATED_FDOLER.resolve("saved");
    public static final String WAV_EXTENSION = ".wav";
    public static final Path RATINGS = Paths.get("ratings.txt");
}
