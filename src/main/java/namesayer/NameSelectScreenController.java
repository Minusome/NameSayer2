package namesayer;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import namesayer.recording.RecordingManager;
import namesayer.util.EmptySelectionModel;

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
        recordingManager = RecordingManager.getInstance();
        ObservableList<String> listOfNames = FXCollections.observableArrayList(recordingManager.getListOfNames());


        nameListView.setCellFactory(value -> new JFXListCell<String>() {
            JFXCheckBox checkBox = new JFXCheckBox();
            BooleanProperty isNameSelected = new SimpleBooleanProperty(false);

            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(checkBox);
                    if (!checkBox.selectedProperty().isBound()){
                        isNameSelected.addListener(new ChangeListener<Boolean>() {
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue && !oldValue) {
                                    selectedNames.add(item);
                                } else if (!newValue && oldValue) {
                                    selectedNames.remove(item);
                                }
                                System.out.println(selectedNames);
                            }
                        });

                        checkBox.selectedProperty().bindBidirectional(isNameSelected);
                    }

                }
            }
        });
        nameListView.setSelectionModel(new EmptySelectionModel<>());
        nameListView.setItems(listOfNames);
        nameListView.setExpanded(false);

    }
}
