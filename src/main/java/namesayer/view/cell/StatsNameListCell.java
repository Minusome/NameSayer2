package namesayer.view.cell;

import com.jfoenix.controls.JFXListCell;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import namesayer.model.CompositeName;

import java.io.IOException;


/**
 * Represents a custom listCell which displays a delete button and dialog to prompt the user
 */
public class StatsNameListCell extends JFXListCell<Pair<CompositeName, Double>> {

    @FXML private Label itemLabel;
    @FXML private HBox listItemHBox;
    private CompositeName name;


    public StatsNameListCell() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/StatsNameListCell.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateItem(Pair<CompositeName, Double> item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            if (item == null) {
                itemLabel.setText("<null>");
            } else {
                name = item.getKey();
                double rating =  (double)Math.round(item.getValue() * 10) / 10;
                itemLabel.setText(name.toString() + " (Rated: " + rating + ")");
            }
            setGraphic(listItemHBox);
        }
    }


    @FXML
    public void onPlayButtonClicked(MouseEvent mouseEvent) {
        name.getExemplar().playAudio();
    }
}
