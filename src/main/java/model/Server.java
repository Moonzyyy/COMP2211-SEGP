package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Server {
    private LocalDateTime entryDate;

    private int timeSpent;

    private int pagesViewed;

    private boolean conversion;

    /**
     *
     * @param input: The incoming data used to create the Impression
     * @param formatter: The date formatter used when converting date string to DateTime object.
     *                   It's a parameter to avoid re-initializing for every object.
     */
    public Server(String[] input, DateTimeFormatter formatter) {
       String dateWithoutMS = input[0].substring(0, 13);
        LocalDateTime entry = LocalDateTime.parse(dateWithoutMS, formatter);
        setEntryDate(entry);

        setTimeSpent(input[0], input[2]);
        setPagesViewed(input[3]);
        setConversion(input[4]);
    }

  /**
   * @return get the entry date
   */
    public LocalDateTime getEntryDate() {
        return entryDate;
    }

  /**
   * @param date set the entry date
   */
    public void setEntryDate(LocalDateTime date) {
        this.entryDate = date;
    }

  /**
   * @return get the amount of time user spent
   */
    public int getTimeSpent() {
        return timeSpent;
    }

    /**
     * Gets the time spent on the website, or returns -1 if the user has not left (i.e. exitDate is n/a)
     */
    public void setTimeSpent(String entryDateString, String exitDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime entryDate = LocalDateTime.parse(entryDateString, formatter);
        if (exitDate.equals("n/a")) {
            this.timeSpent = -1;
        } else {
            this.timeSpent = (int) entryDate.until(LocalDateTime.parse(exitDate, formatter), ChronoUnit.SECONDS);
        }
    }

  /**
   * @return get the amount of pages viewed
   */
    public int getPagesViewed() {
        return pagesViewed;
    }

  /**
   * @param pagesViewed set the amount of pages viewed
   */
    public void setPagesViewed(String pagesViewed) {
        this.pagesViewed = Integer.parseInt(pagesViewed);
    }

  /**
   * @return get the conversion
   */
    public boolean getConversion() {
        return conversion;
    }

    /**
     * Converts "Yes" or "No" conversions to true and false
     */
    public void setConversion(String conversion) {
        this.conversion = conversion.equals("Yes");
    }
}
