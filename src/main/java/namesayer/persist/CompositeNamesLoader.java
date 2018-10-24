package namesayer.persist;

import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;

import java.nio.file.Path;
import java.util.regex.Pattern;

import static namesayer.persist.Config.SAVED_RECORDINGS;

/**
 * Responsible for loading CompositeNames which have been saved in the generated database.
 */

public class CompositeNamesLoader extends NamesLoader<CompositeName, CompositeRecording> {

    @Override
    public Path getDirectory() {
        return SAVED_RECORDINGS;
    }

    @Override
    public Pattern getRegex() {
        return Pattern.compile("[_a-zA-Z]+(?:\\.wav)");
    }

    @Override
    public String processString(String raw) {
        return raw.replace(".wav", "")
                  .replace("_", " ")
                  .trim();
    }

    @Override
    public CompositeName getName(String name) {
        return new CompositeName(name);
    }

    @Override
    public CompositeRecording getRecording(Path path) {
        return new CompositeRecording(path);
    }

    @Override
    public void populate(CompositeName name, CompositeRecording recording) {
        name.addUserAttempt(recording);
    }
}
