package namesayer;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import namesayer.model.Name;
import namesayer.persist.AssessmentSession;
import namesayer.persist.NameStorageManager;
import namesayer.persist.PractiseSession;
import namesayer.persist.Session;
import namesayer.util.NameConcatenateTask;
import namesayer.view.CompleteNameLoadingCell;
import namesayer.view.EmptySelectionModel;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static namesayer.persist.Session.*;
import static namesayer.persist.Session.SessionType.*;


public class NameSelectScreenController {

    @FXML private GridPane parentPane;
    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<String> nameListView;
    @FXML private JFXToggleButton randomToggle;
    private JFXSnackbar bar;


    private SuggestionProvider<String> suggestions;
    private HashSet<String> autoCompletions = new HashSet<>();
    private SessionType sessionType;
    private AssessmentSession assessmentSession;
    private PractiseSession practiseSession;

    private int userInputNameLength = 0;
    private static boolean randomSelected = false;
    private boolean isLoaded = false;


    public void initialize() {
        NameStorageManager nameStorageManager = NameStorageManager.getInstance();

        //Use custom ListCell with checkboxes
        nameListView.setCellFactory(value -> new CompleteNameLoadingCell());
        nameListView.setSelectionModel(new EmptySelectionModel<>());
        nameListView.setExpanded(false);
        nameListView.setPlaceholder(new Label("Please names you wish to practise"));
        bar = new JFXSnackbar(parentPane);
        bar.getStylesheets().addAll("/css/Material.css");


        for (Name name : nameStorageManager.getPartialNames()) {
            autoCompletions.add(name.toString());
        }

        suggestions = SuggestionProvider.create(autoCompletions);
        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(nameSearchBar, suggestions);
        binding.setPrefWidth(500);
    }


    private void addToListView(String string) {
        if (!nameListView.getItems().contains(string)) {
            nameListView.getItems().add(string);
            Session session = (sessionType.equals(ASSESSMENT)) ? assessmentSession : practiseSession;
            new Thread(new NameConcatenateTask(session, string)).start();
        }
    }

    /**
     * The sessionType must be set after initializing this scene
     */
    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
        if (sessionType.equals(SessionType.ASSESSMENT)) {
            assessmentSession = new AssessmentSession();
        } else {
            practiseSession = new PractiseSession();
        }
    }

    /**
     * Loads the RecordingScreen
     */
    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
        if (nameListView.getItems().isEmpty()) {
            bar.enqueue(new JFXSnackbar.SnackbarEvent("Please enter a name first"));
        } else {
            Parent root = null;
            if (sessionType.equals(SessionType.ASSESSMENT)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AssessmentScreen.fxml"));
                root = loader.load();
                AssessmentScreenController controller = loader.getController();
                controller.injectSession(assessmentSession);
            } else {
                //TODO not implemented
            }
            Scene scene = nameSearchBar.getScene();
            scene.setRoot(root);
        }
    }

    public void setRandom() {
        randomSelected = !randomToggle.isDisableAnimation();
    }

    public static boolean RandomToggleOn() {
        return randomSelected;
    }


    /**
     * Works but probably should be cleaned up
     */
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
            addToListView(currentName);
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

    public void onFileInsertClicked(MouseEvent mouseEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select .txt file containing list of names");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".txt", "*.txt"));
        File selectedFile = chooser.showOpenDialog(randomToggle.getScene().getWindow());
        if (selectedFile != null) {
            try (Stream<String> stream = Files.lines(selectedFile.toPath())) {
                stream.forEach(this::addToListView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
