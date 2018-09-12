package NameSayer;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class NameSelectScreenController implements Initializable {

    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<String> nameListView;


    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> listOfNames = FXCollections.observableArrayList(
                "bob",
                "sam",
                "li",
                "george",
                "asdfjkl"
        );
        nameListView.setItems(listOfNames);
    }
}
