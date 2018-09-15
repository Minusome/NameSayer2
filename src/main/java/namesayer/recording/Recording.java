package namesayer.recording;

import java.io.File;
import java.util.List;

public class Recording extends File {
    private int HAVEMULTIVERSION = 0;
    private String nameWithExtension;
    private String Fullname;
    private String pureName;
    private boolean isUserCreation = false;

    public Recording(String pathname) {
        super(pathname);
        Fullname = this.getName();
        setFileNameNoEx(Fullname);
    }

    public Recording(String pathname,int i) {
        super(pathname);
        Fullname = this.getName();
        setFileNameNoEx(Fullname);
        isUserCreation = true;
    }

    public void setFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            int hyphen = filename.lastIndexOf('_');
            if (hyphen!=-1&&dot!=-1) {
                nameWithExtension = filename.substring(hyphen+1, dot)+".wav";
                pureName = filename.substring(hyphen+1, dot);
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

    public String getNameWithwav() {
        return nameWithExtension;
    }

    public String getPureName() {
        return pureName;
    }
}
