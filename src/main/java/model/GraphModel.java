package model;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
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

  public GraphModel(Model model, String title, String xAxisName, String yAxisName, int id, boolean needDivisionForChangingTime) {
    this.id = id;
    this.model = model;
    initPredicates();
    this.data = model.loadData(id, null);
    this.dataSeries = new TimeSeries(title);
    this.dataSet = new TimeSeriesCollection();
    this.dataSet.addSeries(this.dataSeries);
    this.timeFilterVal = "Month";
    this.needDivisionForChangingTime = needDivisionForChangingTime;
    chart = ChartFactory.createTimeSeriesChart(title, xAxisName, yAxisName, this.dataSet, true,
        true, false);
    updateGraphGranularity("Day", this.data);
  }

  public LocalDateTime getStartDate() {
    Map.Entry<LocalDateTime, Double> entry = data.entrySet().iterator().next();
    return entry.getKey();
  }

  /**
   * Sets the data of the graph via time period
   *
   * @param timeChosen The time period to filter by
   */
  public void updateGraphGranularity(String timeChosen, Map<LocalDateTime, Double> incomingData) {
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
  }
    public JFreeChart getChart() {
        return chart;
    }

    public void configureDatePickers(DatePicker startDatePicker, DatePicker endDatePicker, Button dateFilterButton) {
        // set the maximum date of the first date picker to the selected date on the second date picker
        startDatePicker.setDayCellFactory(param -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                dateFilterButton.setDisable(false);
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
                dateFilterButton.setDisable(false);
                if (startDatePicker.getValue() != null) {
                    setDisable(item.isBefore(startDatePicker.getValue()));
                }
            }
        });
    }

    public void updateDateFilters(LocalDate startDate, LocalDate endDate) {
      if (startDate != null && endDate != null) {
        // Create start and end Date objects with selected times
        Date startTimeDate = new GregorianCalendar(startDate.getYear(),startDate.getMonthValue() - 1, startDate.getDayOfMonth()).getTime();
        Date endTimeDate = new GregorianCalendar(endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth()).getTime();
        TimeSeries filteredSeries = new TimeSeries("Filtered Series");
        for (int i = 0; i < dataSeries.getItemCount(); i++) {
          var time = dataSeries.getTimePeriod(i);
          Date date = time.getStart();
          if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
            filteredSeries.add(dataSeries.getDataItem(i));
          }
        }

        dataSet.removeAllSeries();
        dataSet.addSeries(filteredSeries);
      }
    }

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
    this.updateGraphGranularity("Day", model.loadData(id, this.combinePredicates()));
  }
}
