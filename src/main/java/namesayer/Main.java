package namesayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import namesayer.persist.NameStorageManager;


public class Main extends Application {
    //TODO Implement Rewards screen
    //TODO Make the card number display actually work


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MenuScreen.fxml"));
        primaryStage.setTitle("Name Sayer");
        primaryStage.setScene(new Scene(root, 800, 700));
        primaryStage.show();
        NameStorageManager.getInstance().load();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
