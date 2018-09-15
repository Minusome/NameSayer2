package namesayer.recording;

import javax.lang.model.element.Name;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class RecordingManager implements FileManager{

    private static RecordingManager instance = null;
    private List<Recording> DataBaseRecordings = new ArrayList<>();
    private List<File> DirectoryList= new ArrayList<>();
    private List<Recording> newRecordings = new ArrayList<>();
    private List<Recording> _selectedRecordings = new ArrayList<>();
    private String currentPath;
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
        //newRecording(new Recording("/afs/ec.auckland.ac.nz/users/g/z/gzhu282/unixhome/workspace/Hanyu.wav/se206_17-5-2018_14-29-11_Hanyu.wav"));

    }

    @Override
    public Recording newRecording(Recording RecordingInSystem) {
        Recording newRecording = new Recording(currentPath+"/userCreated/"+trimOut(RecordingInSystem.getName()));
        newRecording.mkdirs();//make a directory for this user created recording
        Date day=new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        StartRecording(currentPath+"/userAttemps/"+df.format(day)+RecordingInSystem.getNameWithwav());//start the recording in linux command line
        return newRecording;
    }

    private void StartRecording(String pathName) {
        String str ="ffmpeg -f alsa -i default -t 5 "+pathName;//some linux comand to make the wav file
        StartAnewProcess(str);
        Recording temp = new Recording(pathName);
//    	temp.renameTo(pathName);
        newRecordings.add(new Recording(pathName));
    }

    @Override
    public boolean deleteRecording(Recording deleteThisRecording) {
        if(DataBaseRecordings.contains(deleteThisRecording)) {
            DataBaseRecordings.remove(deleteThisRecording);
        }else {
            newRecordings.remove(deleteThisRecording);
        }
        return deleteThisRecording.delete();
    }

    @Override
    public boolean RecordingExist(Recording r) {
        if(DataBaseRecordings.contains(r)||newRecordings.contains(r)) {
            return true;
        }else {
            return false;
        }
    }

    //return a list of all the names
    public List<String> getListOfNames() {
        //dummy values for now
        List<String> names = new ArrayList<>();
        for(Recording r:DataBaseRecordings) {
            names.add(r.getPureName());
        }
        return names;
    }

    @Override
    public List<Recording> getNewRecordingList() {
        return newRecordings;
    }

    public static List<Recording> RandomiseSelectedList(List<Recording> selectedRecording) {
        Collections.shuffle(selectedRecording);
        return selectedRecording;
    }

    //input is a pure name without any extension or prefix
    @Override
    public List<Recording> getRecordingsInDatabaseThisName(String name) {
        List<Recording> temp = new ArrayList<>();
        for(Recording r:DataBaseRecordings) {
            if(r.getPureName().equals(name)) {
                temp.add(r);
            }
        }
        return temp;
    }

    @Override
    public List<Recording> getRecordingsInUserAttempsForThisName(String name){
        List<Recording> temp = new ArrayList<>();
        for(Recording r:newRecordings) {
            if(r.getPureName().equals(name)) {
                temp.add(r);
            }
        }
        return temp;
    }


    //Initialize all folders
    public void initialise(String path){
        currentPath = path;
        new File(path+"/userAttemps").mkdirs();
        File[] tempList = new File(path).listFiles();
        //populate folders
        for(File f:tempList) {
            new File(path+"/"+trimOut(f.getName())).mkdirs();
            DataBaseRecordings.add(new Recording(f.getAbsolutePath()));//add all recordings in the database
            for(File e:tempList) {
                if(trimOut(f.getName()).equals(trimOut(e.getName()))) {
                    e.renameTo(new File(path+"/"+trimOut(f.getName())+"/"+e.getName()));
                }
            }
        }

    }
    public static String trimOut(String str) {
        if(str.contains("_")) {
            String newString = str.substring(str.lastIndexOf("_")+1, str.length());
            str=newString;
        }
        return str;

    }

    @Override
    public void RandomiseSelectedList() {
        // TODO Auto-generated method stub
        //empty method
    }

    public void SetSelectedRecording(List<Recording> recording) {
        _selectedRecordings = new ArrayList<>();
        _selectedRecordings = recording;
    }


    public void StartAnewProcess(String command) {
        ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",command);
        try {
            Process process = builder.start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
