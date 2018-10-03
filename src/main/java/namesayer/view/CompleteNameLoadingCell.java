package namesayer.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import namesayer.persist.NameStorageManager;
import namesayer.util.Result;

import java.io.IOException;

public class CompleteNameLoadingCell extends JFXListCell<String> {

    @FXML private Label itemLabel;
    @FXML private HBox listItemHBox;
    @FXML private JFXButton deleteButton;
    @FXML private MaterialIconView icon;
    private NameStorageManager manager;

    public CompleteNameLoadingCell() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/CompleteNameLoadingCell.fxml"));
        fxmlLoader.setController(this);
        manager = NameStorageManager.getInstance();
        try {
            fxmlLoader.load();
            listItemHBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    deleteButton.setVisible(true);
                }
            });

            listItemHBox.setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    deleteButton.setVisible(false);
                }
            });
            deleteButton.setText("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setText(null);
        if (empty) {
            setGraphic(null);
        } else {
            if (item == null) {
                itemLabel.setText("<null>");
            } else {
                Result result = manager.queryUserRequestedName(item);
                if (result.equals(Result.ALL_FOUND)) {
                    icon.setGlyphName("DONE");
                } else if (result.equals(Result.PARTIALLY_FOUND)) {
                    icon.setGlyphName("WARNING");
                } else {
                    icon.setGlyphName("DO_NOT_DISTURB");
                }
                itemLabel.setText(item);
            }
            setGraphic(listItemHBox);
        }
    }


}
