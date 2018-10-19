package namesayer;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import namesayer.model.CompositeName;
import namesayer.persist.StatsManager;
import namesayer.util.EmptySelectionModel;
import namesayer.util.LineChartDataAdapter;
import namesayer.util.PieChartDataAdapter;
import namesayer.view.StatsNameListCell;

import java.io.IOException;

public class StatsScreenController {

    @FXML private JFXListView<Pair<CompositeName, Double>> badNamesList;
    @FXML private PieChart pieChart;
    @FXML private LineChart<String, Number> lineChart;

    private StatsManager statsManager = StatsManager.getInstance();

    public void initialize() {


        pieChart.setData(new PieChartDataAdapter().retrieveData(statsManager.getGlobalRatingFreq()));
        pieChart.setLegendVisible(false);
        pieChart.setLabelLineLength(10);

        lineChart.getData().add(new LineChartDataAdapter().retrieveData(statsManager.getAvgAssessRatingOverTime()));
        lineChart.setLegendVisible(false);
        badNamesList.setSelectionModel(new EmptySelectionModel<>());
        badNamesList.setCellFactory(param -> new StatsNameListCell());
        badNamesList.setItems(FXCollections.observableArrayList(statsManager.getDifficultNamesList()));
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
