package namesayer.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.nio.file.Path;

public class CompleteRecording extends Recording{

    private DoubleProperty rating = new SimpleDoubleProperty(3.0);

    public CompleteRecording(Path recordingPath) {
        super(recordingPath);
    }


    public double getRating() {
        return rating.get();
    }

    public DoubleProperty ratingProperty() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }



}
