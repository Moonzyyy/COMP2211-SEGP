package model;

import org.jfree.data.time.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class GraphLine {
    private final String basePredicate;
    private final int id;
    private TimeSeries dataSeries;
    private TimeSeries filteredSeries;
    private final boolean divide;
    private final String title;


    GraphLine(int id, String title, boolean needDivisionForChangingTime, String initialP) {
        this.id = id;
        this.title = title;
        this.dataSeries = new TimeSeries(title);
        this.filteredSeries = new TimeSeries(title);
        this.basePredicate = initialP;
        this.divide = needDivisionForChangingTime;
    }

    /**
     * Sets the data of the graph via time period
     *
     * @param timeChosen The time period to filter by
     */
    public void updateLine(String timeChosen, Map<LocalDateTime, Double> data) {
        dataSeries.clear();
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
                if (divide) {
                    dataSeries.update(rtp, (dataSeries.getValue(rtp).doubleValue() + entry.getValue()) / 2.0);
                } else {
                    dataSeries.update(rtp, dataSeries.getValue(rtp).doubleValue() + entry.getValue());
                }

            }
        }
//        updateDateFilters(currentStart, currentEnd);

//        this.data = data;
    }

    public TimeSeries getDataSeries() {
        return dataSeries;
    }

    public void setDataSeries(TimeSeries dataSeries) {
        this.dataSeries = dataSeries;
    }

    public TimeSeries getFilteredSeries() {
        return filteredSeries;
    }

    public void setFilteredSeries(TimeSeries filteredSeries) {
        this.filteredSeries = filteredSeries;
    }

    public String getBasePredicate() {
        return basePredicate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
