package namesayer.util;

import javafx.concurrent.Task;
import namesayer.model.PartialName;
import namesayer.model.PartialRecording;
import namesayer.persist.NameStorageManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static namesayer.persist.Config.DATABASE_FOLDER;

public class DatabaseImporter extends Task<Void>{
	
	private File _file;
	private final File database = DATABASE_FOLDER.toFile();
	
	public DatabaseImporter(File file) {
		_file=file;
	}

    @Override
    protected Void call() throws Exception {
        URL url = getClass().getResource("/script/VolumeEdit.sh");
        Path script = Paths.get(url.toURI());

        for(File f : _file.listFiles()){
            if(getFileType(f).equals("wav")){

                String command = "sh "+ script.toString() + " " + f.getAbsolutePath();
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
                    System.out.println(result);
                    process.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
                f.renameTo(new File(database + "//" + f.getName()));
                File file=new File(database + "//" + f.getName());
                Matcher matcher = Pattern.compile("[a-zA-Z]+(?:\\.wav)").matcher(f.getName());
                String name = "unrecognized";
                if (matcher.find()) {
                    name = matcher.group(0).replace(".wav", "");
                }
                if(!name.isEmpty()) {
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    PartialName partialName = new PartialName(name);
                    partialName.addRecording(new PartialRecording(file.toPath()));

                    //System.out.println(_file.getAbsolutePath());
                    NameStorageManager.getInstance().addNewPartialName(partialName);
                }
            }
        }
        return null;
    }


	public String getFileType(File file){
	    String fileName = file.getName();
	    String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
	    System.out.println(suffix);
	    return  suffix;
    }


}
