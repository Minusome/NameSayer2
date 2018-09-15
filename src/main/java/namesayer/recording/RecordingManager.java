package namesayer.recording;

import javax.lang.model.element.Name;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordingManager implements FileManager{

    private static RecordingManager instance = null;
    private List<Recording> recordingList = new ArrayList<>();
    private List<File> DirectoryList= new ArrayList<>();
    private List<Recording> newRecordings = new ArrayList<>();
    private String currentPath = System.getProperty("user.dir");
    public static RecordingManager getInstance(String path) {
        if (instance == null) {
            instance = new RecordingManager(path);
        }
        return instance;
    }

    //create a new RecordingManager for all the recordings in that path
    private RecordingManager(String path) {
        //make folders and stuff
        initialise(path);
        newRecording(new Recording("C:/Users/zhugu/Pictures/123"));

    }

    @Override
    public Recording newRecording(Recording RecordingInSystem) {
        Recording newRecording = new Recording(currentPath+"/userCreated/"+RecordingInSystem.getName());
        newRecording.mkdirs();//make a directory for this user created recording
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(df.format(day));
        StartRecording();//start the recording in linux command line
        return newRecording;
    }

    private void StartRecording() {
    }

    @Override
    public boolean deleteRecording(Recording deleteThisRecording) {
        return false;
    }

    @Override
    public boolean RecordingExist(Recording r) {
        return false;
    }

    //return a list of all the names
    public List<String> getListOfNames() {
        //dummy values for now
        List<String> names = new ArrayList<>();
        for (int i = 0; i <= 100; i++) {
            names.add("test"+i);
        }
        return names;
    }

    @Override
    public List<Recording> getNewRecordingList() {
        return null;
    }

    @Override
    public void RandomiseSelectedList() {

    }

    @Override
    public List<Recording> getRecordingsThisName(String name) {
        return null;
    }

    //initialise all folders
    public void initialise(String path){
        currentPath = path;
    Recording temp = new Recording(path);
    File[] fileList = temp.listFiles();
    int i = 0;
    while(i!=fileList.length){
        fileList[i].mkdirs();//create a folder for every recording
        i++;
    }
    File[] newList = temp.listFiles();

        int j = 0;
        while(j!=fileList.length){
            if(fileList[j].isDirectory()){
                DirectoryList.add(fileList[j]); //create a folder for every recording
            }
            j++;
        }
        //put corresponding recordings into that directory
        for(File f:DirectoryList){
            Integer version = 1;
            for(File e:fileList){
                if(f.getName().equals(e.getName())){
                    e.renameTo(new File(f.getPath()+e.getName()+version));
                    version++;
                }
            }
        }
    }


}
