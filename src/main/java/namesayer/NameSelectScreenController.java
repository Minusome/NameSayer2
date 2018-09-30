package namesayer;

import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import namesayer.model.Name;
//import namesayer.util.NameStorageManager;
import namesayer.model.PartialName;
import namesayer.util.EmptySelectionModel;
import namesayer.util.NameStorageManager;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class NameSelectScreenController {

    @FXML private GridPane parentPane;
    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<String> nameListView;
    @FXML private JFXToggleButton randomToggle;
    private JFXSnackbar bar;


    private NameStorageManager nameStorageManager;
    private ObservableList<PartialName> partialNames;
    private SuggestionProvider<String> suggestions;
    private HashSet<String> autoCompletions = new HashSet<>();
    private int userInputNameLength = 0;
    private static boolean randomSelected = false;


    public void initialize() {
        nameStorageManager = NameStorageManager.getInstance();
        partialNames = nameStorageManager.getPartialNames();

        //Use custom ListCell with checkboxes
        nameListView.setCellFactory(value -> new JFXListCell<>());
        nameListView.setSelectionModel(new EmptySelectionModel<>());
        nameListView.setExpanded(false);
        nameListView.setPlaceholder(new Label("Please names you wish to practise"));
        bar = new JFXSnackbar(parentPane);
        bar.getStylesheets().addAll("/css/Material.css");


        for (Name name : partialNames) {
            autoCompletions.add(name.toString());
        }

        suggestions = SuggestionProvider.create(autoCompletions);
        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(nameSearchBar, suggestions);
        binding.setPrefWidth(500);
    }

    /**
     * Loads the RecordingScreen
     */
    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
//        if (nameStorageManager.getSelectedNamesList().isEmpty()) {
//            bar.enqueue(new JFXSnackbar.SnackbarEvent("Please select a name first"));
//            return;
//        }
        Parent root = FXMLLoader.load(getClass().getResource("/RecordingScreen.fxml"));
        Scene scene = nameSearchBar.getScene();
        scene.setRoot(root);
    }

    public void setRandom() {
        randomSelected = !randomToggle.isDisableAnimation();
    }

    public static boolean RandomToggleOn() {
        return randomSelected;
    }


    @FXML
    public void onSearchBarKeyTyped(KeyEvent keyEvent) {

        String currentName = nameSearchBar.getCharacters().toString();
        List<String> nameComponents = Arrays.asList(currentName.split("[\\s-]+"));
        boolean readyForSuggestion = (currentName.lastIndexOf(" ") == currentName.length() - 1);
        currentName = currentName.trim();

        if (!currentName.isEmpty() && nameComponents.size() != userInputNameLength) {
            if (autoCompletions.contains(nameComponents.get(nameComponents.size() - 1)) &&
                    (readyForSuggestion)) {
                suggestions.clearSuggestions();
                String finalCurrentName = currentName;
                suggestions.addPossibleSuggestions(
                        autoCompletions.stream()
                                       .map(s -> finalCurrentName + " " + s)
                                       .collect(Collectors.toSet()));
                userInputNameLength = nameComponents.size();
            }
        } else if (currentName.isEmpty()) {
            suggestions.clearSuggestions();
            suggestions.addPossibleSuggestions(autoCompletions);
            userInputNameLength = 0;
        }
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            nameListView.getItems().add(currentName);
            nameSearchBar.clear();
        }
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
//        partialNames.forEach(name -> name.setSelected(true));
    }

    public void onSelectNoneButtonClicked(MouseEvent mouseEvent) {
//        partialNames.forEach(name -> name.setSelected(false));
    }
}
