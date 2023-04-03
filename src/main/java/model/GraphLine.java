package model;

import org.jfree.data.time.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class GraphLine {
    private final Map<String, Boolean> basePredicates;
    private final int id;
    private TimeSeries dataSeries;
    private final boolean divide;
    private final String title;


    GraphLine(int id, String title, boolean needDivisionForChangingTime, Map<String, Boolean> initialPs) {
        this.id = id;
        this.title = title;
        this.dataSeries = new TimeSeries(title);
        this.basePredicates = initialPs;
        this.divide = needDivisionForChangingTime;
    }

    /**
     * Sets the data of the graph via time period
     *
     * @param timeChosen The time period to filter by
     */
    public TimeSeries updateLine(String timeChosen, Map<LocalDateTime, Double> data) {
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
        return dataSeries;
    }

    public TimeSeries getDataSeries() {
        return dataSeries;
    }

    public void setDataSeries(TimeSeries dataSeries) {
        this.dataSeries = dataSeries;
    }

    public Map<String, Boolean> getBasePredicates() {
        return basePredicates;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }
}
