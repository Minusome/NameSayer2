package namesayer.recording;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
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
    }

    @Override
    public Recording newRecording(Recording RecordingInSystem) {
        StringBuilder sb = new StringBuilder(RecordingInSystem.getAbsolutePath());
        sb.insert(RecordingInSystem.getAbsolutePath().lastIndexOf("/"),"/userCreated");
        Recording newRecording = new Recording(sb.toString());//make new recordings in the userCreated
        newRecording.mkdirs();//make a directory for this user created recording

        StartRecording();//start the recording in linux command line

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
        return new ArrayList<String>() {{
            add("test1");
            add("test2");
            add("test3");
            add("test4");
            add("test5");
            add("test6");
            add("test1");
            add("test2");
            add("test3");
            add("test4");
            add("test5");
            add("test6");
            add("test1");
            add("test2");
            add("test3");
            add("test4");
            add("test5");
            add("test6");
            add("test1");
            add("test2");
            add("test3");
            add("test4");
            add("test5");
            add("test6");

        }};
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
