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

import java.io.IOException;

public class StatsScreenController {

    @FXML private JFXListView<PartialName> badNamesList;
    @FXML private PieChart pieChart;
    @FXML private LineChart<String, Number> lineChart;


    public void initialize() {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("1", 13),
                        new PieChart.Data("2", 25),
                        new PieChart.Data("3", 10),
                        new PieChart.Data("4", 22),
                        new PieChart.Data("5", 30));
        pieChart.setData(pieChartData);
        pieChart.setLegendVisible(false);
        pieChart.setLabelLineLength(10);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        //populating the series with data
        series.getData().add(new XYChart.Data<>("1", 5.0));
        series.getData().add(new XYChart.Data<>("2", 4.8));
        series.getData().add(new XYChart.Data<>("3", 2.2));
        series.getData().add(new XYChart.Data<>("4", 1.0));
        series.getData().add(new XYChart.Data<>("5", 0.7));
        series.getData().add(new XYChart.Data<>("6", 3.2));
        series.getData().add(new XYChart.Data<>("7", 2.4));
        series.getData().add(new XYChart.Data<>("8", 4.5));
        series.getData().add(new XYChart.Data<>("9", 5.0));
        series.getData().add(new XYChart.Data<>("10", 2.1));
        series.getData().add(new XYChart.Data<>("11", 4.9));
        series.getData().add(new XYChart.Data<>("12", 1.0));

        lineChart.getData().add(series);
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
