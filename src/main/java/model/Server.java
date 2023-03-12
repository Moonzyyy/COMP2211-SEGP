package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Server {
    private LocalDateTime entryDate;

    private Long userId;

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
        setEntryDate(LocalDateTime.parse(input[0], formatter));
        setUserId(input[1]);
        setTimeSpent(LocalDateTime.parse(input[0], formatter), input[2], formatter);
        setPagesViewed(input[3]);
        setConversion(input[4]);
    }

    public LocalDateTime getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDateTime date) {
        this.entryDate = date;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(String id) {
        this.userId = Long.parseLong(id);
    }

    public int getTimeSpent() {
        return timeSpent;
    }

    /**
     * Gets the time spent on the website, or returns -1 if the user has not left (i.e. exitDate is n/a)
     */
    public void setTimeSpent(LocalDateTime entryDate, String exitDate, DateTimeFormatter formatter) {
        if (exitDate.equals("n/a")) {
            this.timeSpent = -1;
        } else {
            this.timeSpent = (int) entryDate.until(LocalDateTime.parse(exitDate, formatter), ChronoUnit.SECONDS);
        }
    }

    public int getPagesViewed() {
        return pagesViewed;
    }

    public void setPagesViewed(String pagesViewed) {
        this.pagesViewed = Integer.parseInt(pagesViewed);
    }

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
