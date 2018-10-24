package namesayer.persist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import namesayer.model.CompositeName;
import namesayer.model.CompositeRecording;
import namesayer.model.PartialName;
import namesayer.model.PartialRecording;
import namesayer.persist.Result.Status;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static namesayer.persist.Config.*;


/**
 * Singleton responsible for all file storage operations (loading and saving) surrounding Names
 * DO NOT store names which are only being used temporarily in a session (i.e. might be deleted later)
 */
public class NameStorageManager {


    private static NameStorageManager instance = null;
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

    /**
     * Find a partial name by string
     *
     * @param s String name
     * @return Found PartialName
     */
    public PartialName findPartialNameFromString(String s) {
        for (PartialName pn : partialNames) {
            if (pn.toString().toLowerCase().equals(s.toLowerCase())) {
                return pn;
            }
        }
        return null;
    }


    /**
     * Responsible for parsing a name entered by the user (i.e. user wants
     * to practice) and returning a Result describing if the user's desired
     * name is included in the database
     *
     * @param userRequestedName Raw input
     * @return Result
     */
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
        //The canonical name from the database
        String discoveredName = discoveredBuilder.toString().trim();
        if (count == 0) {
            return new Result(Status.NONE_FOUND, discoveredName);
        } else if (count == components.length) {
            return new Result(Status.ALL_FOUND, discoveredName);
        } else {
            return new Result(Status.PARTIALLY_FOUND, discoveredName);
        }
    }

    /**
     * Saves a CompositeName to the database.
     * First copy all recording files into saved database location.
     * Then, if CompositeName already exists in the database it is updated,
     * if its not then CompositeName is added
     *
     * @param newName CompositeName to save
     */
    public void persistCompleteRecordingsForName(CompositeName newName) {
        List<CompositeRecording> copyOfNames = new ArrayList<>(newName.getUserAttempts());
        for (CompositeRecording newRecording : newName.getUserAttempts()) {
            Path oldPath = newRecording.getRecordingPath();
            Path newPath = SAVED_RECORDINGS.resolve(oldPath.getFileName());
            try {
                File target = new File(newPath.toUri());
                if (!target.exists()) {
                    //Copy new files over
                    Files.move(oldPath, newPath);
                } else {
                    //If already exists, remove it
                    copyOfNames.remove(newRecording);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            newRecording.setRecordingPath(newPath);
        }
        for (CompositeName storedName : compositeNames) {
            if (storedName.equals(newName)) {
                //Update if found and return
                storedName.getUserAttempts().addAll(copyOfNames);
                return;
            }
        }
        //Create a copy and add it
        CompositeName copy = new CompositeName(newName.toString());
        copy.setExemplar(newName.getExemplar());
        for (CompositeRecording attempts : newName.getUserAttempts()) {
            copy.addUserAttempt(attempts);
        }
        compositeNames.add(copy);
    }


    private NameStorageManager() {
        try {
            //Initializes directories
            Path[] pathsArray = new Path[]{
                    USER_ATTEMPTS,
                    SAVED_RECORDINGS,
                    SAVED_PRACTISE_SESSIONS,
                    SAVED_ASSESSMENT_SESSIONS,
                    STATS_FOLDER
            };
            for (Path path : pathsArray) {
                if (!Files.isDirectory(path)) {
                    Files.createDirectories(path);
                }
            }
            //Load both databases
            new CompositeNamesLoader().load(compositeNames);
            new PartialNamesLoader().load(partialNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the names of all bad quality recordings to a txt file
     */
    public void refreshBadQualityFile() {
        Thread thread = new Thread(() -> {
            try {
                File file = new File(BAD_QUALITY_FILE.toUri());
                file.createNewFile();

                FileWriter fw = new FileWriter(file.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                bw.write("The following names have been marked as bad quality: \n\n");

                for (PartialName name : partialNames) {
                    for (PartialRecording recording : name.getRecordings()) {
                        if (recording.isBadQuality()) {
                            bw.write(recording.getRecordingPath().getFileName().toString());
                        }
                    }
                }
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    /**
     * Updates a PartialName in the database or add it if not exist
     *
     * @param name PartialName to add
     */
    public void addNewPartialName(PartialName name) {
        for (PartialName pn : partialNames) {
            if (pn.toString().toLowerCase().equals(name.toString().toLowerCase())) {
                pn.addRecording(name.getRecordings().get(0));
                return;
            }
        }
        partialNames.add(name);
    }
}
