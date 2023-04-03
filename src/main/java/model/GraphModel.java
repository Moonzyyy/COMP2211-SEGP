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
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;

public class GraphModel {

  private final int id;
  private final boolean divide;
  private TimeSeries dataSeries;
  private HashMap<Integer, GraphLine> lines;
  private final TimeSeriesCollection dataSet;

  private Map<LocalDateTime, Double> data;

  public String timeFilterVal;

  private JFreeChart chart;

  private final Model model;
  private Map<String, ArrayList<FilterPredicate>> predicates;
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
    this.data = model.loadData(id, null);
    this.dataSeries = new TimeSeries(title);
    this.dataSet = new TimeSeriesCollection();
    this.timeFilterVal = "Day";
    this.divide = needDivisionForChangingTime;
    this.chart = ChartFactory.createTimeSeriesChart(title, xAxisName, yAxisName, this.dataSet, true,true, false);
    this.newLine(title, new HashMap<>(0));
  }

  public void updateGraphData() {
    this.updateGraphData(this.timeFilterVal);
  }
  /**
   * Sets the data of the graph via time period
   *
   * @param timeChosen The time period to filter by
   */
  public void updateGraphData(String timeChosen) {
    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
    this.timeFilterVal = timeChosen;
    dataSet.removeAllSeries();

    lines.forEach((idx, line) -> {
      Map<String, Boolean> linePredicates = line.getBasePredicates();
      Map<String, ArrayList<FilterPredicate>> adjustedPredicates = this.updateSegmentFilters(predicates, linePredicates, false);
      Map<LocalDateTime, Double> lineData = model.loadData(id, this.combinePredicates(adjustedPredicates));
      dataSet.addSeries(line.updateLine(timeChosen, lineData));
      renderer.setSeriesShapesVisible(line.getId(), !timeChosen.equals("Hour"));
    });
    updateDateFilters(currentStart, currentEnd);
  }

  public void newLine(String title, boolean divide, HashMap<String, Boolean> initialPredicates) {
    GraphLine line = new GraphLine(this.lines.size(), title, divide, initialPredicates);
    Map<String, ArrayList<FilterPredicate>> basePredicates = this.updateSegmentFilters(this.initPredicates(), initialPredicates);
    dataSet.addSeries(line.getDataSeries());
//    line.updateLine(this.timeFilterVal, data);
    lines.put(this.lines.size(), line);
    updateGraphData();
  }

  public void newLine(String title, HashMap<String, Boolean> initialPredicates) {
    this.newLine(title, this.divide, initialPredicates);
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
   */
  private LocalDate convertTimePeriod(RegularTimePeriod rtp) {
    return rtp.getStart().toInstant().atZone(ZoneOffset.UTC).toLocalDate();
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
  public void updateDateFilters(LocalDate startDate, LocalDate endDate) {
    if (startDate != null && endDate != null) {
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

        dataSet.removeSeries(dataSeries);
        line.setDataSeries(filteredSeries);
        dataSet.addSeries(line.getDataSeries());
        XYPlot xyPlot = (XYPlot) chart.getPlot();
        xyPlot.getDomainAxis().setAutoRange(true);
        xyPlot.getRangeAxis().setAutoRange(true);
        currentStart = startDate;
        currentEnd = endDate;
      });
    }
  }

  /**
   * Initialise the predicates used for filtering by audience segment.
   */
  public Map<String, ArrayList<FilterPredicate>> initPredicates() {
    Map<String, ArrayList<FilterPredicate>> allPredicates = new HashMap<>(4);
    ArrayList<FilterPredicate> agePredicates = new ArrayList<>(6);
    ArrayList<FilterPredicate> contextPredicates = new ArrayList<>(7);
    ArrayList<FilterPredicate> incomePredicates = new ArrayList<>(4);
    ArrayList<FilterPredicate> genderPredicates = new ArrayList<>(2);
    agePredicates.add(new FilterPredicate("AGE_ALL", u -> true, true));
    for (Age a : Age.values()) {
      Predicate<User> p = u -> u.getAge() == a;
      agePredicates.add(a.idx, new FilterPredicate(a.name(), p));
    }
    allPredicates.put("ages", agePredicates);

    contextPredicates.add(new FilterPredicate("CONTEXT_ALL", u -> true, true));
    for (Context c : Context.values()) {
      Predicate<User> p = u -> u.getContext() == c;
      contextPredicates.add(c.idx, new FilterPredicate(c.name(), p));
    }
    allPredicates.put("contexts", contextPredicates);

    incomePredicates.add(new FilterPredicate("INCOME_ALL", u -> true, true));
    for (Income i : Income.values()) {
      Predicate<User> p = u -> u.getIncome() == i;
      incomePredicates.add(i.idx, new FilterPredicate(i.name(), p));
    }
    allPredicates.put("incomes", incomePredicates);

    genderPredicates.add(new FilterPredicate("MALE", User::getGender));
    genderPredicates.add(new FilterPredicate("FEMALE", u -> !u.getGender()));
    allPredicates.put("genders", genderPredicates);
    return allPredicates;
  }

  /**
   * Update the enabled predicates for filtering by audience segment
   *
   * @param selected A Map of predicates are either to be applied or not
   */
  public Map<String, ArrayList<FilterPredicate>> updateSegmentFilters(Map<String, ArrayList<FilterPredicate>> predicates, Map<String, Boolean> selected) {
    return this.updateSegmentFilters(predicates, selected, true);
  }
  public Map<String, ArrayList<FilterPredicate>> updateSegmentFilters(Map<String, ArrayList<FilterPredicate>> predicates, Map<String, Boolean> selected, Boolean resetBaseFilters) {
    Pattern p = Pattern.compile("(.*)_(.*)");
    ArrayList<FilterPredicate> agePredicates = predicates.get("ages");
    ArrayList<FilterPredicate> contextPredicates = predicates.get("contexts");
    ArrayList<FilterPredicate> incomePredicates = predicates.get("incomes");
    FilterPredicate malePredicate = predicates.get("genders").get(0);
    FilterPredicate femalePredicate = predicates.get("genders").get(1);
    if (resetBaseFilters != null && resetBaseFilters) {
      agePredicates.get(0).setEnabled(true);
      contextPredicates.get(0).setEnabled(true);
      incomePredicates.get(0).setEnabled(true);
    }
    selected.forEach((key, value) -> {
      Matcher m = p.matcher(key);
      if (m.find()) {
        switch (m.group(1)) {
          case "age" -> {
            if (value) {
              agePredicates.get(0).setEnabled(false);
            }
            agePredicates.get(Integer.parseInt(m.group(2))).setEnabled(value);
          }
          case "context" -> {
            if (value) {
              contextPredicates.get(0).setEnabled(false);
            }
            contextPredicates.get(Integer.parseInt(m.group(2))).setEnabled(value);
          }
          case "income" -> {
            if (value) {
              incomePredicates.get(0).setEnabled(false);
            }
            incomePredicates.get(Integer.parseInt(m.group(2))).setEnabled(value);
          }
          case "male" -> malePredicate.setEnabled(value);
          default -> femalePredicate.setEnabled(value);
        }
      }
    });
    return predicates;
  }

  public Map<String, ArrayList<FilterPredicate>> resetFilters() {
    return this.resetFilters(this.predicates);
  }
  public Map<String, ArrayList<FilterPredicate>> resetFilters(Map<String, ArrayList<FilterPredicate>> predicates) {
    predicates.values().forEach(list -> list.forEach(fp -> fp.setEnabled(false)));
    predicates.get("ages").get(0).setEnabled(true);
    predicates.get("contexts").get(0).setEnabled(true);
    predicates.get("incomes").get(0).setEnabled(true);
    return predicates;
  }


  /**
   * Combine the lists of predicates for filtering by audience segments
   * @return The combined list of predicates - "and"-ed together
   */
  public Predicate<User> combinePredicates(Map<String, ArrayList<FilterPredicate>> predicates) {
    var ages = predicates.get("ages").stream().filter(FilterPredicate::isEnabled).map(FilterPredicate::getPredicate).reduce(u -> false, Predicate::or);
    var contexts = predicates.get("contexts").stream().filter(FilterPredicate::isEnabled).map(FilterPredicate::getPredicate).reduce(u -> false, Predicate::or);
    var incomes = predicates.get("incomes").stream().filter(FilterPredicate::isEnabled).map(FilterPredicate::getPredicate).reduce(u -> false, Predicate::or);
    Predicate<User> gender = u -> true;
    FilterPredicate male = predicates.get("genders").get(0);
    FilterPredicate female = predicates.get("genders").get(1);
    if (male.isEnabled()) {
      gender = male.getPredicate();
    } else if (female.isEnabled()) {
      gender = female.getPredicate();
    }
    return ages.and(contexts).and(incomes).and(gender);
  }

  public Map<LocalDateTime, Double> getData() {
    return data;
  }

  public int getId() {
    return id;
  }

  public Map<String, ArrayList<FilterPredicate>> getPredicates() {
    return this.predicates;
  }

  public void setPredicates(Map<String, ArrayList<FilterPredicate>> predicates) {
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
