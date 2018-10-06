package namesayer;

import com.jfoenix.controls.JFXListView;
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
import namesayer.persist.NameStorageManager;
import namesayer.session.AssessmentSession;
import namesayer.session.PractiseSession;
import namesayer.session.Session;
import namesayer.util.NameConcatenateTask;
import namesayer.util.Result;
import namesayer.util.Result.Status;
import namesayer.view.CompleteNameLoadingCell;
import namesayer.view.EmptySelectionModel;
import namesayer.view.SnackBarLoader;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static namesayer.session.Session.SessionType;
import static namesayer.session.Session.SessionType.ASSESSMENT;


public class NameSelectScreenController {

    @FXML private GridPane parentPane;
    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<String> nameListView;
    @FXML private JFXToggleButton randomToggle;


    private SuggestionProvider<String> suggestions;
    private List<String> autoCompletions = new ArrayList<>();
    private SessionType sessionType;
    private AssessmentSession assessmentSession;
    private PractiseSession practiseSession;
    private Map<String, String> canonicalNameCache = new HashMap<>();
    private NameStorageManager nameStorageManager;


    private static boolean randomSelected = false;


    public void initialize() {
        nameStorageManager = NameStorageManager.getInstance();

        //Use custom ListCell with checkboxes
        nameListView.setCellFactory(value -> new CompleteNameLoadingCell(this));
        nameListView.setSelectionModel(new EmptySelectionModel<>());
        nameListView.setExpanded(false);
        nameListView.setPlaceholder(new Label("Please enter names you wish to practise"));

        for (Name name : nameStorageManager.getPartialNames()) {
            autoCompletions.add(name.toString());
        }

        suggestions = SuggestionProvider.create(autoCompletions);
        AutoCompletionBinding<String> binding = TextFields.bindAutoCompletion(nameSearchBar, suggestions);
        binding.setPrefWidth(500);
    }


    private void addToListView(String userInput, String errorMsg) {
        Result result = nameStorageManager.queryUserRequestedName(userInput);
        if (result.getStatus().equals(Status.NONE_FOUND)) {
            SnackBarLoader.displayMessage(parentPane, errorMsg);
            return;
        }
        if (canonicalNameCache.containsKey(result.getDiscoveredName())) {
            SnackBarLoader.displayMessage(parentPane, "A name with same pronunciation is already entered");
            return;
        }
        nameListView.getItems().add(userInput);
        Session session = (sessionType.equals(ASSESSMENT)) ? assessmentSession : practiseSession;
        new Thread(new NameConcatenateTask(session, userInput)).start();
        canonicalNameCache.put(result.getDiscoveredName(), userInput);
    }

    /**
     * The sessionType must be set after initializing this scene
     */
    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
        if (sessionType.equals(ASSESSMENT)) {
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
            SnackBarLoader.displayMessage(parentPane, "Please enter a name first");
        } else {
            Parent root;
            if (sessionType.equals(ASSESSMENT)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/AssessmentScreen.fxml"));
                root = loader.load();
                AssessmentScreenController controller = loader.getController();
                controller.injectSession(assessmentSession);
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/PractiseScreen.fxml"));
                root = loader.load();
                PractiseScreenController controller = loader.getController();
                controller.injectSession(practiseSession);
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
        String userInput = nameSearchBar.getCharacters().toString();
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            addToListView(userInput, "This name could not be located in the database");
            nameSearchBar.clear();
            resetSuggestions();
            return;
        }
        Result result = nameStorageManager.queryUserRequestedName(userInput);
        if (result.getStatus().equals(Status.NONE_FOUND)) {
            resetSuggestions();
            return;
        }
        boolean readyForSuggestion = (userInput.lastIndexOf(" ") == userInput.length() - 1);
        if (readyForSuggestion) {
            suggestions.clearSuggestions();
            suggestions.addPossibleSuggestions(autoCompletions.stream()
                                                              .map(s -> result.getDiscoveredName() + " " + s)
                                                              .collect(Collectors.toSet()));
        }
    }

    private void resetSuggestions() {
        suggestions.clearSuggestions();
        suggestions.addPossibleSuggestions(autoCompletions);
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


    public void onFileInsertClicked(MouseEvent mouseEvent) {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select .txt file containing list of names");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".txt", "*.txt"));
        File selectedFile = chooser.showOpenDialog(randomToggle.getScene().getWindow());
        if (selectedFile != null) {
            try (Stream<String> stream = Files.lines(selectedFile.toPath())) {
                stream.forEach(s -> addToListView(s, "Some names could not be located in the database"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeSelection(String item) {
        nameListView.getItems().remove(item);
        canonicalNameCache.values().remove(item);
    }
}
