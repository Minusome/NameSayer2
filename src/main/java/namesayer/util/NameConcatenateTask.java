package namesayer.util;

import javafx.concurrent.Task;
import namesayer.model.CompleteName;
import namesayer.model.CompleteNameRecording;
import namesayer.model.PartialName;
import namesayer.model.PartialNameRecording;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static namesayer.util.Config.COMBINED_NAMES;
import static namesayer.util.Config.DATABSE_FOLDER;
import static namesayer.util.Config.WAV_EXTENSION;


/**
 * Parses user requested names into CompleteNames where available
 */
public class NameConcatenateTask extends Task<Void> {

    private String userRequestedName;

    public NameConcatenateTask(String userRequestedNames) {
        this.userRequestedName = userRequestedNames;
    }


    /**
     * Invoked when the Task is executed, the call method must be overridden and
     * implemented by subclasses. The call method actually performs the
     * background thread logic. Only the updateProgress, updateMessage, updateValue and
     * updateTitle methods of Task may be called from code within this method.
     * Any other interaction with the Task from the background thread will result
     * in runtime exceptions.
     *
     * @return The result of the background work, if any.
     * @throws Exception an unhandled exception which occurred during the
     *                   background operation
     */
    @Override
    protected Void call() throws Exception {
        NameStorageManager manager = NameStorageManager.getInstance();
        String[] components = userRequestedName.split("[\\s-]+");
        List<PartialName> discoveredNames = new ArrayList<>();
        for (String s : components) {
            PartialName name = manager.findPartialNameFromString(s);
            if (name != null) {
                discoveredNames.add(name);
            }
        }
        if (discoveredNames.size() == 0) {
            return null;
        }

        StringBuilder command = new StringBuilder("sox");
        for (PartialName name : discoveredNames) {
            PartialNameRecording partialNameRecording = name.getRecording();
            command.append(" \"")
                   .append(partialNameRecording.getRecordingPath().toAbsolutePath().toString())
                   .append("\"");
        }
        //TODO change the naming convention for combined names

        Path completeRecordingPath = DATABSE_FOLDER.resolve(COMBINED_NAMES)
                                                   .resolve(userRequestedName.replace(" ", "_") + WAV_EXTENSION);

        command.append(" \"")
               .append(completeRecordingPath.toAbsolutePath().toString())
               .append("\"");

//
//        URL url = getClass().getResource("/script/combine.sh");
//        Path script = Paths.get(url.toURI());
//        System.out.println(script.toString());
//        System.out.println(command.toString());
//
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command.toString());
        try {
            Process process = builder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        CompleteName completeName = new CompleteName(userRequestedName);
        completeName.setExemplar(new CompleteNameRecording(completeRecordingPath));
        manager.addCompleteName(completeName);
        return null;
    }


}
