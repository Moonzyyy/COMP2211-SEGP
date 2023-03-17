package view.scenes;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Objects;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javax.swing.SwingUtilities;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class Graph extends AbstractScene {

  private final BorderPane layout;
  /**
   * The id of the metric that is being graphed.
   */
  private final Integer metricId;
  Map<LocalDateTime, Double> data;
  String xAxisName;
  String yAxisName;
  String title;
  private Button homeButton;
  private Button printButton;
  private Button compareButton;
  private Button filterButton;
  private JFreeChart chart;
  private CheckBox male;
  private CheckBox female;
  private ListView compareList;

  public Graph(Integer id, String title, String xAxisName, String yAxisName,
      Map<LocalDateTime, Double> data) {
    super();
    layout = new BorderPane();
    metricId = id;
    this.data = data;
    this.xAxisName = xAxisName;
    this.yAxisName = yAxisName;
    this.title = title;

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

    TimeSeries dataSeries = new TimeSeries("Impressions");
    TimeSeriesCollection dataset = new TimeSeriesCollection();
    dataset.addSeries(dataSeries);

    for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
      //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime date = entry.getKey();
      //Day day = Day.parseDay(new SimpleDateFormat("yyyy-MM-dd").format(date));
      Hour hour = new Hour(date.getHour(), date.getDayOfMonth(), date.getMonthValue(),
          date.getYear());
      dataSeries.addOrUpdate(hour, entry.getValue());
    }
    chart = ChartFactory.createTimeSeriesChart(title, xAxisName, yAxisName, dataset, false, true,
        false);

    chart.getTitle().setPadding(0, 120, 0, 0);

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

      chartPanel.setMouseWheelEnabled(true);
      chartPanel.setDomainZoomable(true);
      chartPanel.setRangeZoomable(true);
      chartPanel.setZoomTriggerDistance(Integer.MAX_VALUE);
      chartPanel.setFillZoomRectangle(false);
      chartPanel.setZoomOutlinePaint(new Color(0f, 0f, 0f, 0f));


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

    var startDatePicker = new DatePicker();
    var endDatePicker = new DatePicker();
    startDatePicker.getStyleClass().add("start-date-picker");
    endDatePicker.getStyleClass().add("end-date-picker");

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
    ComboBox<String> timeFilter = new ComboBox<>();
    timeFilter.getItems().addAll("Hour", "Day", "Week", "Month");
    filterButton = new Button("Filter");
    filterButton.setOnAction(e -> {
      LocalDate startDate = startDatePicker.getValue();
      LocalDate endDate = endDatePicker.getValue();
      if (startDate != null && endDate != null) {
        // Create start and end Date objects with selected times
        Date startTimeDate = new GregorianCalendar(startDate.getYear(),
            startDate.getMonthValue() - 1, startDate.getDayOfMonth()).getTime();
        Date endTimeDate = new GregorianCalendar(endDate.getYear(), endDate.getMonthValue() - 1,
            endDate.getDayOfMonth()).getTime();

        TimeSeries filteredSeries = new TimeSeries("Filtered Series");
        for (int i = 0; i < dataSeries.getItemCount(); i++) {
          Hour hour = (Hour) dataSeries.getTimePeriod(i);
          Date date = hour.getStart();
          if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
            filteredSeries.add(dataSeries.getDataItem(i));
          }
        }

        dataset.removeAllSeries();
        dataset.addSeries(filteredSeries);
      }
    });
    filterBar.getChildren().addAll(timeFilter,startDatePicker, endDatePicker, filterButton);

    createCheckBoxes();

    layout.setBottom(filterBar);
    scene = new Scene(layout, 1280, 720);
    scene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/view/graph.css")).toExternalForm());

  }

  /**
   * Creates the checkboxes for the filter bar
   */
  void createCheckBoxes() {
    //TODO: Add a "select all" checkbox for each category, and a select all for all categories
    //TODO: Fix alignment of everything

    var genderText = new Label("Gender of Audience:");
    genderText.getStyleClass().add("list-cell-text");
    male = new CheckBox("Male");
    male.getStyleClass().add("checkbox");
    female = new CheckBox("Female");
    female.getStyleClass().add("checkbox");

    var ageText = new Label("Age of Audience:");
    ageText.getStyleClass().add("list-cell-text");
    CheckBox under25 = new CheckBox("Under 25");
    under25.getStyleClass().add("checkbox");
    CheckBox under34 = new CheckBox("25 to 34");
    under34.getStyleClass().add("checkbox");
    CheckBox under44 = new CheckBox("35 to 44");
    under44.getStyleClass().add("checkbox");
    CheckBox under54 = new CheckBox("45 to 54");
    under54.getStyleClass().add("checkbox");
    CheckBox over54 = new CheckBox("Over 54");
    over54.getStyleClass().add("checkbox");

    var incomeText = new Label("Income of Audience:");
    incomeText.getStyleClass().add("list-cell-text");
    CheckBox lowIncome = new CheckBox("Low Income");
    lowIncome.getStyleClass().add("checkbox");
    CheckBox mediumIncome = new CheckBox("Medium Income");
    mediumIncome.getStyleClass().add("checkbox");
    CheckBox highIncome = new CheckBox("High Income");
    highIncome.getStyleClass().add("checkbox");

    var contextText = new Label("Location of Ad Interaction:");
    contextText.getStyleClass().add("list-cell-text");
    CheckBox blog = new CheckBox("Blog Site");
    blog.getStyleClass().add("checkbox");
    CheckBox news = new CheckBox("News Site");
    news.getStyleClass().add("checkbox");
    CheckBox shopping = new CheckBox("Shopping Site");
    shopping.getStyleClass().add("checkbox");
    CheckBox socialMedia = new CheckBox("Social Media");
    socialMedia.getStyleClass().add("checkbox");

    compareList = new ListView();

    compareList.getStyleClass().add("list-cell");
//    compareList.setMouseTransparent( true );
//    compareList.setFocusTraversable( false );
    compareList.getItems()
        .addAll(genderText, male, female, "", ageText, under25, under34, under44, under54, over54,
            "", incomeText, lowIncome, mediumIncome, highIncome, "", contextText, blog, news,
            shopping, socialMedia);
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

  public Button getFilterButton() {
    return filterButton;
  }

  public JFreeChart getChart() {
    return chart;
  }

  public ListView getCompareList() {
    return compareList;
  }

  public CheckBox getMaleBox() {
    return male;
  }
  public CheckBox getFemaleBox(){
    return female;
  }


}
