package namesayer.recording;

import java.io.File;
import java.util.List;

public class Recording extends File {
    private int HAVEMULTIVERSION = 0;
    private String name;

    public Recording(String pathname) {
        super(pathname);
    }

    public void setFileNameNoEx(String filename) {
        name = "invalid name";
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            int hyphen = filename.lastIndexOf('_');
            if ((dot > -1) && (dot < (filename.length()))) {
                name = filename.substring(hyphen, dot);
            }
        }

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
