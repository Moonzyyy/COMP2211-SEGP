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

  /**
   * @return return the date
   */
    public LocalDateTime getDate() {
        return date;
    }

  /**
   * @param date set the date
   */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

  /**
   * @return get the userID
   */
    public String getUserId() {
        return userId;
    }


  /**
   * @param id set the userID
   */
    public void setUserId(String id) {
        this.userId = id;
    }


  /**
   * @param clickCost set the click cost
   */
    public void setClickCost(String clickCost) {
        this.clickCost = Double.parseDouble(clickCost);
    }


  /**
   * @return get the click cost
   */
    public Double getClickCost() {
        return clickCost;
    }
}
