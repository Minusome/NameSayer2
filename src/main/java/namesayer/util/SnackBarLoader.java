package namesayer.util;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.layout.Pane;

public class SnackBarLoader {

    public static void displayMessage(Pane parent, String message) {
        JFXSnackbar bar = new JFXSnackbar(parent);
        bar.getStylesheets().addAll("/css/Material.css");
        bar.enqueue(new JFXSnackbar.SnackbarEvent(message));
    }

}
