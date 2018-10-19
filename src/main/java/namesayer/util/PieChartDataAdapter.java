package namesayer.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import namesayer.persist.StatsManager;

import java.util.Map;

public class PieChartDataAdapter {

    public static ObservableList<PieChart.Data> retrieveData() {
        Map<Integer, Integer> rawData = StatsManager.getInstance().getGlobalRatingFreq();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for (Map.Entry<Integer, Integer> dataEntry : rawData.entrySet()) {
            pieChartData.add(new PieChart.Data(dataEntry.getKey().toString(), dataEntry.getValue()));
        }

        return pieChartData;
    }


}
