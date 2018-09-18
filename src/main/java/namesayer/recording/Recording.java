package namesayer.recording;


import java.nio.file.Path;

public class Recording {

    private Path file;
    private boolean isCreatedByUser;

    public Recording(Path file){
        this(file, false);
    }

    public Recording(Path file, boolean isCreatedByUser){
        this.file = file;
        this.isCreatedByUser = isCreatedByUser;
    }

    public void playAudio(){

    }

    @Override
    public String toString(){
        return file.toString();
    }


}
