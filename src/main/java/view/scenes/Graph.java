package view.scenes;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Graph extends AbstractScene {

  private final BorderPane layout;

  /**
   * The id of the metric that is being graphed.
   */
  private final Integer metricId;
  Map<LocalDateTime, Double> data;
  private Button homeButton;
  private Button printButton;
  private Button compareButton;
  private Button dateFilterButton;
  private ListView<Node> compareList;
  private final ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>(14);
  private ComboBox<String> timeFilter;
  private ComboBox<String> compareControl1;
  private ComboBox<String> compareControl2;
  private ComboBox<String> compareControl3;
  private final JFreeChart chart;
  private final LocalDateTime startDate;

  private final DatePicker startDatePicker;
  private final DatePicker endDatePicker;

  private CheckBox maleCheckBox;
  private CheckBox femaleCheckBox;

  //  public Graph(Integer id, String title, String xAxisName, String yAxisName,
  public Graph(Integer id, JFreeChart chart, LocalDateTime startDate) {
    super();
    layout = new BorderPane();
    metricId = id;
    this.chart = chart;
    this.startDate = startDate;
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

    TimeSeries dataSeries = new TimeSeries("Impressions");
    TimeSeriesCollection dataset = new TimeSeriesCollection();
    dataset.addSeries(dataSeries);

    chart.getTitle().setPadding(0, 120, 0, 0);
    chart.getLegend().setPadding(0, 120, 0, 0);

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
    chart.getXYPlot().setDomainPannable(true);
    chart.getXYPlot().setRangePannable(true);

    var graphContainer = new HBox();
    graphContainer.setAlignment(Pos.CENTER);
    graphContainer.setPadding(new Insets(10, 10, 10, 10));
    graphContainer.getChildren().add(swingNode);
    layout.setCenter(graphContainer);

    var filterBar = new HBox();
    filterBar.setAlignment(Pos.CENTER);
    filterBar.setPadding(new Insets(10, 10, 10, 10));
    filterBar.setSpacing(10);

    startDatePicker.getStyleClass().add("start-date-picker");
    endDatePicker.getStyleClass().add("end-date-picker");

    startDatePicker.setValue(LocalDate.from(startDate).minusDays(14));
    endDatePicker.setValue(LocalDate.from(startDate));

    timeFilter = new ComboBox<>();
    timeFilter.getItems().addAll("Hour", "Day", "Week", "Month");
    timeFilter.setValue("Day");
    timeFilter.getStyleClass().add("time-filter");

    compareControl1 = new ComboBox<>();
    compareControl1.setStyle("-fx-background-color: #FFFFFF");
    compareControl2 = new ComboBox<>();
    compareControl2.setVisible(false);
    compareControl2.setStyle("-fx-background-color: #1C7C54");
    compareControl3 = new ComboBox<>();
    compareControl3.setVisible(false);
    compareControl3.setStyle("-fx-background-color: #CC2936");

    compareControlFactory(compareControl1);
    compareControlFactory(compareControl2);
    compareControlFactory(compareControl3);

    dateFilterButton = new Button("Filter");
    dateFilterButton.setDisable(true);

    var compareSpacer = new Region();
    compareSpacer.setPadding(new Insets(0, 0, 0, 20));
    var compareSpacer2 = new Region();
    HBox.setHgrow(compareSpacer2, Priority.ALWAYS);

    filterBar.getChildren()
        .addAll(compareControl1, compareControl2, compareControl3, compareSpacer, timeFilter,
            startDatePicker, endDatePicker, dateFilterButton, compareSpacer2, new Label(""));

    createCheckBoxes();

    layout.setBottom(filterBar);
    scene = new Scene(layout, 1280, 720);
    scene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/view/graph.css")).toExternalForm());

  }

  private void compareControlFactory(ComboBox<String> compareControl) {
    compareControl.getItems().addAll("No Filter", "Male", "Female");
    for (Age age : Age.values()) {
      compareControl.getItems().add(age.label);
    }
    for (Income income : Income.values()) {
      compareControl.getItems().add(income.label);
    }
    for (Context context : Context.values()) {
      compareControl.getItems().add(context.label);
    }
    compareControl.getStyleClass().add("time-filter");
    compareControl.setValue("No Filter");
  }

  /**
   * Creates the checkboxes for the filter bar
   */
  void createCheckBoxes() {
    //TODO: Add a "select all" checkbox for each category, and a select all for all categories
    //TODO: Fix alignment of everything

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

  public Button getHomeButton() {
    return homeButton;
  }

  public Button getPrintButton() {
    return printButton;
  }

  public Button getCompareButton() {
    return compareButton;
  }

  public ArrayList<CheckBox> getCheckboxes() {
    return checkboxes;
  }

  public Button getDateFilterButton() {
    return dateFilterButton;
  }

  public ComboBox<String> getTimeFilter() {
    return timeFilter;
  }

  public DatePicker getStartDatePicker() {
    return startDatePicker;
  }

  public DatePicker getEndDatePicker() {
    return endDatePicker;
  }

  public ComboBox<String> getCompareControl1() {
    return compareControl1;
  }

  public ComboBox<String> getCompareControl2() {
    return compareControl2;
  }

  public ComboBox<String> getCompareControl3() {
    return compareControl3;
  }

  public CheckBox getMaleCheckBox() {
    return maleCheckBox;
  }

  public CheckBox getFemaleCheckBox() {
    return femaleCheckBox;
  }
}
