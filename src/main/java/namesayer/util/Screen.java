package namesayer.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;

/**
 * Helper enum for transitions between screens
 */

public enum Screen {

    MAIN_MENU("/MenuScreen.fxml"),
    BROWSE_DATABASE_SCREEN("/BrowseDatabaseScreen.fxml"),
    ASSESSMENT_SCREEN("/AssessmentScreen.fxml"),
    PRACTISE_SCREEN("/PractiseScreen.fxml"),
    NAME_SELECT_SCREEN("/NameSelectScreen.fxml"),
    STATS_SCREEN("/StatsScreen.fxml"),
	MENU_HELP_SCREEN("/MenuHelpScreen.fxml"),
    MIC_TEST_ALERT("/MicTestAlert.fxml");

    private URL url;

    Screen(String url) {
        this.url = getClass().getResource(url);
    }

    public FXMLLoader getLoader() {
        return new FXMLLoader(url);
    }

    public void loadWithNode(Node node) {
        try {
            node.getScene().setRoot(getLoader().load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
