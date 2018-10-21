package namesayer.view.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import static namesayer.util.Screen.NAME_SELECT_SCREEN;

public class NameSelectHelpScreenController {
	@FXML JFXButton backButton;
	
	public void onBackButtonClicked() throws IOException {
        NAME_SELECT_SCREEN.loadWithNode(backButton);
	}
}
