package namesayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/MenuScreen.fxml"));
        primaryStage.setTitle("namesayer");
        primaryStage.setScene(new Scene(root, 1152, 648));
        primaryStage.show();
        new File("C:/Users/zhugu/Pictures/123").mkdir();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
