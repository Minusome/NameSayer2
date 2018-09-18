package namesayer;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import namesayer.recording.Name;
import namesayer.recording.Recording;


public class RecordingScreenController {
    @FXML public JFXListView<Name> selectedNamesListView;
    @FXML public JFXListView<Recording> savedRecordingListView;
    @FXML public JFXListView<Recording> newRecordingListView;
    private ObservableList<Name> selectedNames = null;


    public void injectSelectedNames(ObservableList<Name> names) {
        selectedNames = names;
        selectedNamesListView.setItems(selectedNames);
    }

    @FXML
    public void onSelectedNameClicked(MouseEvent mouseEvent) {
        Name name = selectedNamesListView.getSelectionModel().getSelectedItem();
        savedRecordingListView.setItems(FXCollections.observableArrayList(name.getSavedRecordings()));
        newRecordingListView.setItems(FXCollections.observableArrayList(name.getTempRecordings()));
    }
}
