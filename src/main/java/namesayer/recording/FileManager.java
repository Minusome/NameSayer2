package namesayer.recording;

import java.util.List;

public interface FileManager {


    //make new recordings
    public Recording newRecording(Recording oldrecording);
    //delete this recording and return boolean to indicate whether it is successful
    public boolean deleteRecording(Recording deleteThisRecording);
    //check for existence of a recording
    public boolean RecordingExist(Recording r);

    //return a list of all the names
    public List<String> getListOfNames();

    //return new recordings made by the user
    public List<Recording> getNewRecordingList();

    //randomise the selected Recording list
    public void RandomiseSelectedList();

    //get the recordings for the name specified in database
    public List<Recording> getRecordingsInDatabaseThisName(String name);
    //get the recordings for the name specified in user's attemps
    public List<Recording> getRecordingsInUserAttempsForThisName(String name);
}
