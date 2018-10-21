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

public class StatsManager implements Serializable {

    private Map<Integer, Integer> globalRatingFreq = new HashMap<>();
    private List<Double> avgAssessRatingOverTime = new ArrayList<>();
    private Map<CompositeName, Pair<Double, Integer>> difficultNamesRunningAvg = new HashMap<>();


    private static StatsManager instance;

    static {
        try {
            FileInputStream file = new FileInputStream(STATS_FILE.toFile());
            ObjectInputStream in = new ObjectInputStream(file);
            instance = (StatsManager) in.readObject();
            System.out.println("Successfully read stats file");
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

    public void updateRatingFreq(int rating) {
//        assert (0 <= rating && rating <= 5);
        Integer frequency = globalRatingFreq.get(rating);
        if (frequency == null) {
            globalRatingFreq.put(rating, 1);
        } else {
            globalRatingFreq.put(rating, ++frequency);
        }
    }

    public void updateAvgAssessRating(Double avgRating) {
        avgAssessRatingOverTime.add(avgRating);
    }

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
