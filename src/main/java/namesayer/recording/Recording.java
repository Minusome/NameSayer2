package namesayer.recording;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.nio.file.Path;

public class Recording {

    private Path file;
    private boolean isCreatedByUser;
    //This needs to be here to prevent garbage collection
    private MediaPlayer player;

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
