package namesayer;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import namesayer.recording.Name;
import namesayer.recording.NameStorageManager;
import namesayer.util.EmptySelectionModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NameSelectScreenController implements Initializable {

    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<Name> nameListView;
    @FXML private JFXToggleButton randomToggle;

    private NameStorageManager nameStorageManager;
    private ObservableList<Name> listOfNames;

    public void initialize(URL location, ResourceBundle resources) {
        nameStorageManager = NameStorageManager.getInstance();
        listOfNames = FXCollections.observableArrayList(nameStorageManager.getNamesList());

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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RecordingScreen.fxml"));
        Parent root = loader.load();
        RecordingScreenController controller = loader.getController();
        controller.injectSelectedNames(listOfNames.stream()
                                                  .filter(name -> name.getSelected())
                                                  .collect(Collectors.toCollection(FXCollections::observableArrayList)));

        Scene scene = nameSearchBar.getScene();
        scene.setRoot(root);
    }
}
