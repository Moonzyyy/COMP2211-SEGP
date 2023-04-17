package model;

import org.jfree.data.time.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class GraphLine {
    private String basePredicate;
    private final int id;
    private TimeSeries dataSeries;
    private TimeSeries datedSeries1;
    private TimeSeries datedSeries2;
    private final boolean divide;
    private String title;

    private boolean enabled;

    GraphLine(int id, String title, boolean needDivisionForChangingTime, String initialP, boolean enabled) {
        this.enabled = enabled;
        this.id = id;
        this.title = title;
        this.dataSeries = new TimeSeries(title);
        this.datedSeries1 = new TimeSeries(title);
        this.datedSeries2 = new TimeSeries(title);
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
//        this.setDatedSeries1(dataSeries);
//        updateDateFilters(currentStart, currentEnd);

//        this.data = data;
    }

    public TimeSeries getDataSeries() {
        return dataSeries;
    }

    public void setDataSeries(TimeSeries dataSeries) {
        this.dataSeries = dataSeries;
    }

    public TimeSeries getDatedSeries1() {
        return datedSeries1;
    }

    public void setDatedSeries1(TimeSeries datedSeries1) {
        this.datedSeries1 = datedSeries1;
    }

    public TimeSeries getDatedSeries2() {
        return datedSeries2;
    }

    public void setDatedSeries2(TimeSeries datedSeries2) {
        this.datedSeries2 = datedSeries2;
    }

    public String getBasePredicate() {
        return basePredicate;
    }

    public void setBasePredicate(String basePredicate) {
        this.basePredicate = basePredicate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.setDataSeries(new TimeSeries(title));
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
