package namesayer.view.controller;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import static namesayer.util.Screen.MAIN_MENU;

public class MenuHelpScreenController {
	@FXML JFXButton backButton;
	
	public void onBackButtonClicked() throws IOException {
        MAIN_MENU.loadWithNode(backButton);
	}
}
