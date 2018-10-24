package namesayer.persist;

import javafx.util.Pair;
import namesayer.model.CompositeName;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static namesayer.persist.Config.STATS_FILE;

/**
 * Singleton responsible for calculating all statistics data invovling ratings
 * Also saves and loads statistics file from disk
 */

public class StatsManager implements Serializable {

    private Map<Integer, Integer> globalRatingFreq = new HashMap<>();
    private List<Double> avgAssessRatingOverTime = new ArrayList<>();
    private Map<CompositeName, Pair<Double, Integer>> difficultNamesRunningAvg = new HashMap<>();


    private static StatsManager instance;

    /**
     * Reads the statistics file and loads into singleton instance before
     * getInstance() is called
     */
    static {
        try {
            FileInputStream file = new FileInputStream(STATS_FILE.toFile());
            ObjectInputStream in = new ObjectInputStream(file);
            instance = (StatsManager) in.readObject();
            file.close();
            in.close();
        } catch (FileNotFoundException e) {
            System.err.println("Saved stats file not discovered. New one generated.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private StatsManager() {
        for (int i = 1; i <= 5; i++) {
            globalRatingFreq.put(i, 1);
        }
    }

    public static StatsManager getInstance() {
        if (instance == null) {
            instance = new StatsManager();
        }
        return instance;
    }

    /**
     * On a new thread, saves this object into a file
     */
    public void save() {
        Thread thread = new Thread(() -> {
            try {
                FileOutputStream file = new FileOutputStream(STATS_FILE.toFile());
                ObjectOutputStream out = new ObjectOutputStream(file);
                out.writeObject(this);
                out.flush();
                out.close();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
       thread.start();
    }

    /**
     * Increase the count for global rating frequencies
     * Ratings are between (1-5) inclusive
     *
     * @param rating Rating between (1-5)
     */
    public void updateRatingFreq(int rating) {
        Integer frequency = globalRatingFreq.get(rating);
        if (frequency == null) {
            globalRatingFreq.put(rating, 1);
        } else {
            globalRatingFreq.put(rating, ++frequency);
        }
    }

    /**
     * Caches an Assessment rating final score (i.e. the average)
     *
     * @param avgRating Average rating
     */
    public void updateAvgAssessRating(Double avgRating) {
        avgAssessRatingOverTime.add(avgRating);
    }

    /**
     * Updates the average rating for a difficult to pronounce CompositeName
     * (i.e. name with rating <2 )
     *
     * HashMap stores <Double, Integer>
     *     Double -> Moving Average
     *     Integer -> Incrementing sample size to calcualte the average
     *
     * @param name CompositeName
     * @param rating Rating of name
     */
    public void updateDifficultName(CompositeName name, int rating) {
        if (rating > 2) {
            return;
        }
        Pair<Double, Integer> avgRatingForName = difficultNamesRunningAvg.get(name);
        if (avgRatingForName == null) {
            difficultNamesRunningAvg.put(name, new Pair<>((double) rating, 1));
        } else {
            Double oldAvg = avgRatingForName.getKey();
            Integer count = avgRatingForName.getValue();
            Double newAvg = oldAvg + ((rating - oldAvg) / (++count));
            difficultNamesRunningAvg.put(name, new Pair<>(newAvg, count));
        }
    }

    /**
     * Sorts the cached CompositeNames by lowest rating first
     *
     * @return Returns a List of Pair<CompositeName,Double>> containing the Name and its rating
     */
    public List<Pair<CompositeName,Double>> getDifficultNamesList() {
        return difficultNamesRunningAvg.entrySet()
                                       .stream()
                                       .sorted(Map.Entry.comparingByValue(
                                                       Comparator.comparing(
                                                               Pair::getKey)))
                                       .map(e -> new Pair<>(e.getKey(), e.getValue().getKey()))
                                       .collect(Collectors.toList());
    }


    public Map<Integer, Integer> getGlobalRatingFreq() {
        return Collections.unmodifiableMap(globalRatingFreq);
    }

    public List<Double> getAvgAssessRatingOverTime() {
        return Collections.unmodifiableList(avgAssessRatingOverTime);
    }
}
