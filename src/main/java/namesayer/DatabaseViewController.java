package namesayer;

import java.io.IOException;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;

import com.jfoenix.controls.JFXSnackbar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import namesayer.model.CompositeName;
import namesayer.model.Name;
import namesayer.model.PartialName;
import namesayer.persist.NameStorageManager;

public class DatabaseViewController {
	
	@FXML private JFXButton backButton;
	@FXML private JFXListView listView;
	@FXML private GridPane parentPane;
	private JFXSnackbar bar;
	private ObservableList<CompositeName> userRecordings;
	private ObservableList<PartialName> databaseRecordings;
	
	
	public void initialise() {
		userRecordings = NameStorageManager.getInstance().getCompositeNames();
		databaseRecordings = NameStorageManager.getInstance().getPartialNames();
		bar = new JFXSnackbar(parentPane);
		bar.getStylesheets().addAll("/css/Material.css");
	}
	
	public void onNameDatabaseClicked() {
		initialise();
		if(databaseRecordings.isEmpty()) {
			System.out.println("empty");
		}else {
			//System.out.println("111");
			listView.setItems( databaseRecordings);
			
		}
		
		
	}
	public void onUserRecordingClicked() {
		initialise();
		if(userRecordings.isEmpty()) {
			System.out.println("empty");
			bar.enqueue(new JFXSnackbar.SnackbarEvent("No user recording in datatbase"));
		}else {
			//System.out.println("222");
			listView.setItems(userRecordings);
		}
		
	}
	
	
	public void onBackClicked() throws IOException {
		Scene scene = backButton.getScene();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("/MenuScreen.fxml"));
    	Parent root = loader.load();
    	scene.setRoot(root);
	}
	
	
	
}
