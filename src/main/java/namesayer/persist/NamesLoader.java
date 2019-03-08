package namesayer.persist;


import namesayer.model.Name;
import namesayer.model.Recording;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Base class for loading saved name files into memory
 *
 * @param <N> CompositeName or PartialName
 * @param <R> CompositeRecording or PartialRecording
 */

public abstract class NamesLoader<N extends Name, R extends Recording> {


    /**
     * Traverses the database, parses each name using regex and instantiates Name and Recording
     * implementatons in memory
     *
     * @param listToLoad list to be populated
     */
    public void load(List<N> listToLoad) {
        try (Stream<Path> paths = Files.walk(getDirectory())) {
            Map<String, N> initializedNames = new HashMap<>();
            paths.filter(Files::isRegularFile).forEach(path -> {
                Matcher matcher = getRegex().matcher(path.getFileName().toString());
                String name = "unrecognized";
                if (matcher.find()) {
                    name = processString(matcher.group(0));
                }
                N newName;
                if (!initializedNames.containsKey(name)) {
                    newName = getName(name);
                    initializedNames.put(name, newName);
                    listToLoad.add(newName);
                } else {
                    newName = initializedNames.get(name);
                }
                R recording = getRecording(path);
                populate(newName, recording);
            });
            Collections.sort(listToLoad);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Template Methods for different loading strategies
     **/
    public abstract Path getDirectory();

    public abstract Pattern getRegex();

    public abstract String processString(String string);

    public abstract N getName(String name);

    public abstract R getRecording(Path path);

    public abstract void populate(N name, R recording);
}
