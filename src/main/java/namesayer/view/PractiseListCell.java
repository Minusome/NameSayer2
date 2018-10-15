package namesayer.view;

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
import namesayer.session.PractiseSession;

import java.io.IOException;


/**
 * Represents a custom listCell which displays a delete button and dialog to prompt the user
 */
public class PractiseListCell extends JFXListCell<CompositeRecording> {


    @FXML private Label itemLabel;
    @FXML private HBox listItemHBox;
    @FXML private JFXButton deleteButton;
    @FXML private JFXButton rateButton;
    @FXML private JFXButton playButton;

    private CompositeRecording recording;
    private PractiseSession session;

    public PractiseListCell(PractiseSession session) {
        super();
        this.session = session;
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
    }

    @FXML
    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        recording.playAudio();
    }

    @FXML
    public void onRateButtonClicked(MouseEvent mouseEvent) throws IOException {
        JFXAlert alert = new JFXAlert((Stage) listItemHBox.getScene().getWindow());
        alert.initModality(Modality.NONE);
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
            controller.unbind();
            alert.hideWithAnimation();
        });
        layout.setActions(doneButton);
        alert.setContent(layout);
        alert.show();
    }

}
