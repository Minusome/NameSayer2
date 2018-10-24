package namesayer.persist;

import namesayer.model.PartialName;
import namesayer.model.PartialRecording;

import java.nio.file.Path;
import java.util.regex.Pattern;

import static namesayer.persist.Config.DATABASE_FOLDER;

/**
 * Responsible for loading PartialNames which have been saved in the Name Corpus database.
 */

public class PartialNamesLoader extends NamesLoader<PartialName, PartialRecording> {

    @Override
    public Path getDirectory() {
        return DATABASE_FOLDER;
    }

    @Override
    public Pattern getRegex() {
        return Pattern.compile("[a-zA-Z]+(?:\\.wav)");
    }

    @Override
    public String processString(String raw) {
        String name = raw.replace(".wav", "");
        if (!name.isEmpty()) {
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
        }
        return name;
    }

    @Override
    public PartialName getName(String name) {
        return new PartialName(name);
    }

    @Override
    public PartialRecording getRecording(Path path) {
        return new PartialRecording(path);
    }

    @Override
    public void populate(PartialName name, PartialRecording recording) {
        name.addRecording(recording);
    }
}
