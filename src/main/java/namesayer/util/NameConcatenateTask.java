package namesayer.util;

import javafx.concurrent.Task;
import namesayer.model.CompleteName;
import namesayer.model.CompleteRecording;
import namesayer.model.PartialName;
import namesayer.model.PartialRecording;
import namesayer.persist.NameStorageManager;
import namesayer.persist.Session;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private Session session;

    public NameConcatenateTask(Session session, String userRequestedNames) {
        this.userRequestedName = userRequestedNames;
        this.session = session;
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

        URL url = getClass().getResource("/script/combine.sh");
        Path script = Paths.get(url.toURI());
        StringBuilder stringBuilder = new StringBuilder();
        for (PartialName name : discoveredNames) {
            //TODO make this get the recording with the best quality
            PartialRecording partialRecording = name.getRecordings().get(0);
            stringBuilder.append(partialRecording.getRecordingPath().toAbsolutePath().toString()).append(" ");
        }
        //TODO change the naming convention for combined names

        Path completeRecordingPath = DATABSE_FOLDER.resolve(COMBINED_NAMES)
                                                   .resolve(userRequestedName.replace(" ", "_") + WAV_EXTENSION);

        stringBuilder.append(completeRecordingPath.toAbsolutePath().toString()).append(" ");

        System.out.println(script.toString() + " " + stringBuilder.toString());

        try {
            ProcessBuilder builder = new ProcessBuilder(script.toString(), stringBuilder.toString());
            builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            builder.redirectError(ProcessBuilder.Redirect.INHERIT);
            builder.start().waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        CompleteName completeName = new CompleteName(userRequestedName);
        completeName.setExemplar(new CompleteRecording(completeRecordingPath));
        session.add(completeName);
        return null;
    }


}
