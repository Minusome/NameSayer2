package namesayer.persist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import namesayer.model.*;
import namesayer.util.Result;
import namesayer.util.Result.Status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static namesayer.util.Config.*;


/**
 * Persists names which are stored 5eva
 * DO NOT USE for names which are only being stored temporarily in session(i.e. might be deleted later)
 */
public class NameStorageManager {

    private static final Pattern REGEX_PARTIAL_NAME_PARSER = Pattern.compile("[a-zA-Z]+(?:\\.wav)");
    private static final Pattern REGEX_COMPOSITE_NAME_PARSER = Pattern.compile("[_a-zA-Z]+(?:\\.wav)");
    private static NameStorageManager instance = null;

    //TODO should probably be some kind of map
    private List<PartialName> partialNames = new LinkedList<>();
    private List<CompositeName> compositeNames = new LinkedList<>();

    public static NameStorageManager getInstance() {
        if (instance == null) {
            instance = new NameStorageManager();
        }
        return instance;
    }


    public void loadDatabase() {
        try (Stream<Path> paths = Files.walk(DATABSE_FOLDER)) {
            Map<String, PartialName> initializedNames = new HashMap<>();
            paths.filter(Files::isRegularFile).forEach(path -> {
                System.out.println(path);
                Matcher matcher = REGEX_PARTIAL_NAME_PARSER.matcher(path.getFileName().toString());
                String name = "unrecognized";
                if (matcher.find()) {
                    name = matcher.group(0).replace(".wav", "");
                    if (!name.isEmpty()) {
                        name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    }
                }
                PartialName newName;
                if (!initializedNames.containsKey(name)) {
                    newName = new PartialName(name);
                    initializedNames.put(name, newName);
                    partialNames.add(newName);
                } else {
                    newName = initializedNames.get(name);
                }

                PartialRecording recording = new PartialRecording(path);
                newName.addRecording(recording);
            });
            Collections.sort(partialNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadGenerated() {
        try (Stream<Path> paths = Files.walk(SAVED_RECORDINGS)) {
            Map<String, CompositeName> initializedNames = new HashMap<>();
            paths.filter(Files::isRegularFile).forEach(path -> {
                System.out.println(path);
                Matcher matcher = REGEX_COMPOSITE_NAME_PARSER.matcher(path.getFileName().toString());
                String name = "unrecognized";
                if (matcher.find()) {
                    name = matcher.group(0)
                            .replace(".wav", "")
                            .replace("_", " ")
                            .trim();
                }
                CompositeName newName;
                if (!initializedNames.containsKey(name)) {
                    newName = new CompositeName(name);
                    initializedNames.put(name, newName);
                    compositeNames.add(newName);
                } else {
                    newName = initializedNames.get(name);
                }

                CompositeRecording recording = new CompositeRecording(path);
                newName.addUserAttempt(recording);
            });
            Collections.sort(partialNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public <N extends Name, R extends Recording> void  load(N name, R recording, Function<String,String> stringParser) {
        try (Stream<Path> paths = Files.walk(SAVED_RECORDINGS)) {
            Map<String, CompositeName> initializedNames = new HashMap<>();
            paths.filter(Files::isRegularFile).forEach(path -> {
                System.out.println(path);
                Matcher matcher = REGEX_COMPOSITE_NAME_PARSER.matcher(path.getFileName().toString());
                String name = "unrecognized";
                if (matcher.find()) {
                    name = matcher.group(0)
                            .replace(".wav", "")
                            .replace("_", " ")
                            .trim();
                }
                CompositeName newName;
                if (!initializedNames.containsKey(name)) {
                    newName = new CompositeName(name);
                    initializedNames.put(name, newName);
                    compositeNames.add(newName);
                } else {
                    newName = initializedNames.get(name);
                }

                CompositeRecording recording = new CompositeRecording(path);
                newName.addUserAttempt(recording);
            });
            Collections.sort(partialNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public ObservableList<PartialName> getPartialNames() {
        return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(partialNames));
    }


    public ObservableList<CompositeName> getCompositeNames() {
        return FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(compositeNames));
    }

    public PartialName findPartialNameFromString(String s) {
        for (PartialName pn : partialNames) {
            if (pn.toString().toLowerCase().equals(s.toLowerCase())) {
                return pn;
            }
        }
        return null;
    }


    public Result queryUserRequestedName(String userRequestedName) {
        String[] components = userRequestedName.split("[\\s-]+");
        StringBuilder discoveredBuilder = new StringBuilder();
        int count = 0;
        for (String s : components) {
            PartialName name = findPartialNameFromString(s);
            if (name != null) {
                discoveredBuilder.append(name.toString()).append(" ");
                count++;
            } else {
            }
        }
        String discoveredName = discoveredBuilder.toString().trim();
        if (count == 0) {
            return new Result(Status.NONE_FOUND, discoveredName);
        } else if (count == components.length) {
            return new Result(Status.ALL_FOUND, discoveredName);
        } else {
            return new Result(Status.PARTIALLY_FOUND, discoveredName);
        }
    }

    public void persistCompleteRecordingsForName(CompositeName newName) {
        for (CompositeRecording newRecording : newName.getUserAttempts()) {
            Path oldPath = newRecording.getRecordingPath();
            Path newPath = SAVED_RECORDINGS.resolve(oldPath.getFileName());
            try {
                Files.move(oldPath, newPath , StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
            newRecording.setRecordingPath(newPath);
        }
        for (CompositeName storedName : compositeNames) {
            if (storedName.equals(newName)) {
                storedName.getUserAttempts().addAll(newName.getUserAttempts());
                return;
            }
        }
        compositeNames.add(newName);
    }


    private NameStorageManager() {
        try {
            if (!Files.isDirectory(USER_ATTEMPTS)) {
                Files.createDirectories(USER_ATTEMPTS);
            }
            if (!Files.isDirectory(SAVED_RECORDINGS)) {
                Files.createDirectories(SAVED_RECORDINGS);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
