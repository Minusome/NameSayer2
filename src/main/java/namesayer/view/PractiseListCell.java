package namesayer.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import namesayer.model.CompositeRecording;
import namesayer.persist.PractiseSession;

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
//            listItemHBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    deleteButton.setVisible(true);
//                    rateButton.setVisible(true);
//                    playButton.setVisible(true);
//                }
//            });
//
//            listItemHBox.setOnMouseExited(new EventHandler<MouseEvent>() {
//                @Override
//                public void handle(MouseEvent event) {
//                    deleteButton.setVisible(false);
//                    rateButton.setVisible(false);
//                    playButton.setVisible(false);
//                }
//            });
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
                itemLabel.setText(item.toString());
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
    public void onRateButtonClicked(MouseEvent mouseEvent) {

    }

}
