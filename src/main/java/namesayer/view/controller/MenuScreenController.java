package namesayer.view.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import namesayer.session.Session;

import java.io.IOException;

import static namesayer.session.Session.SessionType.ASSESSMENT;
import static namesayer.session.Session.SessionType.PRACTISE;
import static namesayer.util.Screen.BROWSE_DATABASE_SCREEN;
import static namesayer.util.Screen.NAME_SELECT_SCREEN;
import static namesayer.util.Screen.STATS_SCREEN;
import static namesayer.util.Screen.MENU_HELP_SCREEN;


public class MenuScreenController {


    @FXML private JFXButton practiceButton;
    @FXML private GridPane parentPane;
    private JFXSnackbar bar;


    public void initialize() {
        practiceButton.setDisable(false);
        bar = new JFXSnackbar(parentPane);
        bar.getStylesheets().addAll("/css/Material.css");
    }

    /**
     * Transition to the Name Selection Screen
     */
    public void onPracticeModeClicked(MouseEvent mouseEvent) throws IOException {
        loadSelection(PRACTISE);
    }

    /**
     * Transition to the Name Selection Screen
     */
    public void onAssessModeClicked(MouseEvent mouseEvent) throws IOException {
        loadSelection(ASSESSMENT);
    }

    /**
     * Sets the [Practice, Assessment] Mode of the Name Section Screen controller
     *
     */
    public void loadSelection(Session.SessionType type) throws IOException {
        FXMLLoader loader =  NAME_SELECT_SCREEN.getLoader();
        practiceButton.getScene().setRoot(loader.load());
        NameSelectScreenController controller = loader.getController();
        controller.setSessionType(type);
    }

    public void onBrowseModeClicked(MouseEvent e) throws IOException {
        BROWSE_DATABASE_SCREEN.loadWithNode(practiceButton);
    }

    public void onStatisticsClicked(MouseEvent mouseEvent) throws IOException {
        STATS_SCREEN.loadWithNode(practiceButton);
    }
    
    public void onHelpButtonClicked(MouseEvent e) throws IOException {
    	MENU_HELP_SCREEN.loadWithNode(practiceButton);
    }
}
