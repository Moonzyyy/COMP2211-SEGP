package view.scenes;

import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Objects;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.data.RangeType;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;

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
  private Button filterButton;
  private ListView<Node> compareList;
  private final ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>(14);
  private ComboBox<String> timeFilter;
  private ComboBox<String> compareControl;
  private final JFreeChart chart;
  private final LocalDateTime startDate;

//  public Graph(Integer id, String title, String xAxisName, String yAxisName,
  public Graph(Integer id, JFreeChart chart, LocalDateTime startDate) {
    super();
    layout = new BorderPane();
    metricId = id;
    this.chart = chart;
    this.startDate = startDate;
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

    // Creates the date pickers
    var startDatePicker = new DatePicker();
    var endDatePicker = new DatePicker();
    startDatePicker.getStyleClass().add("start-date-picker");
    endDatePicker.getStyleClass().add("end-date-picker");

    startDatePicker.setValue(LocalDate.from(startDate).minusDays(14));
    endDatePicker.setValue(LocalDate.from(startDate));

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
    timeFilter = new ComboBox<>();
    timeFilter.getItems().addAll("Hour", "Day", "Week", "Month");
    timeFilter.setValue("Day");
    timeFilter.getStyleClass().add("time-filter");

    compareControl = new ComboBox<>();
    compareControl.getItems().addAll("No Filter");
    compareControl.setValue("No Filter");
    compareControl.getStyleClass().add("time-filter");



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
//        dataSetter(dataSeries, timeFilter.getValue());
        TimeSeries filteredSeries = new TimeSeries("Filtered Series");
        for (int i = 0; i < dataSeries.getItemCount(); i++) {
          var time = dataSeries.getTimePeriod(i);
          Date date = time.getStart();
          if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
            filteredSeries.add(dataSeries.getDataItem(i));
          }
        }

        dataset.removeAllSeries();
        dataset.addSeries(filteredSeries);
      }
    });
    filterBar.getChildren().addAll(compareControl,timeFilter, startDatePicker, endDatePicker, filterButton);

    createCheckBoxes();

    layout.setBottom(filterBar);
    scene = new Scene(layout, 1280, 720);
    scene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/view/graph.css")).toExternalForm());

  }

  /**
   * Sets the data of the graph via time period
   *
   * @param dataSeries The series to add the data to
   * @param timeChosen The time period to filter by
   */
  void dataSetter(TimeSeries dataSeries, String timeChosen) {
    dataSeries.clear();
    if (timeChosen.equals("Hour")) {

      for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
        LocalDateTime date = entry.getKey();
        Hour hour = new Hour(date.getHour(), date.getDayOfMonth(), date.getMonthValue(),
            date.getYear());
        if(dataSeries.getDataItem(hour) == null)
        {
          dataSeries.add(hour, entry.getValue());
        } else
        {
          dataSeries.update(hour, dataSeries.getValue(hour).doubleValue() + entry.getValue());
        }
      }
    } else if (timeChosen.equals("Day")) {
      for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
        LocalDateTime date = entry.getKey();
        Day day = new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
        System.out.println(day + " " + entry.getValue());
        if(dataSeries.getDataItem(day) == null)
        {
          dataSeries.add(day, entry.getValue());
        } else
        {
          dataSeries.update(day, dataSeries.getValue(day).doubleValue() + entry.getValue());
        }
      }
      /// TODO: 3/17/2023 check if this actually does it by week 
    } else if (timeChosen.equals("Week")) {
      for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
        LocalDateTime date = entry.getKey();
        Date date1 = Date.from(date.toInstant(ZoneOffset.UTC));
        Week week = new Week(date1);
        if(dataSeries.getDataItem(week) == null)
        {
          dataSeries.add(week, entry.getValue());
        } else
        {
          dataSeries.update(week, dataSeries.getValue(week).doubleValue() + entry.getValue());
        }
      }
    } else {
      for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
        LocalDateTime date = entry.getKey();
        Month month = new Month(date.getMonthValue(), date.getYear());;
        if(dataSeries.getDataItem(month) == null)
        {
          dataSeries.add(month, entry.getValue());
        } else
        {
          dataSeries.update(month, dataSeries.getValue(month).doubleValue() + entry.getValue());
        }
      }
    }


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
    CheckBox male = new CheckBox("Male");
    male.getStyleClass().add("checkbox");
    CheckBox female = new CheckBox("Female");
    female.getStyleClass().add("checkbox");
    this.checkboxes.add(male);
    this.checkboxes.add(female);

    compareList.getStyleClass().add("list-cell");
    compareList.getItems().addAll(genderText, male, female);

    var ageText = new Label("Age of Audience:");
    ageText.getStyleClass().add("list-cell-text");
    compareList.getItems().add(ageText);
    for (Age a : Age.values()) {
      CheckBox box = new CheckBox(a.label);
      compareList.getItems().add(box);
      this.checkboxes.add(box);
    }

    var incomeText = new Label("Income of Audience:");
    incomeText.getStyleClass().add("list-cell-text");
    compareList.getItems().add(incomeText);
    for (Income i : Income.values()) {
      CheckBox box = new CheckBox(i.label);
      compareList.getItems().add(box);
      this.checkboxes.add(box);
    }

    var contextText = new Label("Location of Ad Interaction:");
    contextText.getStyleClass().add("list-cell-text");
    compareList.getItems().add(contextText);
    for (Context c : Context.values()) {
      CheckBox box = new CheckBox(c.label);
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
    var checkboxList = new ArrayList<CheckBox>();
    //From a ListView, get all the checkboxes
    compareList.getItems().forEach(item -> {
      if (item instanceof CheckBox) {
        checkboxList.add((CheckBox) item);
      }
    });
    return checkboxList;
  }

  public Button getFilterButton() {
    return filterButton;
  }

  public JFreeChart getChart() {
    return chart;
  }

  public ComboBox<String> getTimeFilter() {
    return timeFilter;
  }
}
