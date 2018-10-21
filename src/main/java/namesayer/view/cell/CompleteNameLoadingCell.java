package namesayer.view.cell;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import namesayer.view.controller.NameSelectScreenController;
import namesayer.persist.NameStorageManager;
import namesayer.persist.Result;

import java.io.IOException;

import static namesayer.persist.Result.Status.*;

public class CompleteNameLoadingCell extends JFXListCell<String> {

    @FXML private Label itemLabel;
    @FXML private HBox listItemHBox;
    @FXML private JFXButton deleteButton;
    @FXML private MaterialIconView icon;
    @FXML private JFXButton checkMark;
    String item;
    private NameStorageManager manager;
    private NameSelectScreenController parentController;

    public CompleteNameLoadingCell(NameSelectScreenController parentController) {
        super();
        this.parentController = parentController;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CompleteNameLoadingCell.fxml"));
        fxmlLoader.setController(this);
        manager = NameStorageManager.getInstance();
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        this.item = item;
        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            if (item == null) {
                itemLabel.setText("<null>");
            } else {
                Result result = manager.queryUserRequestedName(item);
                if (result.getStatus().equals(ALL_FOUND)) {
                    icon.setGlyphName("DONE");
                    icon.setFill(Paint.valueOf("#4CAF50"));
                    checkMark.setTooltip(new Tooltip("All names were located in the database"));
                } else if (result.getStatus().equals(PARTIALLY_FOUND)) {
                    icon.setGlyphName("WARNING");
                    icon.setFill(Paint.valueOf("#FFC107"));
                    checkMark.setTooltip(new Tooltip("The following names were located in the database: " + result.getDiscoveredName()));
                } else {
                    icon.setGlyphName("DO_NOT_DISTURB");
                    icon.setFill(Paint.valueOf("#D32F2F"));
                    checkMark.setTooltip(new Tooltip("No names were located in the database"));
                }
                itemLabel.setText(item);
            }
            setGraphic(listItemHBox);
        }
    }

    @FXML
    public void onDeleteClicked() {
        parentController.removeSelection(item);
    }


}
