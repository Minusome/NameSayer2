package namesayer;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import namesayer.recording.Name;
import namesayer.recording.NameStorageManager;
import namesayer.util.EmptySelectionModel;

import java.io.IOException;
import java.util.stream.Collectors;

public class NameSelectScreenController {

    @FXML private GridPane parentPane;
    @FXML private JFXButton nextButton;
    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<Name> nameListView;
    @FXML private JFXToggleButton randomToggle;
    private JFXSnackbar bar;

    private NameStorageManager nameStorageManager;
    private ObservableList<Name> listOfNames;
    private static boolean randomSelected = false;

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
        bar = new JFXSnackbar(parentPane);
        bar.getStylesheets().addAll("/css/Material.css");
    }


    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
        if (nameStorageManager.getSelectedNamesList().isEmpty()){
            bar.enqueue(new JFXSnackbar.SnackbarEvent("Please select a name first"));
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/RecordingScreen.fxml"));
        Scene scene = nameSearchBar.getScene();
        scene.setRoot(root);
    }

    public void setRandom() {
        if (randomToggle.isDisableAnimation()) {
            randomSelected = false;
        } else {
            randomSelected = true;
        }
    }

    public static boolean RandomToggleOn() {
        return randomSelected;
    }


    @FXML
    public void onSearchBarKeyTyped(KeyEvent keyEvent) {
        String userInput = nameSearchBar.getCharacters().toString().toLowerCase();
        if (userInput.isEmpty()) {
            listOfNames = nameStorageManager.getNamesList();
        } else {
            listOfNames = listOfNames.stream()
                                     .filter(name -> name.getName().toLowerCase().contains(userInput))
                                     .collect(Collectors.toCollection(FXCollections::observableArrayList));
        }
        //TODO change to bindings if u have time
        nameListView.setItems(listOfNames);
    }

    public void onBackButtonClicked(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MenuScreen.fxml"));
            Scene scene = nameSearchBar.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onSelectAllButtonClicked(MouseEvent mouseEvent) {
        listOfNames.forEach(name -> name.setSelected(true));
    }

    public void onSelectNoneButtonClicked(MouseEvent mouseEvent) {
        listOfNames.forEach(name -> name.setSelected(false));
    }
}
