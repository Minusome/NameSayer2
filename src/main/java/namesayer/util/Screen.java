package namesayer.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;


public enum Screen {

    MAIN_MENU("/MenuScreen.fxml"),
    BROWSE_DATABASE_SCREEN("/RecordingScreen.fxml"),
    ASSESSMENT_SCREEN("/AssessmentScreen.fxml"),
    PRACTISE_SCREEN("/PractiseScreen.fxml"),
    NAME_SELECT_SCREEN("/NameSelectScreen.fxml"),
    STATS_SCREEN("/StatsScreen.fxml");


    private Parent root;
    private FXMLLoader loader;

    Screen(String url) {
        loader = new FXMLLoader(getClass().getResource(url));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Parent getRoot() {
        return root;
    }

    public <T> T getController() {
        return loader.getController();
    }


    public void loadWithNode(Node node) {
        node.getScene().setRoot(root);
    }
}


