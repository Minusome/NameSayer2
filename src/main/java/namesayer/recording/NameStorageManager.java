package namesayer.recording;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import namesayer.NameSelectScreenController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static namesayer.recording.Config.CREATIONS_FOLDER;
import static namesayer.recording.Config.RATINGS;
import static namesayer.recording.Config.SAVED_RECORDINGS;
import static namesayer.recording.Config.TEMP_RECORDINGS;

public class NameStorageManager {

    private static final Pattern REGEX_NAME_PARSER = Pattern.compile("[a-zA-Z]+(?:\\.wav)");
    private static NameStorageManager instance = null;

    private List<Name> namesList = new LinkedList<>();


    public static NameStorageManager getInstance() {
        if (instance == null) {
            instance = new NameStorageManager();
        }
        return instance;
    }


    public void initialize(Path folderPath) {
        try {
            if (!Files.isDirectory(CREATIONS_FOLDER)) {
                Files.createDirectory(CREATIONS_FOLDER);
            } else {
                Files.walk(CREATIONS_FOLDER)
                     .map(Path::toFile)
                     .forEach(File::delete);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Ensure non-blocking
        Thread thread = new Thread(() -> {
            try (Stream<Path> paths = Files.walk(folderPath)) {
                Map<String, Name> initializedNames = new HashMap<>();
                paths.filter(Files::isRegularFile)
                     .forEach(path -> {
                         //Extract name from provided database
                         Matcher matcher = REGEX_NAME_PARSER.matcher(path.getFileName().toString());
                         String name = "unrecognized";
                         if (matcher.find()) {
                             name = matcher.group(0).replace(".wav", "");
                         }
                         Path nameFolder = CREATIONS_FOLDER.resolve(name);
                         Path tempFolder = nameFolder.resolve(TEMP_RECORDINGS);
                         Path savedFolder = nameFolder.resolve(SAVED_RECORDINGS);
                         Name newName = new Name(name, nameFolder);
                         try {
                             //Create sub-folders if not already created
                             if (!initializedNames.containsKey(name)) {
                                 Files.createDirectories(savedFolder);
                                 Files.createDirectories(tempFolder);
                                 initializedNames.put(name, newName);
                                 namesList.add(newName);
                             } else {
                                 newName = initializedNames.get(name);
                             }
                             Path recordingPath = savedFolder.resolve(path.getFileName());
                             Files.copy(path, recordingPath);
                             if (Files.notExists(nameFolder.resolve(RATINGS))) {
                                 Files.createFile(nameFolder.resolve(RATINGS));
                             }
                             Recording recording = new Recording(recordingPath);
                             newName.addSavedRecording(recording);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     });
                Collections.sort(namesList);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    //Initialize the correct folder hierarchy
    private NameStorageManager() {
    }


    public ObservableList<Name> getNamesList() {
        return FXCollections.observableList(namesList);
    }

    public ObservableList<Name> getSelectedNamesList() {
        ObservableList<Name> list = namesList.stream()
                                             .filter(Name::getSelected)
                                             .collect(Collectors.toCollection(FXCollections::observableArrayList));
        if (NameSelectScreenController.RandomToggleOn()) {
            Collections.shuffle(list);
            return list;
        }
        return list;
    }


}
