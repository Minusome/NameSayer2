package namesayer.persist;

import javafx.concurrent.Task;
import namesayer.model.PartialName;
import namesayer.model.PartialRecording;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static namesayer.persist.Config.DATABASE_FOLDER;

public class NewDatabaseLoader {
	
	private File _file;
	private String path;
	private final File databse = DATABASE_FOLDER.toFile();
	
	public void editFile(File file) {
		_file=file;
		path=file.getAbsolutePath();
		load(path);
	}

	public void load(String path) {
		
		
		Task<Boolean> task = new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {
				URL url = getClass().getResource("/script/VolumeEdit.sh");
				Path script = Paths.get(url.toURI());
				String command = "sh "+ script.toString() + " " + path;
	            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", command);
	            builder.redirectInput(ProcessBuilder.Redirect.INHERIT);
	            builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
	            builder.redirectError(ProcessBuilder.Redirect.INHERIT);
	            
	            try {
	                Process process = builder.start();
	                BufferedReader reader =

	                      new BufferedReader(new InputStreamReader(process.getInputStream()));

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
				_file.renameTo(new File(databse + "//" + _file.getName()));
	            File file=new File(databse + "//" + _file.getName());
				String name = _file.getName().replace(".wav", "");
				if(! name.isEmpty()) {
					name = name.substring(0, 1).toUpperCase() + name.substring(1);
					PartialName partialName = new PartialName(name);
					partialName.addRecording(new PartialRecording(file.toPath()));
					System.out.println(_file.toPath().toString());
					System.out.println(_file.getAbsolutePath());
					NameStorageManager.getInstance().addNewPartialName(partialName);

				}
				return null;
			}
			
			@Override
			protected void succeeded() {
				updateMessage("Successful load file: "+_file.getName());
				
			}

			@Override
			protected void failed() {
				super.failed();
				updateMessage("Can't load file: "+_file.getName());
			}
			
		};
		new Thread(task).start();
	}
	
	
	
	
}
