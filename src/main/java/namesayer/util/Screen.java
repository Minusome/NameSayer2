package namesayer.util;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.net.URL;


public class SceneLoader {

    private static SceneLoader instance;
    private Stage parentStage;

    public enum Screen {
        MAIN_MENU("/MenuScreen.fxml"),
        BROWSE_DATABASE_SCREEN("/RecordingScreen.fxml"),
        ASSESSMENT_SCREEN("/AssessmentScreen.fxml"),
        PRACTISE_SCREEN("/PractiseScreen.fxml"),
        NAME_SELECT_SCREEN("/NameSelectScreen.fxml"),
        STATS_SCREEN("/StatsScreen.fxml");

        private URL url;

        Screen(String url) {
            this.url = getClass().getResource(url);
        }

        public FXMLLoader getLoader() {
            return new FXMLLoader(url);
        }
    }

}
