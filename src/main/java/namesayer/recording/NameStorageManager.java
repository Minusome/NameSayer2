package namesayer.recording;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class NameStorageManager {

    private static final Pattern REGEX_NAME_PARSER = Pattern.compile("[a-zA-Z]+(?:\\.wav)");
    private static final Path CREATIONS_FOLDER = Paths.get(System.getProperty("user.home")).resolve("recordings");
    private static final Path TEMP_RECORDINGS = Paths.get("temp");
    private static final Path SAVED_RECORDINGS = Paths.get("saved");
    private static NameStorageManager instance = null;

    private List<Name> namesList = new ArrayList<>();


    public static NameStorageManager getInstance() {
        if (instance == null) {
            instance = new NameStorageManager();
        }
        return instance;
    }


    public void initialize(Path folderPath){
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
                HashSet<String> initializedNames = new HashSet<>();
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
                             if (!initializedNames.contains(name)) {
                                 Files.createDirectories(savedFolder);
                                 Files.createDirectories(tempFolder);
                                 initializedNames.add(name);
                                 namesList.add(newName);
                             }
                             Path recordingPath = savedFolder.resolve(path.getFileName());
                             Files.copy(path, recordingPath);
                             Recording recording = new Recording(recordingPath);
                             newName.addSavedRecording(recording);
                         } catch (IOException e) {
                             e.printStackTrace();
                         }
                     });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }



    //Initialize the correct folder hierarchy
    private NameStorageManager() {
    }


    public List<Name> getNamesList() {
        return Collections.unmodifiableList(namesList);
    }


}
