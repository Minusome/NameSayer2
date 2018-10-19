package namesayer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import namesayer.persist.NameStorageManager;
import namesayer.persist.SessionStorageManager;
import namesayer.persist.StatsManager;

import static namesayer.util.Screen.MAIN_MENU;


public class Main extends Application {
    //TODO Implement Rewards screen
    //TODO Make the card number display actually work


    @Override
    public void start(Stage primaryStage) throws Exception {
        NameStorageManager.getInstance();
        SessionStorageManager.getInstance();
        StatsManager.getInstance();
        Scene scene = new Scene(MAIN_MENU.getRoot(), 800, 700);
        primaryStage.setTitle("Name Sayer");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(700);
        primaryStage.setMinWidth(800);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
