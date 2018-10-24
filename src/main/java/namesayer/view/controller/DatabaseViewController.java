package namesayer.view.controller;

import com.jfoenix.controls.*;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.util.Duration;
import namesayer.model.*;
import namesayer.persist.NameStorageManager;
import namesayer.util.DatabaseImporter;
import namesayer.util.SnackBarLoader;
import org.controlsfx.control.Rating;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.stream.Collectors;

import static namesayer.util.Screen.MAIN_MENU;

public class DatabaseViewController {

    @FXML private JFXToggleButton badQualityToggle;
    @FXML private JFXButton backButton;
    @FXML private JFXListView nameList;
    @FXML private JFXListView recordingList;
    @FXML private GridPane parentPane;
    @FXML private JFXSpinner playingSpinner;
    @FXML private JFXTextField nameSearchBar;
    @FXML private Rating rating;
    @FXML private JFXButton sortButton;
    private double ratingValue;
    private ObservableList<CompositeName> userRecordings;
    private ObservableList<PartialName> databaseRecordings;
    private ObservableList<Name> listOfNames;
    private int counter = 0;

    /**
     * Initialize listView
     */
    public void initialize() {
        userRecordings = NameStorageManager.getInstance().getCompositeNames();
        databaseRecordings = NameStorageManager.getInstance().getPartialNames();
        rating.setRating(3.0);
        onNameDatabaseClicked();
    }

    /**
     * Show partial name database
     */
    public void onNameDatabaseClicked() {
    	counter=0;
        playingSpinner.setProgress(1);
        setRatingVisible(false, false,false);
        if (databaseRecordings.isEmpty()) {
            SnackBarLoader.displayMessage(parentPane, "No recordings in database");
        } else {
            showNameDatabase();
        }
    }

    /**
     * Show user recordings
     */
    public void onUserRecordingClicked() {
    	counter=1;
        playingSpinner.setProgress(1);
        setRatingVisible(false, false,false);
        if (userRecordings.isEmpty()) {
            SnackBarLoader.displayMessage(parentPane, "No user recording in datatbase");
        } else {
            showComNameDatabase();
        }
    }

    

    public void searchNameKeyTyped(KeyEvent e) {
    	String userInput = nameSearchBar.getText();
        if (userInput.isEmpty()) {
            listOfNames = (counter == 0) ? FXCollections.observableArrayList(databaseRecordings) :
                    FXCollections.observableArrayList(userRecordings);
            nameList.setItems(listOfNames);
            return;
        }
		if(e.getCode().equals(KeyCode.ENTER)) {
            if(counter==0) {
                listOfNames = databaseRecordings.stream()
                        .filter(name -> name.toString().toLowerCase().contains(userInput))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }else {
                listOfNames = userRecordings.stream()
                        .filter(name -> name.toString().toLowerCase().contains(userInput))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
	        if(listOfNames.isEmpty()) {
                SnackBarLoader.displayMessage(parentPane, "Name not found");
	        }
	        nameList.setItems(listOfNames);
    	}
    	
    }
    

    /**
     * Back to main menu
     */
    public void onBackClicked() throws IOException {
        MAIN_MENU.loadWithNode(backButton);
    }

    /**
     * Play the selected recording
     */
    public void onPlayButtonClicked(MouseEvent e) {
        Recording recording = (Recording) recordingList.getSelectionModel().getSelectedItem();
        if (recording == null) {
            SnackBarLoader.displayMessage(parentPane, "Please select a recording first");
        } else {
            recording.playAudio();
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(0), new KeyValue(playingSpinner.progressProperty(), 0)),
                    new KeyFrame(
                            Duration.seconds(recording.getLength()),
                            //event -> disableButtons(false, true),
                            new KeyValue(playingSpinner.progressProperty(), 1)
                    )
            );
            timeline.play();
        }
    }

    /**
     * Show the recording list of selected partial name
     */
    private void showNameDatabase() {
        nameList.setItems(databaseRecordings);
        nameList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent e) {
                setRatingVisible(false, false,false);
                PartialName name = (PartialName) nameList.getSelectionModel().getSelectedItem();
                if (name != null) {
                    recordingList.setItems(FXCollections.observableArrayList(name.getRecordings()));
                }
                recordingActionListener();
            }

        });


    }

    /**
     * Show the recording list of selected composite name
     */
    private void showComNameDatabase() {
        nameList.setItems(userRecordings);
        nameList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                setRatingVisible(false, false,true);
                CompositeName name = (CompositeName) nameList.getSelectionModel().getSelectedItem();
                if (name != null) {
                    recordingList.setItems(FXCollections.observableArrayList(name.getUserAttempts()));
                }
                userAttemptsListener();
            }


        });
    }

    /**
     * Add event listener to each composite recording
     */
    private void userAttemptsListener() {
        recordingList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                setRatingVisible(false, true,true);
                CompositeRecording r = (CompositeRecording) recordingList.getSelectionModel().getSelectedItem();
                if (r != null) {
                    rating.setRating(r.getRating());
                }
                //setUserAttemptsRating();
            }

        });

    }

    /**
     * Add event listener to each partial recording
     */
    private void recordingActionListener() {
        recordingList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                setRatingVisible(true, false,false);
                PartialRecording r = (PartialRecording) recordingList.getSelectionModel().getSelectedItem();
                if (r != null) {
                    badQualityToggle.setSelected(r.isBadQuality());
                }
            }
        });
    }


    /**
     * Save the quality to partial recording
     */
    public void setRecordingQuality() {
        PartialRecording r = (PartialRecording) recordingList.getSelectionModel().getSelectedItem();
        boolean newQuality = !r.isBadQuality();
        badQualityToggle.setSelected(newQuality);
        r.setBadQuality(newQuality);
        NameStorageManager.getInstance().refreshBadQualityFile();
    }

    /**
     * Save rating to composite recording
     */
    @FXML
    public void setUserAttemptsRating() {
        CompositeRecording r = (CompositeRecording) recordingList.getSelectionModel().getSelectedItem();
        ratingValue = rating.getRating();
        r.setRating(ratingValue);

    }


    /**
     * Set visibility of ratings
     */
    private void setRatingVisible(boolean thumb, boolean star,boolean sort) {
        rating.setVisible(star);
        badQualityToggle.setVisible(thumb);
        sortButton.setVisible(sort);
    }

    /**
    * Sort recording list by rating
    */
    @FXML
    private void sortByRating(MouseEvent e) {
        if(nameList.getSelectionModel().getSelectedItem()==null) {
            SnackBarLoader.displayMessage(parentPane, "Please select a name to sort");
        }else {
            CompositeName name = (CompositeName) nameList.getSelectionModel().getSelectedItem();
            recordingList.setItems(FXCollections.observableArrayList(name.getUserAttempts()));
            recordingList.getItems().sort(Comparator.comparingDouble(CompositeRecording::getRating).reversed());
        }
    }

    /**
     * Import new database
    */
    public void onImportButtonClicked() {
        DirectoryChooser chooser= new DirectoryChooser();
        chooser.setTitle("Select folder containing recordings");
        File selectedFolder = chooser.showDialog(null);
        if (selectedFolder != null) {
            SnackBarLoader.displayMessage(parentPane, "Database loading in progress...");
            DatabaseImporter importer = new DatabaseImporter(selectedFolder);
            importer.setOnFailed(event -> SnackBarLoader.displayMessage(parentPane, "Can't load folder: " + selectedFolder.getName()));
            importer.setOnSucceeded(event -> {
                SnackBarLoader.displayMessage(parentPane, "Successfully loaded folder: " + selectedFolder.getName());
                initialize();
            });
            new Thread(importer).start();
        }
    }
}
