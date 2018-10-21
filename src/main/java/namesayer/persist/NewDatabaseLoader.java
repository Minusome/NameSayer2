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
	boolean succeed =true;
	
	public boolean editFile(File file) {
		_file=file;
		//path=file.getAbsolutePath();
		load(path);
		return succeed;
	}

	public void load(String path) {
		
		
		Task<Boolean> task = new Task<Boolean>() {



			@Override
			protected Boolean call() throws Exception {
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
                        f.renameTo(new File(databse + "//" + f.getName()));
                        File file=new File(databse + "//" + f.getName());
                        String name = f.getName().replace(".wav", "");
                        if(! name.isEmpty()) {
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
			
			@Override
			protected void succeeded() {
				updateMessage("Successful load file: "+_file.getName());
				
			}

			@Override
			protected void failed() {
				super.failed();
				updateMessage("Can't load file: "+_file.getName());
				succeed=false;
			}
			
		};
		new Thread(task).start();
	}

	public String getFileType(File file){
	    String fileName = file.getName();
	    String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
	    System.out.println(suffix);

	    return  suffix;
    }
	
	
	
}
