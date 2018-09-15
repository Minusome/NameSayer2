package namesayer;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import namesayer.recording.Name;
import namesayer.recording.RecordingManager;
import namesayer.util.EmptySelectionModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NameSelectScreenController implements Initializable {

    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<Name> nameListView;

    private RecordingManager recordingManager;
    private ObservableList<Name> listOfNames;

    public void initialize(URL location, ResourceBundle resources) {
        recordingManager = RecordingManager.getInstance();
        listOfNames = FXCollections.observableArrayList(recordingManager.getListOfNames());


        nameListView.setCellFactory(value -> new JFXListCell<Name>() {
            JFXCheckBox checkBox = new JFXCheckBox();
            Name recycledName = null;

            @Override
            public void updateItem(Name item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    if (recycledName != null){
                        checkBox.selectedProperty().unbindBidirectional(recycledName.selectedProperty());
                    }
                    checkBox.selectedProperty().bindBidirectional(item.selectedProperty());
                    recycledName = item;
                    setGraphic(checkBox);
                }
            }
        });
        nameListView.setSelectionModel(new EmptySelectionModel<>());
        nameListView.setItems(listOfNames);
        nameListView.setExpanded(false);
    }


    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
        Scene scene = nameSearchBar.getScene();
        Parent root = FXMLLoader.load(getClass().getResource("/RecordingScreen.fxml"));
        scene.setRoot(root);
    }
}
