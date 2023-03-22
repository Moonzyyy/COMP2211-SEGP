package model;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
import java.time.ZoneId;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.*;

public class GraphModel {

  private final int id;
  private TimeSeries dataSeries;
  private final TimeSeriesCollection dataSet;

  private Map<LocalDateTime, Double> data;

  public String timeFilterVal;

  private JFreeChart chart;

  private final boolean needDivisionForChangingTime;
  private final Model model;

  private Map<Integer, FilterPredicate> agePredicates;
  private Map<Integer, FilterPredicate> contextPredicates;
  private Map<Integer, FilterPredicate> incomePredicates;
  private FilterPredicate malePredicate;
  private FilterPredicate femalePredicate;

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
    initPredicates();
    this.data = model.loadData(id, null);
    this.dataSeries = new TimeSeries(title);
    this.dataSet = new TimeSeriesCollection();
    this.dataSet.addSeries(this.dataSeries);
    this.needDivisionForChangingTime = needDivisionForChangingTime;
    chart = ChartFactory.createTimeSeriesChart(title, xAxisName, yAxisName, this.dataSet, true,
            true, false);
    updateGraphData("Day", this.data);
  }

  /**
   * Sets the data of the graph via time period
   *
   * @param timeChosen The time period to filter by
   */
  public void updateGraphData(String timeChosen, Map<LocalDateTime, Double> incomingData) {
    var data = incomingData != null ? incomingData : this.data;
    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) chart.getXYPlot().getRenderer();
    renderer.setSeriesShapesVisible(0, !timeChosen.equals("Hour"));
    dataSeries.clear();
    this.timeFilterVal = timeChosen;

    Function<LocalDateTime, RegularTimePeriod> fun;
    switch (timeChosen) {
      case "Hour" -> fun = d -> new Hour(d.getHour(), d.getDayOfMonth(), d.getMonthValue(), d.getYear());
      case "Day" -> fun = d -> new Day(d.getDayOfMonth(), d.getMonthValue(), d.getYear());
      case "Week" ->
        fun = d -> {
          Date date1 = Date.from(d.toInstant(ZoneOffset.UTC));
          return new Week(date1);
        };
      default -> fun = d -> new Month(d.getMonthValue(), d.getYear());
    }

    for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
      LocalDateTime date = entry.getKey();
      RegularTimePeriod rtp = fun.apply(date);
      if (dataSeries.getDataItem(rtp) == null) {
        dataSeries.add(rtp, entry.getValue());
      } else {
        if (needDivisionForChangingTime) {
          dataSeries.update(rtp, (dataSeries.getValue(rtp).doubleValue() + entry.getValue()) / 2.0);
        } else {
          dataSeries.update(rtp, dataSeries.getValue(rtp).doubleValue() + entry.getValue());
        }

      }
    }
      updateDateFilters(currentStart, currentEnd);

    this.data = data;
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

      dataSet.removeAllSeries();
      dataSet.addSeries(filteredSeries);
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
  public void initPredicates() {
    agePredicates = new HashMap<>();
    contextPredicates = new HashMap<>();
    incomePredicates = new HashMap<>();
    agePredicates.put(0, new FilterPredicate(u -> true, true));
    for (Age a : Age.values()) {
      Predicate<User> p = u -> u.getAge() == a;
      agePredicates.put(a.idx, new FilterPredicate(p));
    }

    contextPredicates.put(0, new FilterPredicate(u -> true, true));
    for (Context c : Context.values()) {
      Predicate<User> p = u -> u.getContext() == c;
      contextPredicates.put(c.idx, new FilterPredicate(p));
    }

    incomePredicates.put(0, new FilterPredicate(u -> true, true));
    for (Income i : Income.values()) {
      Predicate<User> p = u -> u.getIncome() == i;
      incomePredicates.put(i.idx, new FilterPredicate(p));
    }
    malePredicate = new FilterPredicate(User::getGender);
    femalePredicate = new FilterPredicate(u -> !u.getGender());
  }

  /**
   * Update the enabled predicates for filtering by audience segment
   * @param checkboxes: A list of checkboxes from view.Graph
   */
  public void updateFilters(ArrayList<CheckBox> checkboxes) {
    Pattern p = Pattern.compile("(.*)_(.*)");
    agePredicates.get(0).setEnabled(true);
    contextPredicates.get(0).setEnabled(true);
    incomePredicates.get(0).setEnabled(true);
    for (CheckBox checkBox : checkboxes) {
      Matcher m = p.matcher(checkBox.getId());
      if (m.find()) {
        switch (m.group(1)) {
          case "age" -> {
            if (checkBox.isSelected()) {
              agePredicates.get(0).setEnabled(false);
            }
            agePredicates.get(Integer.parseInt(m.group(2))).setEnabled(checkBox.isSelected());
          }
          case "context" -> {
            if (checkBox.isSelected()) {
              contextPredicates.get(0).setEnabled(false);
            }
            contextPredicates.get(Integer.parseInt(m.group(2))).setEnabled(checkBox.isSelected());
          }
          case "income" -> {
            if (checkBox.isSelected()) {
              incomePredicates.get(0).setEnabled(false);
            }
            incomePredicates.get(Integer.parseInt(m.group(2))).setEnabled(checkBox.isSelected());
          }
          case "male" -> malePredicate.setEnabled(checkBox.isSelected());
          default -> femalePredicate.setEnabled(checkBox.isSelected());
        }
      }
    }
    this.updateGraphData(this.timeFilterVal, model.loadData(id, this.combinePredicates()));
  }

  /**
   * Combine the lists of predicates for filtering by audience segments
   * @return The combined list of predicates - "and"-ed together
   */
  public Predicate<User> combinePredicates() {
    var ages = agePredicates.values().stream().filter(FilterPredicate::isEnabled).map(FilterPredicate::getPredicate).reduce(u -> false, Predicate::or);
    var contexts = contextPredicates.values().stream().filter(FilterPredicate::isEnabled).map(FilterPredicate::getPredicate).reduce(u -> false, Predicate::or);
    var incomes = incomePredicates.values().stream().filter(FilterPredicate::isEnabled).map(FilterPredicate::getPredicate).reduce(u -> false, Predicate::or);
    Predicate<User> gender = u -> true;
    if (malePredicate.isEnabled()) {
      gender = malePredicate.getPredicate();
    } else if (femalePredicate.isEnabled()) {
      gender = femalePredicate.getPredicate();
    }
    return ages.and(contexts).and(incomes).and(gender);
  }

  public Map<LocalDateTime, Double> getData() {
    return data;
  }

}
