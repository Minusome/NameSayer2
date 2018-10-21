package namesayer.view.alert;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import namesayer.persist.SessionStorageManager;
import namesayer.session.AssessmentSession;
import namesayer.session.PractiseSession;
import namesayer.session.Session;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static namesayer.util.Screen.MAIN_MENU;

public class SaveAlert extends JFXAlert {

    private AssessmentSession assessmentSession;
    private PractiseSession practiseSession;
    private SessionStorageManager manager = SessionStorageManager.getInstance();

    public SaveAlert(Stage stage, AssessmentSession session) {
        super(stage);
        assessmentSession = session;
        loadContent(stage, session, this::saveAssessment, this::removeAssessment);
    }

    public SaveAlert(Stage stage, PractiseSession session) {
        super(stage);
        practiseSession = session;
        loadContent(stage, session, this::savePractise, this::removePractise);
    }

    private void loadContent(Stage stage ,Session session, Runnable saveStrategy, Runnable removeStrategy) {
        this.initModality(Modality.WINDOW_MODAL);
        JFXDialogLayout layout = new JFXDialogLayout();
        this.setContent(layout);
        layout.setHeading(new Label("Would you like to save this session?"));
        JFXButton cancelButton = new JFXButton("Cancel");
        JFXButton saveButton = new JFXButton("Save");
        JFXButton dontSaveButton = new JFXButton("Don't Save");
        AtomicReference<String> sessionName = new AtomicReference<>(session.getSessionName());
        boolean notSaved = (sessionName.get() == null) || (sessionName.get().isEmpty());
        if (notSaved) {
            JFXTextField field = new JFXTextField();
            field.setPromptText("Please enter a name to save this session");
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                sessionName.set(newValue);
                if (newValue.isEmpty()) {
                    saveButton.setDisable(true);
                } else {
                    saveButton.setDisable(false);
                }
            });
            saveButton.setDisable(true);
            layout.setBody(field);
        } else {
            Label label = new Label("The name of this session is " + sessionName);
            saveButton.setDisable(false);
            layout.setBody(label);
        }

        cancelButton.setOnAction(event -> {
            this.hideWithAnimation();
        });
        saveButton.setOnAction(event -> {
            if (notSaved) {
                session.setSessionName(sessionName.get());
            }
            saveStrategy.run();
            previousScreen(stage);
            this.hideWithAnimation();
        });
        dontSaveButton.setOnAction(event -> {
            removeStrategy.run();
            previousScreen(stage);
            this.hideWithAnimation();
        });
        layout.setActions(saveButton, dontSaveButton, cancelButton);
        layout.setCache(true);
        layout.setCacheShape(true);
        layout.setCacheHint(CacheHint.SPEED);
    }

    private void previousScreen(Stage stage) {
        FXMLLoader loader = MAIN_MENU.getLoader();
        try {
            stage.getScene().setRoot(loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
