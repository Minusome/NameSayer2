package namesayer;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import namesayer.recording.Name;
import namesayer.recording.NameStorageManager;
import namesayer.util.EmptySelectionModel;

import java.io.IOException;

public class NameSelectScreenController {

    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<Name> nameListView;
    @FXML private JFXToggleButton randomToggle;

    private NameStorageManager nameStorageManager;
    private ObservableList<Name> listOfNames;

    public void initialize() {
        nameStorageManager = NameStorageManager.getInstance();
        listOfNames = nameStorageManager.getNamesList();

        nameListView.setCellFactory(value -> new JFXListCell<Name>() {
            JFXCheckBox checkBox = new JFXCheckBox();
            Name recycledName = null;

            @Override
            public void updateItem(Name item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    if (recycledName != null) {
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
        Parent root = FXMLLoader.load(getClass().getResource("/RecordingScreen.fxml"));
        Scene scene = nameSearchBar.getScene();
        scene.setRoot(root);
    }
}
