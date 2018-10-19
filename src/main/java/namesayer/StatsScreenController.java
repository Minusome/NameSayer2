package namesayer;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import namesayer.model.PartialName;
import namesayer.util.LineChartDataAdapter;
import namesayer.util.PieChartDataAdapter;

import java.io.IOException;

public class StatsScreenController {

    @FXML private JFXListView<PartialName> badNamesList;
    @FXML private PieChart pieChart;
    @FXML private LineChart<String, Number> lineChart;


    public void initialize() {
        pieChart.setData(PieChartDataAdapter.retrieveData());
        pieChart.setLegendVisible(false);
        pieChart.setLabelLineLength(10);

        lineChart.getData().add(LineChartDataAdapter.retrieveData());
        lineChart.setLegendVisible(false);
    }

    public void onBackButtonClicked(MouseEvent mouseEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/MenuScreen.fxml"));
            Scene scene = lineChart.getScene();
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
