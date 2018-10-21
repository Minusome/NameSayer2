package namesayer.view.controller;

import com.jfoenix.controls.*;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import namesayer.model.*;
import namesayer.persist.NameStorageManager;
import org.controlsfx.control.Rating;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DatabaseViewController implements Initializable {

    @FXML private JFXButton backButton;
    @FXML private JFXListView nameList;
    @FXML private JFXListView recordingList;
    @FXML private GridPane parentPane;
    @FXML private JFXSpinner playingSpinner;
    @FXML private JFXTextField nameSearchBar;
    @FXML private Rating rating;
    @FXML private JFXToggleButton badQualityToggle;
    @FXML private MaterialIconView ratingIcon;
    @FXML private JFXButton sortButton;
    private double ratingValue;
    private JFXSnackbar bar;
    private ObservableList<CompositeName> userRecordings;
    private ObservableList<PartialName> databaseRecordings;
    private boolean isNameDatabase;
    private ObservableList<Name> listOfNames;
    private int counter = 0;

    /**
     * Initialize listView
     */
    public void initialise() {
       // userRecordings = NameStorageManager.getInstance().getCompositeNames();
       // databaseRecordings = NameStorageManager.getInstance().getPartialNames();
        playingSpinner.setProgress(1);
        setRatingVisible(false, false,false);
    }

    /**
     * Show partial name database
     */
    public void onNameDatabaseClicked() {

    	counter=0;
        isNameDatabase = true;
        initialise();
        if (databaseRecordings.isEmpty()) {
            System.out.println("empty");
            bar.enqueue(new JFXSnackbar.SnackbarEvent("No recordings in datatbase"));
        } else {
            showNameDatabase();

        }
    }

    /**
     * Show user recordings
     */
    public void onUserRecordingClicked() {
    	counter=1;
        isNameDatabase = false;
        initialise();
        if (userRecordings.isEmpty()) {
            System.out.println("empty");
            bar.enqueue(new JFXSnackbar.SnackbarEvent("No user recording in datatbase"));
        } else {
            //System.out.println("222");
            showComNameDatabase();
        }
    }

    
    @FXML
    public void searchNameKeyTyped(KeyEvent e) {
    	String userInput = nameSearchBar.getText();
		if(e.getCode().equals(KeyCode.ENTER)) {
			nameSearchBar.clear();
	        if (userInput.isEmpty()) {
	            //showNameDatabase();
	        } else {
	        	if(counter==0) {
	        		listOfNames = databaseRecordings.stream()

                            .filter(name -> name.toString().toLowerCase().contains(userInput))

                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
	        	}else {
	        		listOfNames = userRecordings.stream()

                            .filter(name -> name.toString().toLowerCase().contains(userInput))

                            .collect(Collectors.toCollection(FXCollections::observableArrayList));
	        	}
	        	

	        }

	        //TODO change to bindings if u have time
	        if(listOfNames.isEmpty()) {
	        	bar.enqueue(new JFXSnackbar.SnackbarEvent("Name not found"));
	        }

	        nameList.setItems(listOfNames);
    	}
    	
    }
    
    
    
    
    
    /**
     * Back to main menu
     */
    public void onBackClicked() throws IOException {
        Scene scene = backButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MenuScreen.fxml"));
        Parent root = loader.load();
        scene.setRoot(root);
    }

    /**
     * Play the selected recording
     */
    public void onPlayButtonClicked(MouseEvent e) {
        Recording recording = (Recording) recordingList.getSelectionModel().getSelectedItem();
        if (recording == null) {
            bar.enqueue(new JFXSnackbar.SnackbarEvent("Please select a recording first"));
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
            System.out.println(recording.toString());
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
                recordingList.setItems(FXCollections.observableArrayList(name.getRecordings()));
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
                recordingList.setItems(FXCollections.observableArrayList(name.getUserAttempts()));
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
                rating.setRating(r.getRating());
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
                badQualityToggle.setSelected(r.isBadQuality());
            }
        });
    }

    /**
     * Show quality of partial recording
     */
    private void setRating(boolean isBadQuality) {
        if (isBadQuality) {
            ratingIcon.setGlyphName("THUMB_DOWN");
        } else {
            ratingIcon.setGlyphName("THUMB_UP");
        }
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
     * Initialise database view
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        userRecordings = NameStorageManager.getInstance().getCompositeNames();
        databaseRecordings = NameStorageManager.getInstance().getPartialNames();
        bar = new JFXSnackbar(parentPane);
        bar.getStylesheets().addAll("/css/Material.css");
        rating.setRating(3.0);
        setRatingVisible(false, false,false);

    }

    /**
     * Set visibility of ratings
     */
    private void setRatingVisible(boolean thumb, boolean star, boolean sort) {
        rating.setVisible(star);
        badQualityToggle.setVisible(thumb);
        sortButton.setVisible(sort);
    }
    
    @FXML
    private void sortByRating(MouseEvent e) {
    	if(nameList.getSelectionModel().getSelectedItem()==null) {
    		bar.enqueue(new JFXSnackbar.SnackbarEvent("Please select a name to sort"));
    	}else {
    		CompositeName name = (CompositeName) nameList.getSelectionModel().getSelectedItem();
            recordingList.setItems(FXCollections.observableArrayList(name.getUserAttempts()));
    		recordingList.getItems().sort(Comparator.comparingDouble(CompositeRecording::getRating).reversed());
    	}
    }
    

}
