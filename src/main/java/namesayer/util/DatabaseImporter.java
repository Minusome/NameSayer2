package namesayer.util;

import javafx.concurrent.Task;
import namesayer.model.PartialName;
import namesayer.model.PartialRecording;
import namesayer.persist.NameStorageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static namesayer.persist.Config.DATABASE_FOLDER;
import static namesayer.persist.Config.SCRIPT_FILE;

/**
 * A task responsible for add names from a different database into
 * the default one maintained by our application
 */

public class DatabaseImporter extends Task<Void>{
	
	private File _file;

	
	/**
     * Set the folder that contains name recordings
	 */
	public DatabaseImporter(File file) {
		_file=file;
	}

	/**
	 *  Edit audio file including silence removing and adjusting volume
	 */
    @Override
    protected Void call() throws Exception {

        for(File f : _file.listFiles()){
            if(getFileType(f).equals("wav")){

                String command = SCRIPT_FILE.toAbsolutePath() + " " + f.getAbsolutePath();
                ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);


                builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
                builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
                builder.redirectError(ProcessBuilder.Redirect.INHERIT);
                try {
                    Process process = builder.start();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    StringBuilder builder2 = new StringBuilder();
                    String line = null;
                    while ( (line = reader.readLine()) != null) {
                        builder2.append(line);
                        builder2.append(System.getProperty("line.separator"));
                    }
                    String result = builder2.toString();
                    process.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                Path newPath = DATABASE_FOLDER.resolve(f.getName());
                File newFile = new File(newPath.toUri());
                if (!newFile.exists()) {
                    Files.copy(f.toPath(), newPath);
                }
                Matcher matcher = Pattern.compile("[a-zA-Z]+(?:\\.wav)").matcher(f.getName());
                String name = "unrecognized";
                if (matcher.find()) {
                    name = matcher.group(0).replace(".wav", "");
                }
                if(!name.isEmpty()) {
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    PartialName partialName = new PartialName(name);
                    partialName.addRecording(new PartialRecording(newPath));
                    NameStorageManager.getInstance().addNewPartialName(partialName);
                }
            }
        }
        return null;
    }

    /**
     * Return the file type
     */
	public String getFileType(File file){
	    String fileName = file.getName();
	    String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
	    return  suffix;
    }


}
