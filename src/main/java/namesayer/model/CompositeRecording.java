package namesayer.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.nio.file.Path;
import java.time.LocalDateTime;

public class CompositeRecording extends Recording{

    private DoubleProperty rating = new SimpleDoubleProperty(3.0);

    private LocalDateTime timeStamp;

    public CompositeRecording(Path recordingPath) {
        super(recordingPath);
        timeStamp = LocalDateTime.now();
    }

    public double getRating() {
        return rating.get();
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public DoubleProperty ratingProperty() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }



}
