package namesayer.view;

import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import org.controlsfx.control.Rating;

public class RatingPopupController {

    @FXML private Rating rating;
    private DoubleProperty property;

    public void bind(DoubleProperty doubleProperty) {
        property = doubleProperty;
        rating.ratingProperty().bindBidirectional(doubleProperty);
    }

    public void unbind() {
        rating.ratingProperty().unbindBidirectional(property);
    }

    public int getRating() {
        return (int) Math.round(property.get());
    }
}
