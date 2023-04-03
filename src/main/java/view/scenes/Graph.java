package view.scenes;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import view.components.CompareItem;


public class Graph extends AbstractScene {

  private final BorderPane layout;

  /**
   * The id of the metric that is being graphed.
   */
  private final Integer metricId;
  Map<LocalDateTime, Double> data;
  private Button homeButton;
  private Button printButton;
  private Button segmentFilterButton;
  private Button dateFilterButton;
  private ListView<Node> compareList;
  private final ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>(14);
  private ComboBox<String> timeFilter;
  private ComboBox<CompareItem> compareControl1;
  private ComboBox<CompareItem> compareControl2;
  private ComboBox<CompareItem> compareControl3;
  private final JFreeChart lineChart;
  private final JFreeChart histogram;

  private final DatePicker startDatePicker;
  private final DatePicker endDatePicker;

  private CheckBox maleCheckBox;
  private CheckBox femaleCheckBox;

  private ChartPanel chartPanel;

  private boolean showLineGraph = true;


  /**
   * Class Constructor
   *
   * @param id        the metric ID
   * @param lineChart     the lineChart
   * @param histogram
   */
  public Graph(Integer id, JFreeChart lineChart, JFreeChart histogram) {
    super();
    layout = new BorderPane();
    metricId = id;
    this.lineChart = lineChart;
    this.histogram = histogram;
    this.startDatePicker = new DatePicker();
    this.endDatePicker = new DatePicker();
  }

  /**
   * Creates all the components of the scene, and adds them to the layout
   */
  public void createScene() {

    var topBar = new HBox();
    topBar.setAlignment(Pos.BOTTOM_CENTER);

    topBar.getStyleClass().add("topBar");

    homeButton = new Button("Home");
    homeButton.getStyleClass().add("button");
    HBox.setHgrow(homeButton, Priority.ALWAYS);

    var graphTitle = new Label("AdViz - Graph");
    graphTitle.getStyleClass().add("graphTitle");


    printButton = new Button("Print");
    printButton.getStyleClass().add("button");


    segmentFilterButton = new Button("Filter");
    segmentFilterButton.getStyleClass().add("button");


    topBar.getChildren().add(homeButton);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    topBar.getChildren().add(spacer);
    topBar.getChildren().add(graphTitle);
    Region spacer2 = new Region();
    HBox.setHgrow(spacer2, Priority.ALWAYS);
    topBar.getChildren().add(spacer2);
    topBar.getChildren().add(printButton);
    topBar.getChildren().add(segmentFilterButton);

    layout.setTop(topBar);

    BorderPane.setMargin(topBar, new Insets(10, 10, 10, 10));

    var filterBar = new HBox();
    filterBar.setAlignment(Pos.CENTER);
    filterBar.setPadding(new Insets(10, 10, 10, 10));
    filterBar.setSpacing(10);

    if (metricId == 4) {
      ToggleButton toggleGraph = new ToggleButton("Histogram");
      toggleGraph.setOnAction(event -> {
        if (toggleGraph.isSelected()) {
          toggleGraph.setText("Line Graph");
          showLineGraph = false;
          chartPanel.setChart(histogram);
        } else {
          toggleGraph.setText("Histogram");
          showLineGraph = true;
          chartPanel.setChart(lineChart);
        }
      });
      toggleGraph.getStyleClass().add("button");
      filterBar.getChildren().add(toggleGraph);
    }

    if (showLineGraph) {
      lineChart.getTitle().setPadding(0, 120, 0, 0);
      lineChart.getLegend().setPadding(0, 120, 0, 0);
      chartPanel = new ChartPanel(lineChart);
    } else {
      histogram.getTitle().setPadding(0, 120, 0, 0);
      histogram.getLegend().setPadding(0, 120, 0, 0);
      chartPanel = new ChartPanel(histogram);
    }


    SwingNode swingNode = new SwingNode();
    SwingUtilities.invokeLater(() -> {
        chartPanel.getChart().setBackgroundPaint(new Color(18, 18, 18));
        lineChart.getTitle().setPaint(Color.WHITE);
        lineChart.getTitle().setFont(new Font("Roboto", Font.PLAIN, 20));
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
        chartPanel.getChart().getXYPlot().getRenderer().setSeriesStroke(0, new BasicStroke(4.0f));
      swingNode.setContent(chartPanel);

      chartPanel.getChart().getLegend().setItemFont(new Font("Roboto", Font.PLAIN, 12));
      chartPanel.getChart().getLegend().setItemPaint(Color.WHITE);
      chartPanel.getChart().getLegend().setBackgroundPaint(Color.decode("#121212"));

      chartPanel.setMouseWheelEnabled(true);
      chartPanel.setDomainZoomable(true);
      chartPanel.setRangeZoomable(true);
      chartPanel.setZoomTriggerDistance(Integer.MAX_VALUE);
      chartPanel.zoomOutBoth(0, 0);
      chartPanel.restoreAutoBounds();
      chartPanel.setFillZoomRectangle(false);
      chartPanel.setZoomOutlinePaint(new Color(0f, 0f, 0f, 0f));
      chartPanel.setZoomInFactor(1.0);
      chartPanel.setZoomOutFactor(1.0);
    });
    lineChart.getXYPlot().setDomainPannable(true);
    lineChart.getXYPlot().setRangePannable(true);

    var graphContainer = new HBox();
    graphContainer.setAlignment(Pos.CENTER);
    graphContainer.setPadding(new Insets(10, 10, 10, 10));
    graphContainer.getChildren().add(swingNode);
    layout.setCenter(graphContainer);

    startDatePicker.getStyleClass().add("start-date-picker");
    endDatePicker.getStyleClass().add("end-date-picker");


    timeFilter = new ComboBox<>();
    timeFilter.getItems().addAll("Hour", "Day", "Week", "Month");
    timeFilter.setValue("Day");
    timeFilter.getStyleClass().add("time-filter");

    dateFilterButton = new Button("Apply");
    dateFilterButton.setDisable(true);

    compareControl1 = new ComboBox<>();
    compareControl1.setStyle("-fx-background-color: #FFFFFF");
    compareControl2 = new ComboBox<>();
    compareControl2.setVisible(true);
    compareControl2.setStyle("-fx-background-color: #1C7C54");
    compareControl3 = new ComboBox<>();
    compareControl3.setVisible(false);
    compareControl3.setStyle("-fx-background-color: #CC2936");

    compareControlFactory(compareControl1);
    compareControlFactory(compareControl2);
    compareControlFactory(compareControl3);

    var compareSpacer = new Region();
    compareSpacer.setPadding(new Insets(0, 0, 0, 20));
    var compareSpacer2 = new Region();
    HBox.setHgrow(compareSpacer2, Priority.ALWAYS);

    filterBar.getChildren()
        .addAll(compareControl1, compareSpacer, compareControl2, compareSpacer2, compareControl3, timeFilter,startDatePicker, endDatePicker, dateFilterButton);

    createCheckBoxes();

    layout.setBottom(filterBar);
    scene = new Scene(layout, 1280, 720);
    scene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/view/graph.css")).toExternalForm());

  }


  /**
   * @param compareControl
   */
  private void compareControlFactory(ComboBox<CompareItem> compareControl) {
    CompareItem defaultItem = new CompareItem("Default", null);
    compareControl.getItems().addAll(defaultItem, new CompareItem("Male", "male_1"), new CompareItem("Female", "female_1"));
    for (Age age : Age.values()) {
      compareControl.getItems().add(new CompareItem(age.label, "age_" + age.idx));
    }
    for (Income income : Income.values()) {
      compareControl.getItems().add(new CompareItem(income.label, "income_" + income.idx));
    }
    for (Context context : Context.values()) {
      compareControl.getItems().add(new CompareItem(context.label, "context_" + context.idx));
    }
    compareControl.getStyleClass().add("time-filter");
    compareControl.setValue(defaultItem);
  }

  /**
   * Creates the checkboxes for the filter bar
   */
  void createCheckBoxes() {

    compareList = new ListView<>();

    var genderText = new Label("Gender of Audience:");
    genderText.getStyleClass().add("list-cell-text");
    maleCheckBox = new CheckBox("Male");
    maleCheckBox.getStyleClass().add("checkbox");
    maleCheckBox.setId("male_1");
    femaleCheckBox = new CheckBox("Female");
    femaleCheckBox.getStyleClass().add("checkbox");
    femaleCheckBox.setId("female_1");
    this.checkboxes.add(maleCheckBox);
    this.checkboxes.add(femaleCheckBox);

    compareList.getStyleClass().add("list-cell");
    compareList.getItems().addAll(genderText, maleCheckBox, femaleCheckBox);

    var ageText = new Label("Age of Audience:");
    ageText.getStyleClass().add("list-cell-text");
    compareList.getItems().add(ageText);
    for (Age a : Age.values()) {
      CheckBox box = new CheckBox(a.label);
      box.setId("age_" + a.idx);
      compareList.getItems().add(box);
      this.checkboxes.add(box);
    }

    var incomeText = new Label("Income of Audience:");
    incomeText.getStyleClass().add("list-cell-text");
    compareList.getItems().add(incomeText);
    for (Income i : Income.values()) {
      CheckBox box = new CheckBox(i.label);
      box.setId("income_" + i.idx);
      compareList.getItems().add(box);
      this.checkboxes.add(box);
    }

    var contextText = new Label("Location of Ad Interaction:");
    contextText.getStyleClass().add("list-cell-text");
    compareList.getItems().add(contextText);
    for (Context c : Context.values()) {
      CheckBox box = new CheckBox(c.label);
      box.setId("context_" + c.idx);
      compareList.getItems().add(box);
      this.checkboxes.add(box);
    }
    this.checkboxes.forEach(c -> c.getStyleClass().add("checkbox"));
    layout.setRight(compareList);

  }

  public ChartPanel getLineChart() {
    return chartPanel;
  }

  /**
   * @return get home button
   */
  public Button getHomeButton() {
    return homeButton;
  }

  /**
   * @return get print button
   */
  public Button getPrintButton() {
    return printButton;
  }

  /**
   * @return get compare button
   */
  public Button getSegmentFilterButton() {
    return segmentFilterButton;
  }

  /**
   * @return get the list of check boxes
   */
  public ArrayList<CheckBox> getCheckboxes() {
    return checkboxes;
  }

  /**
   * @return get the date filter button
   */
  public Button getDateFilterButton() {
    return dateFilterButton;
  }

  /**
   * @return get the box of time filters
   */
  public ComboBox<String> getTimeFilter() {
    return timeFilter;
  }

  /**
   * @return get the start date picker
   */
  public DatePicker getStartDatePicker() {
    return startDatePicker;
  }

  /**
   * @return get the end date picker
   */
  public DatePicker getEndDatePicker() {
    return endDatePicker;
  }

  /**
   * @return
   */
  public ComboBox<CompareItem> getCompareControl1() {
    return compareControl1;
  }

  public ComboBox<CompareItem> getCompareControl2() {
    return compareControl2;
  }

  public ComboBox<CompareItem> getCompareControl3() {
    return compareControl3;
  }

  /**
   * @return get the male check box
   */
  public CheckBox getMaleCheckBox() {
    return maleCheckBox;
  }

  /**
   * @return get the female check box
   */
  public CheckBox getFemaleCheckBox() {
    return femaleCheckBox;
  }
}
