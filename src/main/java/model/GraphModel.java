package model;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;

import java.time.ZoneId;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;

public class GraphModel {

  private final int id;
  private final boolean divide;
  private final HashMap<Integer, GraphLine> lines;
  private final TimeSeriesCollection dataSet;
  public String timeFilterVal;
  private final JFreeChart chart;
  private final Model model;
  private Map<String, FilterPredicate> predicates;
  private Map<String, Boolean> currentlySelected;
  private LocalDate currentStart;
  private LocalDate currentEnd;

  /**
   * @param model take in the model class
   * @param title title of the graph
   * @param xAxisName the X axis of the graph
   * @param yAxisName the Y axis of the graph
   * @param id the ID of the graph
   * @param needDivisionForChangingTime does the data need to have any division needed to show data correctly
   */
  public GraphModel(Model model, String title, String xAxisName, String yAxisName, int id, boolean needDivisionForChangingTime) {
    this.id = id;
    this.model = model;
    this.predicates = initPredicates();
    this.lines = new HashMap<>(1);
    this.dataSet = new TimeSeriesCollection();
    this.timeFilterVal = "Day";
    this.divide = needDivisionForChangingTime;
    this.chart = ChartFactory.createTimeSeriesChart(title, xAxisName, yAxisName, this.dataSet, true,true, false);
    this.newLine(title, null);
  }

  public void updateGraphData(HashMap<String, Boolean> selected) {
    this.updateGraphData(this.timeFilterVal, selected);
  }
  /**
   * Sets the data of the graph via time period
   *
   * @param timeChosen The time period to filter by
   */
  public void updateGraphData(String timeChosen, HashMap<String, Boolean> selected) {
    if (selected != null) currentlySelected = selected;
    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
    this.timeFilterVal = timeChosen;

    lines.forEach((idx, line) -> {
      Map<String, FilterPredicate> adjustedPredicates = this.updateSegmentFilters(predicates, currentlySelected);
      String linePredicate = line.getBasePredicate();
      if (linePredicate != null) {
        FilterPredicate predicate = predicates.get(linePredicate);
        adjustedPredicates.remove(predicate.group() + "_all");
        adjustedPredicates.putIfAbsent(linePredicate, predicate);
      }
      Map<LocalDateTime, Double> lineData = model.loadData(id, this.combinePredicates(adjustedPredicates));
      line.updateLine(timeChosen, lineData);
      renderer.setSeriesShapesVisible(line.getId(), !timeChosen.equals("Hour"));
    });

    updateDateFilters(currentStart, currentEnd, true);
    updateDateFilters(currentStart, currentEnd, false);
  }

  public void newLine(String title, boolean divide, String predicateCode) {
    GraphLine line = new GraphLine(this.lines.size(), title, divide, predicateCode);
    dataSet.addSeries(line.getDataSeries());
    lines.put(this.lines.size(), line);
    updateGraphData(null);
  }

  public void newLine(String title, String predicateCode) {
    this.newLine(title, this.divide, predicateCode);
  }

  /**
   * Getter for the Chart
   * @return The Graphs chart
   */
  public JFreeChart getChart() {
    return chart;
  }

  /**
   * The initial configuration for the datePickers.
   * Includes setting their initial values, the update handlers for each cell and the min/max values for each picker.
   * @param startDatePicker Date picker for the start date
   * @param endDatePicker Date picker for the end date
   * @param dateFilterButton Button responsible for starting date filtering
   */
  public void configureDatePickers(DatePicker startDatePicker, DatePicker endDatePicker, Button dateFilterButton) {
    TimeSeries dataSeries = lines.get(0).getDataSeries();
    Date startDate = dataSeries.getTimePeriod(0).getStart();
    LocalDate localStart = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    currentStart = localStart;
    startDatePicker.setValue(localStart);

    Date endDate = dataSeries.getTimePeriod(dataSeries.getItemCount() - 1).getEnd();
    LocalDate localEnd = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    currentEnd = localEnd;
    endDatePicker.setValue(localEnd);

    // set the maximum date of the first date picker to the selected date on the second date picker
    startDatePicker.setDayCellFactory(param -> new DateCell() {
      @Override
      public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        dateFilterButton.setDisable(false);
        boolean end = endDatePicker.getValue() != null ? item.isAfter(endDatePicker.getValue().minusDays(1)) : item.isAfter(localEnd.minusDays(1));
        setDisable(end || item.isBefore(localStart));
      }
    });


    // set the minimum date of the second date picker to the selected date on the first date picker
    endDatePicker.setDayCellFactory(param -> new DateCell() {
      @Override
      public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        dateFilterButton.setDisable(false);
        boolean start = startDatePicker.getValue() != null ? item.isBefore(startDatePicker.getValue().plusDays(1)) : item.isBefore(localStart.plusDays(1));
        setDisable(start || item.isAfter(localEnd));
      }
    });
  }

  /**
   * Convert LocalDate to Date
   * @param date: LocalDate object to be converted
   * @return Returns the new Date object
   */
  private Date convertDate(LocalDate date) {
    return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
  }

  /**
   * Filter the current TimeSeries between two dates
   * @param startDate: lower bound of the dates to filter by
   * @param endDate: upper bound of the dates to filter by
   */
  public void updateDateFilterss(LocalDate startDate, LocalDate endDate) {
    if (startDate != null && endDate != null) {
      dataSet.removeAllSeries();
      lines.forEach((idx, line) -> {
        TimeSeries dataSeries = line.getDataSeries();
        // Create start and end Date objects with selected times
        TimeSeries filteredSeries = new TimeSeries(dataSeries.getKey());
        if (!this.timeFilterVal.equals("Week")) {
          Date startTimeDate = convertDate(startDate);
          Date endTimeDate = convertDate(endDate);
          for (int i = 0; i < dataSeries.getItemCount(); i++) {
            var time = dataSeries.getTimePeriod(i);
            Date date = time.getStart();
            if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
              filteredSeries.add(dataSeries.getDataItem(i));
            }
          }
        } else {
          Week startWeek = new Week(java.sql.Date.valueOf(startDate));
          Week endWeek = new Week(java.sql.Date.valueOf(endDate));
          for (int i = 0; i < dataSeries.getItemCount(); i++) {
            RegularTimePeriod compWeek = dataSeries.getDataItem(i).getPeriod();
            if (compWeek.compareTo(startWeek) >= 0 && compWeek.compareTo(endWeek) <= 0) {
              filteredSeries.add(dataSeries.getDataItem(i));
            }
          }
        }

        line.setFilteredSeries(filteredSeries);
        dataSet.addSeries(filteredSeries);
      });
      XYPlot xyPlot = (XYPlot) chart.getPlot();
      xyPlot.getDomainAxis().setAutoRange(true);
      xyPlot.getRangeAxis().setAutoRange(true);
      currentStart = startDate;
      currentEnd = endDate;
    }
  }

    /**
     * Filter the current TimeSeries between two dates
     * @param startDate: lower bound of the dates to filter by
     * @param endDate: upper bound of the dates to filter by
     */
    public void updateDateFilters(LocalDate startDate, LocalDate endDate, boolean mainGraphLine) {
        if (startDate != null && endDate != null) {
            dataSet.removeAllSeries();
            if(mainGraphLine)
            {
                TimeSeries dataSeries = lines.get(0).getDataSeries();
                TimeSeries filteredSeries = new TimeSeries(dataSeries.getKey());
                if (!this.timeFilterVal.equals("Week")) {
                    Date startTimeDate = convertDate(startDate);
                    Date endTimeDate = convertDate(endDate);

                    var time = dataSeries.getTimePeriod(0);
                    Date date = time.getStart();
                    if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
                        filteredSeries.add(dataSeries.getDataItem(0));
                    }

                } else {
                    Week startWeek = new Week(java.sql.Date.valueOf(startDate));
                    Week endWeek = new Week(java.sql.Date.valueOf(endDate));

                    RegularTimePeriod compWeek = dataSeries.getDataItem(0).getPeriod();
                    if (compWeek.compareTo(startWeek) >= 0 && compWeek.compareTo(endWeek) <= 0) {
                        filteredSeries.add(dataSeries.getDataItem(0));
                    }

                }
                lines.get(0).setFilteredSeries(filteredSeries);
                dataSet.addSeries(filteredSeries);

            } else if(!mainGraphLine)
            {
                TimeSeries dataSeries = lines.get(1).getDataSeries();
                TimeSeries filteredSeries = new TimeSeries(dataSeries.getKey());
                if (!this.timeFilterVal.equals("Week")) {
                    Date startTimeDate = convertDate(startDate);
                    Date endTimeDate = convertDate(endDate);

                    var time = dataSeries.getTimePeriod(1);
                    Date date = time.getStart();
                    if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
                        filteredSeries.add(dataSeries.getDataItem(1));
                    }

                } else {
                    Week startWeek = new Week(java.sql.Date.valueOf(startDate));
                    Week endWeek = new Week(java.sql.Date.valueOf(endDate));

                    RegularTimePeriod compWeek = dataSeries.getDataItem(1).getPeriod();
                    if (compWeek.compareTo(startWeek) >= 0 && compWeek.compareTo(endWeek) <= 0) {
                        filteredSeries.add(dataSeries.getDataItem(1));
                    }

                }
                lines.get(1).setFilteredSeries(filteredSeries);
                dataSet.addSeries(filteredSeries);
            }

            XYPlot xyPlot = (XYPlot) chart.getPlot();
            xyPlot.getDomainAxis().setAutoRange(true);
            xyPlot.getRangeAxis().setAutoRange(true);
            currentStart = startDate;
            currentEnd = endDate;
        }
    }

  /**
   * Initialise the predicates used for filtering by audience segment.
   */
  public Map<String, FilterPredicate> initPredicates() {
    currentlySelected = new HashMap<>();
    Map<String, FilterPredicate> allPredicates = new HashMap<>(19);
    allPredicates.put("age_all", new FilterPredicate("age", u -> true));
    for (Age a : Age.values()) {
      Predicate<User> p = u -> u.getAge() == a;
      allPredicates.put("age_" + a.idx, new FilterPredicate("age", p));
    }

    allPredicates.put("context_all", new FilterPredicate( "context", u -> true));
    for (Context c : Context.values()) {
      Predicate<User> p = u -> u.getContext() == c;
      allPredicates.put("context_" + c.idx, new FilterPredicate("context", p));
    }

    allPredicates.put("income_all", new FilterPredicate( "income", u -> true));
    for (Income i : Income.values()) {
      Predicate<User> p = u -> u.getIncome() == i;
      allPredicates.put("income_" + i.idx, new FilterPredicate("income", p));
    }

    allPredicates.put("male_1", new FilterPredicate("gender", User::getGender));
    allPredicates.put("female_1", new FilterPredicate("gender", u -> !u.getGender()));
    return allPredicates;
  }

  /**
   * Update the enabled predicates for filtering by audience segment
   *
   * @param selected A Map of predicates are either to be applied or not
   */
  public Map<String, FilterPredicate> updateSegmentFilters(Map<String, FilterPredicate> predicates, Map<String, Boolean> selected) {
    return this.updateSegmentFilters(predicates, selected, true);
  }
  public Map<String, FilterPredicate> updateSegmentFilters(Map<String, FilterPredicate> predicates, Map<String, Boolean> selected, Boolean resetBaseFilters) {
    HashMap<String, FilterPredicate> predicateMap = new HashMap<>();
    if (resetBaseFilters != null && resetBaseFilters) {
      predicateMap.put("age_all", predicates.get("age_all"));
      predicateMap.put("context_all", predicates.get("context_all"));
      predicateMap.put("income_all", predicates.get("income_all"));
      predicateMap.remove("male_1");
      predicateMap.remove("female_1");
    }
    selected.forEach((key, value) -> {
      FilterPredicate fp = predicates.get(key);
      if (value) {
        predicateMap.put(key, fp);
        if (fp.group().equals("age")) predicateMap.remove("age_all");
        if (fp.group().equals("context")) predicateMap.remove("context_all");
        if (fp.group().equals("income")) predicateMap.remove("income_all");
      }
    });
    return predicateMap;
  }

  public void resetFilters(Map<String, FilterPredicate> predicates) {
    currentlySelected = new HashMap<>();
    currentlySelected.put("age_all", true);
    currentlySelected.put("context_all", true);
    currentlySelected.put("income_all", true);
  }

  /**
   * Combine the lists of predicates for filtering by audience segments
   * @return The combined list of predicates - "and"-ed together
   */
  public Predicate<User> combinePredicates(Map<String, FilterPredicate> predicates) {
    var ages = getPredicateGroup("age", predicates).map(FilterPredicate::predicate).reduce(u -> false, Predicate::or);
    var contexts = getPredicateGroup("context", predicates).map(FilterPredicate::predicate).reduce(u -> false, Predicate::or);
    var incomes = getPredicateGroup("income", predicates).map(FilterPredicate::predicate).reduce(u -> false, Predicate::or);
    Predicate<User> gender = u -> true;
    FilterPredicate male = predicates.get("male_1");
    FilterPredicate female = predicates.get("female_1");
    if (male != null) {
      gender = male.predicate();
    } else if (female != null) {
      gender = female.predicate();
    }
    return ages.and(contexts).and(incomes).and(gender);
  }

  private Stream<FilterPredicate> getPredicateGroup(String group, Map<String, FilterPredicate> predicates) {
    return predicates.values().stream().filter(fp -> fp.group().equals(group));
  }

  public int getId() {
    return id;
  }

  public Map<String, FilterPredicate> getPredicates() {
    return this.predicates;
  }

  public void setPredicates(Map<String, FilterPredicate> predicates) {
    this.predicates = predicates;
  }

  public void removeLine(int i) {
    GraphLine line = this.lines.get(i);
    if (line != null) {
      this.dataSet.removeSeries(line.getDataSeries());
      lines.remove(i);
    }
  }

  public HashMap<Integer, GraphLine> getLines() {
    return lines;
  }
}
