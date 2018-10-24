package namesayer.view.cell;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import namesayer.model.CompositeRecording;
import namesayer.persist.StatsManager;
import namesayer.session.PractiseSession;
import namesayer.view.controller.PractiseScreenController;
import namesayer.view.controller.RatingPopupController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Represents a custom listCell which displays a play button, delete button and rating prompt
 */
public class PractiseListCell extends JFXListCell<CompositeRecording> {


    @FXML private Label itemLabel;
    @FXML private HBox listItemHBox;
    @FXML private JFXButton deleteButton;
    @FXML private JFXButton rateButton;
    @FXML private JFXButton playButton;

    private CompositeRecording recording;
    private PractiseSession session;
    private StatsManager manager = StatsManager.getInstance();
    private PractiseScreenController parentController;
    private static List<CompositeRecording> processedRecordings = new ArrayList<>();


    public PractiseListCell(PractiseSession session, PractiseScreenController controller) {
        super();
        this.session = session;
        this.parentController = controller;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/PractiseListCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            deleteButton.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateItem(CompositeRecording item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        recording = item;
        if (empty) {
            setGraphic(null);
        } else {
            if (item == null) {
                itemLabel.setText("<null>");
            } else {
                itemLabel.setText("Recording on " + recording.getTimeStamp());
            }
            setGraphic(listItemHBox);
        }
    }


    @FXML
    public void onDeleteButtonClicked(MouseEvent mouseEvent) {
        session.removeRecordingForCurrentName(recording);
        parentController.refreshList();
    }

    @FXML
    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        recording.playAudio();
    }

    /**
     * Opens a rating dialog pop-up with so user can rate their recordings
     *
     * @throws IOException thrown if FXML fails to load
     */
    @FXML
    public void onRateButtonClicked(MouseEvent mouseEvent) throws IOException {
        JFXAlert alert = new JFXAlert((Stage) listItemHBox.getScene().getWindow());
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setOverlayClose(true);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label("Rating"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/RatingPopup.fxml"));
        Parent root = loader.load();
        RatingPopupController controller = loader.getController();
        controller.bind(recording.ratingProperty());
        layout.setBody(root);
        JFXButton doneButton = new JFXButton("Done");
        doneButton.setOnAction(event -> {
            if (!processedRecordings.contains(recording)){
                manager.updateRatingFreq(controller.getRating());
                manager.updateDifficultName(session.getCurrentName(), controller.getRating());
                processedRecordings.add(recording);
            }
            controller.unbind();
            alert.hideWithAnimation();
        });
        layout.setActions(doneButton);
        alert.setContent(layout);
        alert.show();
    }

}
