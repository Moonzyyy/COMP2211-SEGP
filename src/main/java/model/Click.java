package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Click {
    private LocalDateTime date;

    private String userId;

    private Double clickCost;

    /**
     *
     * @param input: The incoming data used to create the Click
     * @param formatter: The date formatter used when converting date string to DateTime object.
     *                   It's a parameter to avoid re-initializing for every object.
     */
    public Click(String[] input, DateTimeFormatter formatter) {
        setDate(LocalDateTime.parse(input[0], formatter));
        setUserId(input[1]);
        setClickCost(input[2]);
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = id;
    }

    public void setClickCost(String clickCost) {
        this.clickCost = Double.parseDouble(clickCost);
    }

    public Double getClickCost() {
        return clickCost;
    }
}
