package namesayer.view.controller;

import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.CacheHint;
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
import namesayer.view.cell.StatsNameListCell;

import java.io.IOException;

import static namesayer.util.Screen.MAIN_MENU;

public class StatsScreenController {

    @FXML private JFXListView<Pair<CompositeName, Double>> badNamesList;
    @FXML private PieChart pieChart;
    @FXML private LineChart<String, Number> lineChart;

    private StatsManager statsManager = StatsManager.getInstance();


    public void initialize() {

        pieChart.setLegendVisible(false);
        pieChart.setLabelLineLength(10);
        lineChart.setLegendVisible(false);
        badNamesList.setSelectionModel(new EmptySelectionModel<>());
        badNamesList.setCellFactory(param -> new StatsNameListCell());

        Thread thread = new Thread(() -> {
            Platform.runLater(() -> {
                badNamesList.setItems(FXCollections.observableArrayList(statsManager.getDifficultNamesList()));
                pieChart.setData(new PieChartDataAdapter().retrieveData(statsManager.getGlobalRatingFreq()));
                lineChart.getData().add(new LineChartDataAdapter().retrieveData(statsManager.getAvgAssessRatingOverTime()));
                lineChart.getXAxis().setAnimated(false);
            });
        });
        thread.start();
    }

    public void onBackButtonClicked(MouseEvent mouseEvent) {
        MAIN_MENU.loadWithNode(lineChart);
    }
}
