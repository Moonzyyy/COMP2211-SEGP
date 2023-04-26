package model;

import java.time.ZoneId;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
  private LocalDate start1;
  private LocalDate end1;

  private LocalDate start2;
  private LocalDate end2;

  private final String title;

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
    this.predicates = model.initPredicates();
    this.lines = new HashMap<>(1);
    this.dataSet = new TimeSeriesCollection();
    this.timeFilterVal = "Day";
    this.divide = needDivisionForChangingTime;
    this.chart = ChartFactory.createTimeSeriesChart(title, xAxisName, yAxisName, this.dataSet, true,true, false);
    this.newLine(title, null);
    this.newLine("Comparison", null, false);
    this.title = title;
    updateGraphData(new HashMap<>());
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
        start1 = localStart;
        start2 = localStart;
        startDatePicker.setValue(localStart);

        Date endDate = dataSeries.getTimePeriod(dataSeries.getItemCount() - 1).getEnd();
        LocalDate localEnd = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        end1 = localEnd;
        end2 = localEnd;
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
      if (line.isEnabled()) {
        Map<String, FilterPredicate> adjustedPredicates = model.updateSegmentFilters(predicates, currentlySelected);
        String linePredicate = line.getBasePredicate();
        if (linePredicate != null) {
          FilterPredicate predicate = predicates.get(linePredicate);
          adjustedPredicates.remove(predicate.group() + "_all");
          adjustedPredicates.putIfAbsent(linePredicate, predicate);
        }
        Map<LocalDateTime, Double> lineData = model.loadData(id, model.combinePredicates(adjustedPredicates));
        line.updateLine(timeChosen, lineData);
        renderer.setSeriesShapesVisible(line.getId(), !timeChosen.equals("Hour"));
      }
    });

//    updateMainDateFilters(currentStart, currentEnd);
//    updateCompareDateFilters(currentStart, currentEnd);
    updateDateFilters(start1, end1);
    updateCompareDateFilters(start2, end2);
  }

  public void newLine(String title, boolean divide, String predicateCode, boolean enabled) {
    GraphLine line = new GraphLine(this.lines.size(), title, divide, predicateCode, enabled);
    lines.put(this.lines.size(), line);
  }

  public void newLine(String title, String predicateCode, boolean enabled) {
    this.newLine(title, this.divide, predicateCode, enabled);
  }

  public void newLine(String title, String predicateCode) {
    this.newLine(title, this.divide, predicateCode, true);
  }

  /**
   * Getter for the Chart
   * @return The Graphs chart
   */
  public JFreeChart getChart() {
    return chart;
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
      dataSet.removeAllSeries();
      lines.forEach((idx, line) -> {
        TimeSeries filteredSeries = line.getDatedSeries1();
        dataSet.addSeries(filteredSeries);
        if (line.isEnabled()) {
            // Create start and end Date objects with selected times
            filteredSeries.clear();
            this.applyDateFilter(line.getDataSeries(), filteredSeries, startDate, endDate);
            line.setDatedSeries1(filteredSeries);
        }
      });
      XYPlot xyPlot = (XYPlot) chart.getPlot();
      xyPlot.getDomainAxis().setAutoRange(true);
      xyPlot.getRangeAxis().setAutoRange(true);
      start1 = startDate;
      end1 = endDate;
      updateCompareDateFilters(start2, end2);
    }
  }

  /**
   * Filter the current TimeSeries between two dates
   * @param startDate: lower bound of the dates to filter by
   * @param endDate: upper bound of the dates to filter by
   */
  public boolean updateCompareDateFilters(LocalDate startDate, LocalDate endDate) {
    if (startDate != null && endDate != null) {
//      dataSet.removeAllSeries();
      lines.forEach((idx, line) -> {
        dataSet.removeSeries(line.getDatedSeries2());
        line.setDatedSeries2(new TimeSeries(line.getTitle() + ": " + startDate + " " + endDate));
        TimeSeries filteredSeries = line.getDatedSeries2();
        if (line.isEnabled() && !(startDate.equals(start1) && endDate.equals(end1))) {
          this.applyDateFilter(line.getDataSeries(), line.getDatedSeries2(), startDate, endDate);
          line.setDatedSeries2(filteredSeries);
          dataSet.addSeries(filteredSeries);
        }
      });
      XYPlot xyPlot = (XYPlot) chart.getPlot();
      xyPlot.getDomainAxis().setAutoRange(true);
      xyPlot.getRangeAxis().setAutoRange(true);
      start2 = startDate;
      end2 = endDate;
      return true;
    }
    return false;
  }

  private void applyDateFilter(TimeSeries originalSeries, TimeSeries filteredSeries, LocalDate start, LocalDate end) {
    if (!this.timeFilterVal.equals("Week")) {
      Date startTimeDate = convertDate(start);
      Date endTimeDate = convertDate(end);
      for (int i = 0; i < originalSeries.getItemCount(); i++) {
        var time = originalSeries.getTimePeriod(i);
        Date date = time.getStart();
        if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
          filteredSeries.add(originalSeries.getDataItem(i));
        }
      }
    } else {
      Week startWeek = new Week(java.sql.Date.valueOf(start));
      Week endWeek = new Week(java.sql.Date.valueOf(end));
      for (int i = 0; i < originalSeries.getItemCount(); i++) {
        RegularTimePeriod compWeek = originalSeries.getDataItem(i).getPeriod();
        if (compWeek.compareTo(startWeek) >= 0 && compWeek.compareTo(endWeek) <= 0) {
          filteredSeries.add(originalSeries.getDataItem(i));
        }
      }
    }
  }

    /**
     * Filter the current TimeSeries between two dates
     * @param startDate: lower bound of the dates to filter by
     * @param endDate: upper bound of the dates to filter by
     */
//    public void updateMainDateFilters(LocalDate startDate, LocalDate endDate) {
//        if (startDate != null && endDate != null) {
//
//
//            TimeSeries dataSeries = lines.get(0).getDataSeries();
//            TimeSeries filteredSeries = new TimeSeries(dataSeries.getKey());
//            if (!this.timeFilterVal.equals("Week")) {
//                Date startTimeDate = convertDate(startDate);
//                Date endTimeDate = convertDate(endDate);
//
//                for (int i = 0; i < dataSeries.getItemCount(); i++) {
//                    var time = dataSeries.getTimePeriod(i);
//                    Date date = time.getStart();
//                    if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
//                        filteredSeries.add(dataSeries.getDataItem(i));
//                    }
//                }
//
//            } else {
//                Week startWeek = new Week(java.sql.Date.valueOf(startDate));
//                Week endWeek = new Week(java.sql.Date.valueOf(endDate));
//
//                for (int i = 0; i < dataSeries.getItemCount(); i++) {
//                    RegularTimePeriod compWeek = dataSeries.getDataItem(i).getPeriod();
//                    if (compWeek.compareTo(startWeek) >= 0 && compWeek.compareTo(endWeek) <= 0) {
//                        filteredSeries.add(dataSeries.getDataItem(i));
//                    }
//                }
//
//            }
//            lines.get(0).setFilteredSeries(filteredSeries);
//            if (lines.size() > 1) {
//                var data = dataSet.getSeries(1);
//                dataSet.removeAllSeries();
//                dataSet.addSeries(filteredSeries);
//                dataSet.addSeries(data);
//            } else {
//                dataSet.removeAllSeries();
//                dataSet.addSeries(filteredSeries);
//            }
//
//
//            XYPlot xyPlot = (XYPlot) chart.getPlot();
//            xyPlot.getDomainAxis().setAutoRange(true);
//            xyPlot.getRangeAxis().setAutoRange(true);
//            currentStart = startDate;
//            currentEnd = endDate;
//        }
//    }

    /**
     * Filter the current TimeSeries between two dates
     * @param startDate: lower bound of the dates to filter by
     * @param endDate: upper bound of the dates to filter by
     */
//    public void updateCompareDateFilters(LocalDate startDate, LocalDate endDate) {
//        if (startDate != null && endDate != null && lines.size() > 1) {
//
//            TimeSeries dataSeries = lines.get(1).getDataSeries();
//            TimeSeries filteredSeries = new TimeSeries(dataSeries.getKey());
//            if (!this.timeFilterVal.equals("Week")) {
//                Date startTimeDate = convertDate(startDate);
//                Date endTimeDate = convertDate(endDate);
//
//                for (int i = 0; i < dataSeries.getItemCount(); i++) {
//                    var time = dataSeries.getTimePeriod(i);
//                    Date date = time.getStart();
//                    if (date.compareTo(startTimeDate) >= 0 && date.compareTo(endTimeDate) <= 0) {
//                        filteredSeries.add(dataSeries.getDataItem(i));
//                    }
//                }
//
//            } else {
//                Week startWeek = new Week(java.sql.Date.valueOf(startDate));
//                Week endWeek = new Week(java.sql.Date.valueOf(endDate));
//
//                for (int i = 0; i < dataSeries.getItemCount(); i++) {
//                    RegularTimePeriod compWeek = dataSeries.getDataItem(i).getPeriod();
//                    if (compWeek.compareTo(startWeek) >= 0 && compWeek.compareTo(endWeek) <= 0) {
//                        filteredSeries.add(dataSeries.getDataItem(i));
//                    }
//                }
//
//            }
//            lines.get(1).setFilteredSeries(filteredSeries);
//            var data = dataSet.getSeries(0);
//            dataSet.removeAllSeries();
//            dataSet.addSeries(data);
//            dataSet.addSeries(filteredSeries);
//
//        }
//
//        XYPlot xyPlot = (XYPlot) chart.getPlot();
//        xyPlot.getDomainAxis().setAutoRange(true);
//        xyPlot.getRangeAxis().setAutoRange(true);
//        start1 = startDate;
//        end1 = endDate;
//    }




  public int getId() {
    return id;
  }

  public Map<String, FilterPredicate> getPredicates() {
    return this.predicates;
  }

  public void setPredicates(Map<String, FilterPredicate> predicates) {
    this.predicates = predicates;
  }

  public void removeLine(TimeSeries series) {
    this.dataSet.removeSeries(series);
  }

  public HashMap<Integer, GraphLine> getLines() {
    return lines;
  }

  public String getTitle() {
    return title;
  }
}
