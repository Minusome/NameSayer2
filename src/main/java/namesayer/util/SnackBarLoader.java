package namesayer.util;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.layout.Pane;

/**
 * Helper class for displays a SnackBar message on screen
 */

public class SnackBarLoader {

    public static void displayMessage(Pane parent, String message) {
        JFXSnackbar bar = new JFXSnackbar(parent);
        bar.getStylesheets().addAll("/css/Material.css");
        bar.enqueue(new JFXSnackbar.SnackbarEvent(message));
    }

}
