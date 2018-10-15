package namesayer.util;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

public class TransitionFactory {

    public enum Direction {
        RIGHT, LEFT
    }

    public static SequentialTransition cardDoubleSlideTransition(Node node , Direction direction, EventHandler<ActionEvent> handler) {
        TranslateTransition moveLeft = new TranslateTransition(Duration.millis(200), node);
        int pos = 0;
        if (direction.equals(Direction.RIGHT)) {
            pos = 1;
        } else {
            pos = -1;
        }
        moveLeft.setFromX(0);
        moveLeft.setToX(pos*1000);
        moveLeft.setOnFinished(handler);
        TranslateTransition moveLeft2 = new TranslateTransition(Duration.millis(200), node);
        moveLeft2.setFromX(-pos * 1000);
        moveLeft2.setToX(0);
        return new SequentialTransition(moveLeft, moveLeft2);
    }

    public static TranslateTransition slideUpTransition(Node node) {
        TranslateTransition moveUp = new TranslateTransition(Duration.millis(200), node);
        moveUp.setFromY(100);
        moveUp.setToY(0);
        node.setVisible(true);
        return moveUp;
    }

    public static TranslateTransition slideDownTransition(Node node) {
        TranslateTransition moveDown = new TranslateTransition(Duration.millis(200), node);
        moveDown.setFromY(0);
        moveDown.setToY(100);
        moveDown.setOnFinished(event -> node.setVisible(false));
        return moveDown;
    }


}
