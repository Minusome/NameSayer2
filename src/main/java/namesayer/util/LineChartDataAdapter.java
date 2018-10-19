package namesayer.util;

import javafx.scene.chart.XYChart;
import namesayer.persist.StatsManager;

import java.util.List;

public class LineChartDataAdapter {

    public static XYChart.Series<String, Number> retrieveData() {
        List<Double> data = StatsManager.getInstance().getAvgAssessRatingOverTime();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        
        int i = 1;
        for (Double point : data) {
            series.getData().add(new XYChart.Data<>(i + "", point));
            i++;
        }
        return series;
    }

}
