package namesayer.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.nio.file.Path;
import java.time.LocalDateTime;

/**
 * Represents an audio file of a CompositeName
 */

public class CompositeRecording extends Recording {

    //Default rating is 3.0
    private double rating = 3.0;

    private LocalDateTime timeStamp;

    public CompositeRecording(Path recordingPath) {
        super(recordingPath);
        timeStamp = LocalDateTime.now();
    }

    public double getRating() {
        return rating;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public DoubleProperty ratingProperty() {
        SimpleDoubleProperty doubleProperty = new SimpleDoubleProperty(rating);
        doubleProperty.addListener((observable, oldValue, newValue) -> rating = newValue.doubleValue());
        return doubleProperty;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

}
