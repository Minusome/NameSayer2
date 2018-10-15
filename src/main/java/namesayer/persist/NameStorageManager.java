package namesayer.persist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;
import namesayer.model.PartialName;
import namesayer.persist.Result.Status;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;

import static namesayer.persist.Config.SAVED_RECORDINGS;
import static namesayer.persist.Config.SAVED_SESSIONS;
import static namesayer.persist.Config.USER_ATTEMPTS;


/**
 * Persists names which are stored 5eva
 * DO NOT USE for names which are only being stored temporarily in session(i.e. might be deleted later)
 */
public class NameStorageManager {


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
                Files.move(oldPath, newPath, StandardCopyOption.REPLACE_EXISTING);
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
            if (!Files.isDirectory(SAVED_SESSIONS)) {
                Files.createDirectories(SAVED_SESSIONS);
            }
            new CompositeNamesLoader().load(compositeNames);
            new PartialNamesLoader().load(partialNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
