package namesayer.util;

import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.util.Duration;

/**
 * Responsible for creating animations at runtime
 */

public class TransitionFactory {

    public enum Direction {
        RIGHT, LEFT
    }

    /**
     * Swipe affect of cards
     *
     * @param node The node to animate
     * @param direction Left or Right
     * @param handler Executed post-animation
     * @return The Transition
     */
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

    /**
     * Animate a slide-up
     *
     * @param node The node to animate
     * @return The transition
     */
    public static TranslateTransition slideUpTransition(Node node) {
        TranslateTransition moveUp = new TranslateTransition(Duration.millis(200), node);
        moveUp.setFromY(100);
        moveUp.setToY(0);
        node.setVisible(true);
        return moveUp;
    }

    /**
     * Animate a slide-down
     *
     * @param node The node to animate
     * @return The transition
     */
    public static TranslateTransition slideDownTransition(Node node) {
        TranslateTransition moveDown = new TranslateTransition(Duration.millis(200), node);
        moveDown.setFromY(0);
        moveDown.setToY(100);
        moveDown.setOnFinished(event -> node.setVisible(false));
        return moveDown;
    }


}
