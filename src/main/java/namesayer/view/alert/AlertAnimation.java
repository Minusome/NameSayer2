package namesayer.view.alert;

import com.jfoenix.animation.alert.CenterTransition;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import javafx.animation.Animation;
import javafx.scene.Node;


public class AlertAnimation implements JFXAlertAnimation {
    @Override
    public void initAnimation(Node contentContainer, Node overlay) {
        overlay.setOpacity(0.0D);
        contentContainer.setScaleX(0.0D);
        contentContainer.setScaleY(0.0D);
    }

    @Override
    public Animation createShowingAnimation(Node contentContainer, Node overlay) {
        return new CenterTransition(contentContainer, overlay);
    }

    @Override
    public Animation createHidingAnimation(Node contentContainer, Node overlay) {
        return (Animation)inverseAnimation.apply(new CenterTransition(contentContainer, overlay));
    }
}
