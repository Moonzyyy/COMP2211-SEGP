package view.scenes;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.util.Objects;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Graph extends AbstractScene {

  private final BorderPane layout;
  private Button homeButton;
  private Button printButton;
  private Button compareButton;
  private Button filterButton;
  /**
   * The id of the metric that is being graphed.
   */
  private final Integer metricId;

  public Graph(Integer id) {
    super();
    layout = new BorderPane();
    metricId = id;

  }

  public void createScene() {
    var topBar = new HBox();
    topBar.setAlignment(Pos.BOTTOM_CENTER);

    topBar.getStyleClass().add("topBar");

    homeButton = new Button("Home");
    homeButton.getStyleClass().add("button");
    HBox.setHgrow(homeButton, Priority.ALWAYS);

    var graphTitle = new Label("AdViz - Graph");
    graphTitle.getStyleClass().add("graphTitle");
    //HBox.setHgrow(graphTitle, Priority.ALWAYS);

    printButton = new Button("Print");
    printButton.getStyleClass().add("button");
//    HBox.setHgrow(printButton, Priority.ALWAYS);

    compareButton = new Button("Compare");
    compareButton.getStyleClass().add("button");
//    HBox.setHgrow(compareButton, Priority.ALWAYS);

    topBar.getChildren().add(homeButton);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    topBar.getChildren().add(spacer);
    topBar.getChildren().add(graphTitle);
    Region spacer2 = new Region();
    HBox.setHgrow(spacer2, Priority.ALWAYS);
    topBar.getChildren().add(spacer2);
    topBar.getChildren().add(printButton);
    topBar.getChildren().add(compareButton);

    layout.setTop(topBar);

    BorderPane.setMargin(topBar, new Insets(10, 10, 10, 10));

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
        false,
        true,
        false
    );

    // Best way to style the chart unfortunately
    // I recommend looking at the docs for JFreeChart to see what you can do
    SwingNode swingNode = new SwingNode();
    SwingUtilities.invokeLater(() -> {
      ChartPanel chartPanel = new ChartPanel(chart);
      chartPanel.getChart().setBackgroundPaint(new Color(18, 18, 18));
      chart.getTitle().setPaint(Color.WHITE);
      chart.getTitle().setFont(new Font("Roboto", Font.PLAIN, 20));
      chartPanel.getChart().getPlot().setBackgroundPaint(Color.DARK_GRAY);
      chartPanel.getChart().getPlot().setOutlinePaint(Color.DARK_GRAY);
      chartPanel.getChart().getXYPlot().getRenderer().setSeriesPaint(0, Color.WHITE);
      chartPanel.getChart().getXYPlot().getDomainAxis().setAxisLinePaint(Color.WHITE);
      chartPanel.getChart().getXYPlot().getDomainAxis().setTickLabelPaint(Color.WHITE);
      chartPanel.getChart().getXYPlot().getDomainAxis().setLabelPaint(Color.WHITE);
      chartPanel.getChart().getXYPlot().getDomainAxis()
          .setLabelFont(new Font("Roboto", Font.PLAIN, 12));
      chartPanel.getChart().getXYPlot().getDomainAxis()
          .setTickLabelFont(new Font("Roboto", Font.PLAIN, 12));
      chartPanel.getChart().getXYPlot().getRangeAxis().setAxisLinePaint(Color.WHITE);
      chartPanel.getChart().getXYPlot().getRangeAxis().setTickLabelPaint(Color.WHITE);
      chartPanel.getChart().getXYPlot().getRangeAxis().setLabelPaint(Color.WHITE);
      chartPanel.getChart().getXYPlot().getRangeAxis()
          .setLabelFont(new Font("Roboto", Font.PLAIN, 12));
      chartPanel.getChart().getXYPlot().getRangeAxis()
          .setTickLabelFont(new Font("Roboto", Font.PLAIN, 12));

      chartPanel.getChart().getXYPlot().getRenderer()
          .setDefaultItemLabelFont(new Font("Roboto", Font.PLAIN, 12));
      swingNode.setContent(chartPanel);
    });

    var graphContainer = new HBox();
    graphContainer.setAlignment(Pos.CENTER);
    graphContainer.setPadding(new Insets(10, 10, 10, 10));
    graphContainer.getChildren().add(swingNode);
    layout.setCenter(graphContainer);

    var filterBar = new HBox();
    filterBar.setAlignment(Pos.CENTER);
    filterBar.setPadding(new Insets(10, 10, 10, 10));
    filterBar.setSpacing(10);

    var startDatePicker = new DatePicker();
    var endDatePicker = new DatePicker();

    // set the maximum date of the first date picker to the selected date on the second date picker
    startDatePicker.setDayCellFactory(param -> new DateCell() {
      @Override
      public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (endDatePicker.getValue() != null) {
          setDisable(item.isAfter(endDatePicker.getValue()));
        }
      }
    });

    // set the minimum date of the second date picker to the selected date on the first date picker
    endDatePicker.setDayCellFactory(param -> new DateCell() {
      @Override
      public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        if (startDatePicker.getValue() != null) {
          setDisable(item.isBefore(startDatePicker.getValue()));
        }
      }
    });

    filterButton = new Button("Filter");
    filterButton.setOnAction(e -> {
      LocalDate startDate = startDatePicker.getValue();
      LocalDate endDate = endDatePicker.getValue();
      if (startDate != null && endDate != null) {

        TimeSeries filteredSeries = new TimeSeries("Filtered Clicks");
        for (int i = 0; i < clicksSeries.getItemCount(); i++) {
          Day day = (Day) clicksSeries.getTimePeriod(i);
          if (day.compareTo(
              new Day(startDate.getDayOfMonth(), startDate.getMonthValue(),
                  startDate.getYear())) >= 0
              && day.compareTo(
              new Day(endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear())) <= 0) {
            filteredSeries.add(clicksSeries.getDataItem(i));
          }
        }

        dataset.removeAllSeries();
        dataset.addSeries(filteredSeries);
      }
    });
    filterBar.getChildren().addAll(startDatePicker, endDatePicker, filterButton);
    layout.setBottom(filterBar);

    scene = new Scene(layout, 1280, 720);
    scene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/view/graph.css")).toExternalForm());

  }

  public Button getHomeButton() {
    return homeButton;
  }

  public Button getPrintButton() {
    return printButton;
  }

  public Button getCompareButton() {
    return compareButton;
  }

  public Button getFilterButton() {
    return filterButton;
  }

}
