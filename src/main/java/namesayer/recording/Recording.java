package namesayer.recording;

import java.io.File;
import java.util.List;

public class Recording extends File {
    private List<Recording> Multiversion = null;
    private int HAVEMULTIVERSION = 0;
    private String name;

    public Recording(String pathname) {
        super(pathname);
    }

    public List<Recording> getRecordingsInthatPath(String pathname){
        return null;
    }

    public void setFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            int hyphen = filename.lastIndexOf('_');
            if ((dot > -1) && (dot < (filename.length()))) {
                name = filename.substring(hyphen, dot);
            }
        }
        name = "invalid name";
    }

    public boolean haveMultiVersion(){
        if (HAVEMULTIVERSION == 0){
            return false;
        }else{
            return true;
        }
    }

    public void playTheRecording(){

    }

    public void deleteTheRecording(){

    }
}
