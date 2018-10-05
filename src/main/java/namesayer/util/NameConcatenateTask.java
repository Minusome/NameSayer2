package namesayer.util;

import javafx.concurrent.Task;
import namesayer.model.*;
import namesayer.persist.NameStorageManager;
import namesayer.session.Session;

import java.util.ArrayList;
import java.util.List;


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
        List<PartialRecording> discovered = new ArrayList<>();
        for (String s : components) {
            PartialName name = manager.findPartialNameFromString(s);
            if (name != null) {
                //TODO just getting first name for now, change quality
                discovered.add(name.getRecordings().get(0));
            }
        }
        if (discovered.size() == 0) {
            return null;
        }


//        URL url = getClass().getResource("/script/combine.sh");
//        Path script = Paths.get(url.toURI());
//        StringBuilder stringBuilder = new StringBuilder();
//        for (PartialName name : discovered) {
//
//            PartialRecording partialRecording = name.getRecordings().get(0);
//            stringBuilder.append(partialRecording.getRecordingPath().toAbsolutePath().toString()).append(" ");
//        }
//
//
//        Path completeRecordingPath = DATABSE_FOLDER.resolve(COMBINED_NAMES)
//                                                   .resolve(userRequestedName.replace(" ", "_") + WAV_EXTENSION);
//
//        stringBuilder.append(completeRecordingPath.toAbsolutePath().toString()).append(" ");
//
//        System.out.println(script.toString() + " " + stringBuilder.toString());
//
//        try {
//            ProcessBuilder builder = new ProcessBuilder(script.toString(), stringBuilder.toString());
//            builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
//            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
//            builder.redirectError(ProcessBuilder.Redirect.INHERIT);
//            Process process = builder.start();
//
//            BufferedReader reader =
//                    new BufferedReader(new InputStreamReader(process.getInputStream()));
//            StringBuilder builder2 = new StringBuilder();
//            String line = null;
//            while ( (line = reader.readLine()) != null) {
//                builder2.append(line);
//                builder2.append(System.getProperty("line.separator"));
//            }
//            String result = builder2.toString();
//            System.out.println(result);
//            process.waitFor();
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }

        CompositeName compositeName = new CompositeName(userRequestedName);
        compositeName.setExemplar(new Exemplar(discovered));
        session.addName(compositeName);
        return null;
    }


}
