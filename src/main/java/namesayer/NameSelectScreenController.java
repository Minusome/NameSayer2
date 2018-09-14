package namesayer;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import namesayer.recording.RecordingManager;
import namesayer.util.EmptySelectionModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NameSelectScreenController implements Initializable {

    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<String> nameListView;

    private RecordingManager recordingManager;
    private List<String> selectedNames = new ArrayList<String>();


    public void initialize(URL location, ResourceBundle resources) {
        recordingManager = RecordingManager.getInstance("C:/Users/zhugu/Pictures/Camera Roll");
        ObservableList<String> listOfNames = FXCollections.observableArrayList(recordingManager.getListOfNames());


        nameListView.setCellFactory(value -> new JFXListCell<String>() {
            JFXCheckBox checkBox = new JFXCheckBox();
            String name;

            {
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue && !oldValue) {
                        selectedNames.add(name);
                    } else if (!newValue && oldValue) {
                        selectedNames.remove(name);
                    }
                    System.out.println(selectedNames);
                });
            }

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                name = item;
                if (empty) {
                    setGraphic(null);
                } else {
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
