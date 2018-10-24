package namesayer.view.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;

import java.io.IOException;

import static namesayer.util.Screen.MAIN_MENU;

public class MenuHelpScreenController {
	@FXML JFXButton backButton;
	
	public void onBackButtonClicked() throws IOException {
        MAIN_MENU.loadWithNode(backButton);
	}
}
