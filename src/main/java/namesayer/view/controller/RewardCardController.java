package namesayer.view.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.Rating;

/**
 * Displays the final card of an Assessment session to inform
 * user of their average rating
 */

public class RewardCardController {

    @FXML private Label rewardLabel;
    @FXML private Rating rewardRating;

    public void setRating(double rating) {
        double rounded =  (double)Math.round(rating * 10) / 10;
        rewardLabel.setText("Your average rating was: " + rounded);
        rewardRating.setPartialRating(true);
        rewardRating.setRating(rating);
    }

}
