package namesayer.view.alert;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import namesayer.persist.SessionStorageManager;
import namesayer.session.AssessmentSession;
import namesayer.session.PractiseSession;
import namesayer.session.Session;

import java.io.IOException;

import static namesayer.util.Screen.MAIN_MENU;

public class SaveAlert extends JFXAlert {

    private AssessmentSession assessmentSession;
    private PractiseSession practiseSession;
    private SessionStorageManager manager = SessionStorageManager.getInstance();

    public SaveAlert(Stage stage, AssessmentSession session) {
        super(stage);
        assessmentSession = session;
        loadContent(session, this::saveAssessment, this::removeAssessment);
    }

    public SaveAlert(Stage stage, PractiseSession session) {
        super(stage);
        practiseSession = session;
        loadContent(session, this::savePractise, this::removePractise);
    }

    private void loadContent(Session session, Runnable saveStrategy, Runnable removeStrategy) {
        this.initModality(Modality.WINDOW_MODAL);
        this.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Would you like to save this session?"));
        JFXTextField field = new JFXTextField();
        JFXButton cancelButton = new JFXButton("Cancel");
        JFXButton saveButton = new JFXButton("Save");
        JFXButton dontSaveButton = new JFXButton("Don't Save");
        saveButton.setDisable(true);
        String text = session.getSessionName();
        if (text == null || text.isEmpty()) {
            text = "Please enter a name to save this session";
        } else {
            field.setEditable(false);
            saveButton.setDisable(false);
        }
        field.setPromptText(text);
        field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                saveButton.setDisable(true);
            } else {
                saveButton.setDisable(false);
            }
        });
        layout.setBody(field);
        cancelButton.setOnAction(event -> {
            this.hideWithAnimation();
        });
        saveButton.setOnAction(event -> {
            session.setSessionName(field.getText());
            saveStrategy.run();
            previousScreen(layout);
            this.hideWithAnimation();
        });
        dontSaveButton.setOnAction(event -> {
            removeStrategy.run();
            previousScreen(layout);
            this.hideWithAnimation();
        });
        layout.setActions(saveButton, dontSaveButton, cancelButton);
        this.setContent(layout);
    }

    private void previousScreen(Node node) {
        MAIN_MENU.loadWithNode(node);
    }


    private void saveAssessment() {
        manager.saveSession(assessmentSession);
    }

    private void savePractise() {
        manager.saveSession(practiseSession);
    }

    private void removeAssessment() {
        manager.removeSession(assessmentSession);
    }

    private void removePractise() {
        manager.removeSession(practiseSession);
    }
}
