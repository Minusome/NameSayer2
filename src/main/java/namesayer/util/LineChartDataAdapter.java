package namesayer.util;

import javafx.scene.chart.XYChart;

import java.util.List;

public class LineChartDataAdapter {

    public XYChart.Series<String, Number> retrieveData(List<Double> rawData) {
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        int i = 1;
        for (Double point : rawData) {
            series.getData().add(new XYChart.Data<>(i + "", point));
            i++;
        }
        return series;
    }

}
