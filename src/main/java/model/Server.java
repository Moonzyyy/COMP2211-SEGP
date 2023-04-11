package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public record Server(LocalDateTime entryDate, int timeSpent, int pagesViewed, boolean conversion) {

    /**
     *
     * @param input: The incoming data used to create the Impression
     * @param formatter: The date formatter used when converting date string to DateTime object.
     *                   It's a parameter to avoid re-initializing for every object.
     */
    public Server(String[] input, DateTimeFormatter formatter) {
        this(
                Server.setEntryDate(input[0], formatter),
                Server.setTimeSpent(input[0], input[2]),
                Integer.parseInt(input[3]),
                input[4].equals("Yes")
        );
    }

  /**
   * @return get the entry date
   */
    public LocalDateTime getEntryDate() {
        return entryDate;
    }

  /**
   * Set the entry date
   * @param string converted to LocalDateTime
   * @param formatter used to convert the string to LocalDateTime
   */
    public static LocalDateTime setEntryDate(String string, DateTimeFormatter formatter) {
        String dateWithoutMS = string.substring(0, 13);
        return LocalDateTime.parse(dateWithoutMS, formatter);
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
    public static int setTimeSpent(String entryDateString, String exitDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime entryDate = LocalDateTime.parse(entryDateString, formatter);
        if (exitDate.equals("n/a")) {
            return -1;
        } else {
            return (int) entryDate.until(LocalDateTime.parse(exitDate, formatter), ChronoUnit.SECONDS);
        }
    }

  /**
   * @return get the amount of pages viewed
   */
    public int getPagesViewed() {
        return pagesViewed;
    }

  /**
   * @return get the conversion
   */
    public boolean getConversion() {
        return conversion;
    }
}
