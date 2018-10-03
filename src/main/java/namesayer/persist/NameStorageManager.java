package namesayer.persist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import namesayer.model.CompleteName;
import namesayer.model.PartialName;
import namesayer.model.PartialNameRecording;
import namesayer.util.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static namesayer.util.Config.COMBINED_NAMES;
import static namesayer.util.Config.DATABSE_FOLDER;
import static namesayer.util.Config.USER_ATTEMPTS;

public class NameStorageManager {

    private static final Pattern REGEX_NAME_PARSER = Pattern.compile("[a-zA-Z]+(?:\\.wav)");
    private static NameStorageManager instance = null;

    private List<PartialName> partialNames = new LinkedList<>();
    private List<CompleteName> completeNames = new LinkedList<>();

    public static NameStorageManager getInstance() {
        if (instance == null) {
            instance = new NameStorageManager();
        }
        return instance;
    }


    public void load() {
        try (Stream<Path> paths = Files.walk(DATABSE_FOLDER)) {
            Map<String, PartialName> initializedNames = new HashMap<>();
            paths.forEach(path -> {
                     //Extract name from provided database
                     Matcher matcher = REGEX_NAME_PARSER.matcher(path.getFileName().toString());
                     String name = "unrecognized";
                     if (matcher.find()) {
                         name = matcher.group(0).replace(".wav", "").toLowerCase();
//                             if (!name.isEmpty()) {
//                                 name = name.substring(0, 1).toUpperCase() + name.substring(1);
//                             }
                     }
                     PartialName newName;
                     if (!initializedNames.containsKey(name)) {
                         newName = new PartialName(name);
                         initializedNames.put(name, newName);
                         partialNames.add(newName);
                     } else {
                         newName = initializedNames.get(name);
                     }

                     PartialNameRecording recording = new PartialNameRecording(path);
                     newName.addDatabaseRecording(recording);
                     System.out.println(recording);
                 });
            //sorts the final list
            Collections.sort(partialNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public ObservableList<PartialName> getPartialNames() {
        return FXCollections.observableList(partialNames);
    }

    public PartialName findPartialNameFromString(String s){
        for (PartialName pn : partialNames) {
            if (pn.toString().equals(s)) {
                return pn;
            }
        }
        return null;
    }


    public Result queryUserRequestedName(String userRequestedName){
        String[] components = userRequestedName.split("[\\s-]+");
        List<PartialName> discoveredNames = new ArrayList<>();
        for (String s : components) {
            PartialName name = findPartialNameFromString(s);
            if (name != null) {
                discoveredNames.add(name);
            }
        }
        if (discoveredNames.size() == 0) {
            return Result.NONE_FOUND;
        } else if (discoveredNames.size() == components.length) {
            return Result.ALL_FOUND;
        } else {
            return Result.PARTIALLY_FOUND;
        }
    }

    public void addCompleteName(CompleteName name) {
        completeNames.add(name);
    }

    public List<CompleteName> getCompleteNames() {
        return Collections.unmodifiableList(completeNames);
    }

//
//    /**
//     * load existing database hierarchy
//     */
//    public void loadExistingHierarchy(Path folderPath, Button button) {
//        partialNames = new LinkedList<>();
//        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath)) {
//            for (Path e : stream) {
//                Name temp = new Name(e.getFileName().toString(), e);
//                partialNames.add(temp);
//                Properties ratingProperties = new Properties();
//
//                //load the properties
//                ratingProperties.load(new FileInputStream(e.resolve(RATINGS).toFile()));
//                try (DirectoryStream<Path> stream1 = Files.newDirectoryStream(e.resolve(SAVED_RECORDINGS))) {
//
//                    for (Path p : stream1) {
//                        Recording recording = new Recording(p);
//                        double rating = Double.valueOf(ratingProperties.getProperty(recording.toString()));
//                        recording.setRating(rating);
//                        temp.addSavedRecording(recording);//add saved recordings to corresponding name
//                    }
//
//                } catch (IOException e1) {
//
//                }
//                try (DirectoryStream<Path> stream1 = Files.newDirectoryStream(new File(e.toString() + "/temp").toPath())) {
//                    for (Path p : stream1) {
//                        temp.addSavedRecording(new Recording(p, true));//add user created recordings to corresponding name
//                    }
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//                Collections.sort(partialNames);
//                Platform.runLater(() -> button.setDisable(false));
//            }
//        } catch (IOException e) {
//
//        } finally {
//
//        }
//    }
//
//
//    /**
//     * Create new database hierarchy
//     */
//    public void initialize(Path folderPath, Button button) {
//        try {
//            if (!Files.isDirectory(CREATIONS_FOLDER)) {
//                Files.createDirectory(CREATIONS_FOLDER);
//            } else {
//                Files.walk(CREATIONS_FOLDER)
//                     .map(Path::toFile)
//                     .forEach(File::delete);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //Ensure non-blocking
//        Thread thread = new Thread(() -> {
//            try (Stream<Path> paths = Files.walk(folderPath)) {
//                Map<String, Name> initializedNames = new HashMap<>();
//                paths.filter(Files::isRegularFile)
//                     .forEach(path -> {
//                         //Extract name from provided database
//                         Matcher matcher = REGEX_NAME_PARSER.matcher(path.getFileName().toString());
//                         String name = "unrecognized";
//                         if (matcher.find()) {
//                             name = matcher.group(0).replace(".wav", "").toLowerCase();
//                             if (!name.isEmpty()) {
//                                 name = name.substring(0, 1).toUpperCase() + name.substring(1);
//                             }
//                         }
//                         Path nameFolder = CREATIONS_FOLDER.resolve(name);
//                         Path tempFolder = nameFolder.resolve(TEMP_RECORDINGS);
//                         Path savedFolder = nameFolder.resolve(SAVED_RECORDINGS);
//                         Name newName = new Name(name, nameFolder);
//                         try {
//                             //Create sub-folders if not already created
//                             if (!initializedNames.containsKey(name)) {
//                                 Files.createDirectories(savedFolder);
//                                 Files.createDirectories(tempFolder);
//                                 initializedNames.put(name, newName);
//                                 partialNames.add(newName);
//                             } else {
//                                 newName = initializedNames.get(name);
//                             }
//                             Path recordingPath = savedFolder.resolve(path.getFileName());
//                             Files.copy(path, recordingPath);
//                             if (Files.notExists(nameFolder.resolve(RATINGS))) {
//                                 Files.createFile(nameFolder.resolve(RATINGS));
//                             }
//                             Recording recording = new Recording(recordingPath);
//                             newName.addSavedRecording(recording);
//                         } catch (IOException e) {
//                             e.printStackTrace();
//                         }
//                     });
//                //sorts the final list
//                Collections.sort(partialNames);
//                Platform.runLater(() -> button.setDisable(false));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        thread.start();
//    }

    private NameStorageManager() {
        try {
            if (!Files.isDirectory(DATABSE_FOLDER.resolve(COMBINED_NAMES))) {
                Files.createDirectory(DATABSE_FOLDER.resolve(COMBINED_NAMES));
            }
            if (!Files.isDirectory(DATABSE_FOLDER.resolve(USER_ATTEMPTS))) {
                Files.createDirectory(DATABSE_FOLDER.resolve(USER_ATTEMPTS));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        partialNames.clear();
    }

//    public void saveAllTempRecordings() {
//        partialNames.forEach(Name::saveTempRecordings);
//    }
//
//    public void removeAllTempRecordings() {
//        partialNames.forEach(Name::removeTempRecordings);
//    }

//    public ObservableList<Name> getNamesList() {
//        return FXCollections.observableList(partialNames);
//    }
//
//    public ObservableList<Name> getSelectedNamesList() {
//        ObservableList<Name> list = partialNames.stream()
//                                             .filter(Name::getSelected)
//                                             .collect(Collectors.toCollection(FXCollections::observableArrayList));
//        if (NameSelectScreenController.RandomToggleOn()) {
//            Collections.shuffle(list);
//            return list;
//        }
//        return list;
//    }


}
