package NameSayer;

import com.jfoenix.controls.JFXButton;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import javax.annotation.Resources;
import java.net.URL;

public class ButtonController {

    @FXML
    private JFXButton helloButton;

//    @FXML
//    protected void initialize(URL location, Resources resources) {
//        helloButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            public void handle(MouseEvent event) {
//
//            }
//        });
//
//    }



    public void sayHelloWorld(MouseEvent mouseEvent) {;
        System.out.println("hello world");
    }
}