package view.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import view.AppView;

import javafx.embed.swing.SwingNode;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.ChartPanel;

import java.util.Objects;

public class Graph extends AbstractScene {

  private final BorderPane layout;

  public Graph(Stage stage, AppView view) {
    super(stage, view);
    layout = new BorderPane();
    createScene();
  }

  void createScene() {
    scene = new Scene(layout, 800, 600);
    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/view/graph.css")).toExternalForm());

    var topBar = new HBox();
    topBar.setAlignment(Pos.BOTTOM_CENTER);
    topBar.getStyleClass().add("topBar");

    var homeButton = new Button("Home");
    homeButton.setOnAction(e -> {
      getStage().setScene(new Dashboard(getStage(), getView()).getScene());
    });
    homeButton.getStyleClass().add("button");
    HBox.setHgrow(homeButton, Priority.ALWAYS);

    var graphTitle = new Label("AdViz - Graph");
    graphTitle.getStyleClass().add("graphTitle");
    HBox.setHgrow(graphTitle, Priority.ALWAYS);

    var printButton = new Button("Print");
    printButton.getStyleClass().add("button");
    HBox.setHgrow(printButton, Priority.ALWAYS);
    var compareButton = new Button("Compare");
    compareButton.getStyleClass().add("button");
    HBox.setHgrow(compareButton, Priority.ALWAYS);

    topBar.getChildren().addAll(homeButton, graphTitle, printButton, compareButton);
    layout.setTop(topBar);

    TimeSeries clicksSeries = new TimeSeries("Clicks");
    TimeSeriesCollection dataset = new TimeSeriesCollection();
    dataset.addSeries(clicksSeries);

    clicksSeries.add(new Day(1, 1, 2023), 70);
    clicksSeries.add(new Day(2, 1, 2023), 100);
    clicksSeries.add(new Day(3, 1, 2023), 80);
    clicksSeries.add(new Day(4, 1, 2023), 120);
    clicksSeries.add(new Day(5, 1, 2023), 150);
    clicksSeries.add(new Day(6, 1, 2023), 90);
    clicksSeries.add(new Day(7, 1, 2023), 130);
    clicksSeries.add(new Day(8, 1, 2023), 110);
    clicksSeries.add(new Day(9, 1, 2023), 160);
    clicksSeries.add(new Day(10, 1, 2023), 200);
    clicksSeries.add(new Day(11, 1, 2023), 180);
    clicksSeries.add(new Day(12, 1, 2023), 230);
    clicksSeries.add(new Day(13, 1, 2023), 250);
    clicksSeries.add(new Day(14, 1, 2023), 190);

    JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Clicks over Time",
            "Date",
            "Clicks",
            dataset,
            true,
            true,
            false
    );

    SwingNode swingNode = new SwingNode();
    SwingUtilities.invokeLater(() -> {
      ChartPanel chartPanel = new ChartPanel(chart);
      swingNode.setContent(chartPanel);
    });

    layout.setCenter(swingNode);


  }

}
