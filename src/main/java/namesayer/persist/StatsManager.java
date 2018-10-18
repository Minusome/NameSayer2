package namesayer.persist;

import javafx.util.Pair;
import namesayer.model.PartialName;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static namesayer.persist.Config.STATS_FILE;

public class StatsManager {

    private Map<Integer, Integer> globalRatingFreq = new HashMap<>();
    private List<Double> avgRatingOverTime = new ArrayList<>();
    private Map<PartialName, Pair<Double, Integer>> difficultNamesRunningAvg = new HashMap<>();


    private static StatsManager instance;

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

    public StatsManager getInstance() {
        if (instance == null) {
            instance = new StatsManager();
        }
        return instance;
    }


    public void save() {
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
    }

    public void updateRatingFreq(int rating) {
        assert (0 <= rating && rating <= 5);
        Integer frequency = globalRatingFreq.get(rating);
        if (frequency == null) {
            globalRatingFreq.put(rating, 1);
        } else {
            globalRatingFreq.put(rating, ++frequency);
        }
    }

    public void updateAvgRating(Double avgRating) {
        avgRatingOverTime.add(avgRating);
    }

    public void updateDifficultName(PartialName name, int rating) {
        assert (0 <= rating && rating <= 5);
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


    public List<PartialName> getDifficultNamesList() {
        return difficultNamesRunningAvg.entrySet()
                                       .stream()
                                       .sorted(Collections.reverseOrder(
                                               Map.Entry.comparingByValue(
                                                       Comparator.comparing(
                                                               Pair::getKey))))
                                       .map(Map.Entry::getKey)
                                       .collect(Collectors.toList());
    }


    public Map<Integer, Integer> getGlobalRatingFreq() {
        return Collections.unmodifiableMap(globalRatingFreq);
    }

    public List<Double> getAvgRatingOverTime() {
        return Collections.unmodifiableList(avgRatingOverTime);
    }
}
