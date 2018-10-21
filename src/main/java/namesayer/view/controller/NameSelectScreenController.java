package namesayer.view.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import impl.org.controlsfx.autocompletion.SuggestionProvider;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import namesayer.model.CompositeName;
import namesayer.model.Name;
import namesayer.persist.NameStorageManager;
import namesayer.persist.Result;
import namesayer.persist.Result.Status;
import namesayer.persist.SessionStorageManager;
import namesayer.session.AssessmentSession;
import namesayer.session.PractiseSession;
import namesayer.session.Session;
import namesayer.session.SessionFactory;
import namesayer.util.EmptySelectionModel;
import namesayer.util.NameConcatenateTask;
import namesayer.util.SnackBarLoader;
import namesayer.view.cell.CompleteNameLoadingCell;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static namesayer.session.Session.SessionType;
import static namesayer.session.Session.SessionType.ASSESSMENT;
import static namesayer.session.Session.SessionType.PRACTISE;
import static namesayer.util.Screen.*;


public class NameSelectScreenController {

    @FXML private JFXButton resumeSessionButton;
    @FXML private JFXListView<Session> savedSessionsListView;
    @FXML private GridPane parentPane;
    @FXML private JFXTextField nameSearchBar;
    @FXML private JFXListView<String> nameListView;
    @FXML private JFXToggleButton randomToggle;
    @FXML private JFXButton helpButton;

    private SuggestionProvider<String> suggestions;
    private List<String> autoCompletions = new ArrayList<>();
    private Map<String, String> canonicalNameCache = new HashMap<>();
    private SessionType sessionType;
    private SessionFactory sessionFactory = new SessionFactory();
    private NameStorageManager nameStorageManager = NameStorageManager.getInstance();
    private SessionStorageManager sessionStorageManager = SessionStorageManager.getInstance();


    public void initialize() {
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
        new Thread(new NameConcatenateTask(sessionFactory, userInput)).start();
        canonicalNameCache.put(result.getDiscoveredName(), userInput);
    }

    /**
     * The sessionType must be set after initializing this scene
     */

    public void setSessionType(SessionType sessionType) {
        this.sessionType = sessionType;
        savedSessionsListView.setPlaceholder(new Label("No sessions have been saved"));
        ObservableList<Session> items;
        if (sessionType.equals(ASSESSMENT)) {
            items = FXCollections.observableArrayList(sessionStorageManager.getSavedAssessmentSessions());
        } else {
            items = FXCollections.observableArrayList(sessionStorageManager.getSavedPractiseSessions());
        }
        resumeSessionButton.setDisable(items.isEmpty());
        savedSessionsListView.setItems(items);
    }

    /**
     * Loads the RecordingScreen
     */

    public void onNextButtonClicked(MouseEvent mouseEvent) throws IOException {
        if (nameListView.getItems().isEmpty()) {
            SnackBarLoader.displayMessage(parentPane, "Please enter a name first");
        } else {
            Parent root;
            if (randomToggle.isSelected()) {
                sessionFactory.randomize();
            }
            if (sessionType.equals(ASSESSMENT)) {
                FXMLLoader loader = ASSESSMENT_SCREEN.getLoader();
                root = loader.load();
                AssessmentScreenController controller = loader.getController();
                controller.injectSession(sessionFactory.makeAssessmentSession());
            } else {
                FXMLLoader loader = PRACTISE_SCREEN.getLoader();
                root = loader.load();
                PractiseScreenController controller = loader.getController();
                controller.injectSession(sessionFactory.makePractiseSession());
            }
            Scene scene = nameSearchBar.getScene();
            scene.setRoot(root);
        }
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
        System.out.println(result.getStatus());
        if (result.getStatus().equals(Status.NONE_FOUND)) {
            resetSuggestions();
            return;
        } else if (result.getStatus().equals(Status.ALL_FOUND)) {
            String lastChar = userInput.substring(userInput.length() - 1);
            boolean readyForSuggestion = (lastChar.equals(" ") || lastChar. equals("-"));
            if (readyForSuggestion) {
                suggestions.clearSuggestions();
                suggestions.addPossibleSuggestions(autoCompletions.stream()
                        .map(s -> result.getDiscoveredName() + lastChar + s)
                        .collect(Collectors.toSet()));
            }
        }
    }


    private void resetSuggestions() {
        suggestions.clearSuggestions();
        suggestions.addPossibleSuggestions(autoCompletions);
    }


    public void onBackButtonClicked(MouseEvent mouseEvent) {
        MAIN_MENU.loadWithNode(nameSearchBar);
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
        if (sessionType.equals(PRACTISE)) {
            sessionFactory.removeName(new CompositeName(item));
        } else {
            sessionFactory.removeName(new CompositeName(item));
        }
        nameListView.getItems().remove(item);
        canonicalNameCache.values().remove(item);
    }

    public void onResumeButtonClicked(ActionEvent actionEvent) throws IOException {
        Parent root;
        if (sessionType.equals(ASSESSMENT)) {
            FXMLLoader loader = ASSESSMENT_SCREEN.getLoader();
            root = loader.load();
            AssessmentScreenController controller = loader.getController();
            controller.injectSession((AssessmentSession) savedSessionsListView.getSelectionModel().getSelectedItem());
        } else {
            FXMLLoader loader = PRACTISE_SCREEN.getLoader();
            root = loader.load();
            PractiseScreenController controller = loader.getController();
            controller.injectSession((PractiseSession) savedSessionsListView.getSelectionModel().getSelectedItem());
        }
        Scene scene = nameSearchBar.getScene();
        scene.setRoot(root);
    }
    
    @FXML
    public void onHelpButtonClicked(MouseEvent e) throws IOException {
        NAME_SELECT_HELP_SCREEN.loadWithNode(helpButton);
    }
}