package namesayer.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.controlsfx.control.Rating;

public class RewardCardController {

    @FXML private Label rewardLabel;
    @FXML private Rating rewardRating;

    public void setRating(double rating) {
        System.out.println(rating);
        double rounded =  (double)Math.round(rating * 10) / 10;
        rewardLabel.setText("Your average rating was: " + rounded);
        rewardRating.setPartialRating(true);
        rewardRating.setRating(rating);
    }

}
