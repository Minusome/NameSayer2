package namesayer;

import com.jfoenix.controls.JFXListView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import namesayer.recording.Name;
import namesayer.recording.NameStorageManager;
import namesayer.recording.Recording;

import java.io.IOException;


public class RecordingScreenController {
    @FXML private JFXListView<Name> selectedNamesListView;
    @FXML private JFXListView<Recording> savedRecordingListView;
    @FXML private JFXListView<Recording> newRecordingListView;
    @FXML private HBox actionViewContainer;

    private NameStorageManager storageManager = NameStorageManager.getInstance();
    private ObservableList<Name> selectedNames;
    private Name selectedName;


    public void initialize() {
        selectedNames = storageManager.getSelectedNamesList();
        selectedNamesListView.setItems(selectedNames);
    }

    @FXML
    public void onSelectedNameClicked(MouseEvent mouseEvent) {
        selectedName = selectedNamesListView.getSelectionModel().getSelectedItem();
        if (selectedName != null) {
            savedRecordingListView.setItems(selectedName.getSavedRecordings());
            newRecordingListView.setItems(selectedName.getTempRecordings());
        }
    }


    public void onSavedRecordingClicked(MouseEvent mouseEvent) {
        displayPlayerFragment(savedRecordingListView.getSelectionModel().getSelectedItem());
    }

    public void onNewRecordingClicked(MouseEvent mouseEvent) {
        displayPlayerFragment(newRecordingListView.getSelectionModel().getSelectedItem());
    }

    public void displayPlayerFragment(Recording recording) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PlayerFragment.fxml"));
            Parent root = loader.load();
            PlayerFragmentController controller = loader.getController();
            controller.injectRecording(recording);
            actionViewContainer.getChildren().setAll(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void onSaveButtonClicked(MouseEvent mouseEvent) {
        selectedName.saveTempRecordings();
    }

    public void onNewButtonClicked(MouseEvent mouseEvent) throws IOException {
        if (selectedNames != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RecordingFragment.fxml"));
            Parent root = loader.load();
            RecordingFragmentController controller = loader.getController();
            controller.injectName(selectedName);
            actionViewContainer.getChildren().setAll(root);
        }
    }

}