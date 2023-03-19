package model;

import javafx.scene.chart.Chart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GraphModel {

    public TimeSeries dataSeries;
    public TimeSeriesCollection dataSet;

    Map<LocalDateTime, Double> data;

    public String timeFilterVal;

    public JFreeChart chart;

    public GraphModel(String title, String xAxisName, String yAxisName, Map<LocalDateTime, Double> data) {
        this.dataSeries = new TimeSeries(title);
        this.dataSet = new TimeSeriesCollection();
        this.dataSet.addSeries(this.dataSeries);
        this.data = data;
        this.timeFilterVal = "Month";
        chart = ChartFactory.createTimeSeriesChart(title, xAxisName, yAxisName, this.dataSet, true, true,false);
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
                        this.dataSeries.addOrUpdate(hour, entry.getValue());
                    }
                }
                case "Day" -> {
                    for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
                        LocalDateTime date = entry.getKey();
                        Day day = new Day(date.getDayOfMonth(), date.getMonthValue(), date.getYear());
                        this.dataSeries.addOrUpdate(day, entry.getValue());
                    }
                }
                /// TODO: 3/17/2023 check if this actually does it by week
                case "Week" -> {
                    for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
                        LocalDateTime date = entry.getKey();
                        Date date1 = Date.from(date.toInstant(ZoneOffset.UTC));
                        Week week = new Week(date1);
                        this.dataSeries.addOrUpdate(week, entry.getValue());
                    }
                }
                default -> {
                    for (Map.Entry<LocalDateTime, Double> entry : data.entrySet()) {
                        LocalDateTime date = entry.getKey();
                        Month month = new Month(date.getMonthValue(), date.getYear());
                        System.out.println(month);
                        this.dataSeries.addOrUpdate(month, entry.getValue());
                    }
                }
            }
        }
    }

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

    public JFreeChart getChart() {
        return chart;
    }
}
