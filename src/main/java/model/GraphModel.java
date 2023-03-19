package model;

import javafx.scene.chart.Chart;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Week;

public class GraphModel {

  public TimeSeries dataSeries;
  public TimeSeriesCollection dataSet;

  Map<LocalDateTime, Double> data;

  public String timeFilterVal;

  public JFreeChart chart;

  private boolean needDivisionForChangingTime = false;

  public GraphModel(String title, String xAxisName, String yAxisName,
      Map<LocalDateTime, Double> data, boolean needDivisionForChangingTime) {
    this.dataSeries = new TimeSeries(title);
    this.dataSet = new TimeSeriesCollection();
    this.dataSet.addSeries(this.dataSeries);
    this.data = data;
    this.timeFilterVal = "Month";
    this.needDivisionForChangingTime = needDivisionForChangingTime;
    chart = ChartFactory.createTimeSeriesChart(title, xAxisName, yAxisName, this.dataSet, true,
        true, false);
    dataSetter("Day");
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
  public void dataSetter(String timeChosen) {
    this.dataSeries.clear();
    if (!timeFilterVal.equals(timeChosen)) {
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
  }
    public JFreeChart getChart() {
        return chart;
    }

    public void configureDatePickers(DatePicker startDatePicker, DatePicker endDatePicker) {
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
}
