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
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;

public class GraphModel {

  private final int id;
  private final TimeSeries dataSeries;
  private final TimeSeriesCollection dataSet;
  private final Map<LocalDateTime, Double> data;

  public String timeFilterVal;

  private final JFreeChart chart;

  private boolean needDivisionForChangingTime = false;
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
    this.dataSeries.clear();
    this.timeFilterVal = timeChosen;
    switch (timeChosen) {
      case "Hour" -> {
        for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
          LocalDateTime date = entry.getKey();
          Hour hour = new Hour(date.getHour(), date.getDayOfMonth(), date.getMonthValue(),
                  date.getYear());
          if (dataSeries.getDataItem(hour) == null) {
            dataSeries.add(hour, entry.getValue());
          } if(needDivisionForChangingTime)
          {
            dataSeries.update(hour, (dataSeries.getValue(hour).doubleValue() + entry.getValue()) / 2.0);
          } else
          {
            dataSeries.update(hour, dataSeries.getValue(hour).doubleValue() + entry.getValue());
          }
        }
      }
      case "Day" -> {
        for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
          LocalDateTime date = entry.getKey();
          Day day = new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
          if (dataSeries.getDataItem(day) == null) {
            dataSeries.add(day, entry.getValue());
          } else {
            if(needDivisionForChangingTime)
            {
              dataSeries.update(day, (dataSeries.getValue(day).doubleValue() + entry.getValue()) / 2.0);
            } else
            {
              dataSeries.update(day, dataSeries.getValue(day).doubleValue() + entry.getValue());
            }

          }
        }
      }
      /// TODO: 3/17/2023 check if this actually does it by week
      case "Week" -> {
        for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
          LocalDateTime date = entry.getKey();
          Date date1 = Date.from(date.toInstant(ZoneOffset.UTC));
          Week week = new Week(date1);
          if (dataSeries.getDataItem(week) == null) {
            dataSeries.add(week, entry.getValue());
          } else {
            if(needDivisionForChangingTime)
            {
              dataSeries.update(week, (dataSeries.getValue(week).doubleValue() + entry.getValue()) / 2.0);
            } else
            {
              dataSeries.update(week, dataSeries.getValue(week).doubleValue() + entry.getValue());
            }

          }
        }
      }
      default -> {
        for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
          LocalDateTime date = entry.getKey();
          Month month = new Month(date.getMonthValue(), date.getYear());
          if (dataSeries.getDataItem(month) == null) {
            dataSeries.add(month, entry.getValue());
          } else {
            if(needDivisionForChangingTime)
            {
              dataSeries.update(month, (dataSeries.getValue(month).doubleValue() + entry.getValue()) / 2.0);
            } else
            {
              dataSeries.update(month, dataSeries.getValue(month).doubleValue() + entry.getValue());
            }

          }
        }
      }
    }
    updateDateFilters(currentStart, currentEnd);
  }

  /**
   * Getter for the Chart
   * @return The Graphs chart
   */
  public JFreeChart getChart() {
    return chart;
  }

  /**
   * The initial configuration for the datepickers.
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
        boolean end = endDatePicker.getValue() != null ? item.isAfter(endDatePicker.getValue()) : item.isAfter(localEnd);
        setDisable(end || item.isBefore(localStart));
      }
    });


    // set the minimum date of the second date picker to the selected date on the first date picker
    endDatePicker.setDayCellFactory(param -> new DateCell() {
      @Override
      public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        dateFilterButton.setDisable(false);
        boolean start = startDatePicker.getValue() != null ? item.isBefore(startDatePicker.getValue()) : item.isBefore(localStart);
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
    return new GregorianCalendar(date.getYear(),date.getMonthValue() - 1, date.getDayOfMonth()).getTime();
  }

  /**
   * Filter the current TimeSeries between two dates
   * @param startDate: lower bound of the dates to filter by
   * @param endDate: upper bound of the dates to filter by
   */
  public void updateDateFilters(LocalDate startDate, LocalDate endDate) {
    if (startDate != null && endDate != null) {
      // Create start and end Date objects with selected times
      Date startTimeDate = convertDate(startDate);
      Date endTimeDate = convertDate(endDate);
      TimeSeries filteredSeries = new TimeSeries(dataSeries.getKey());
      for (int i = 0; i < dataSeries.getItemCount(); i++) {
        var time = dataSeries.getTimePeriod(i);
        Date date = time.getStart();
        if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
          filteredSeries.add(dataSeries.getDataItem(i));
        }
      }

      dataSet.removeAllSeries();
      dataSet.addSeries(filteredSeries);
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
    this.updateGraphData("Day", model.loadData(id, this.combinePredicates()));
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
}
