package namesayer;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class HelpScreenController2 {
	@FXML JFXButton backButton;
	
	public void onBackButtonClicked() throws IOException {
        Scene scene = backButton.getScene();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/NameSelectScreen.fxml"));
        Parent root = loader.load();
        scene.setRoot(root);
	}
}
