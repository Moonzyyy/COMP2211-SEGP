package model;

//import com.opencsv.CSVReader;
import core.AdViz;
import core.Controller;
import javafx.util.Pair;

import java.io.*;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;
import java.util.List;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CsvReader {
    private static final Logger logger = LogManager.getLogger(CsvReader.class);
    private final HashMap<Long,User> users = new HashMap<Long, User>();

    /**
     * Insert log files and create objects of Server, Click and Impression types to put into lists in User
     * @param clicksFile click log file in CSV format input by user
     * @param impressionsFile click log file in CSV format input by user
     * @param serverFile click log file in CSV format input by user
     */
    public CsvReader(File clicksFile, File impressionsFile, File serverFile) {

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
      try {
        logger.info("Reading CSV Files...");
        String columnsChecker = "";

        InputStream impressionPath = new FileInputStream(impressionsFile);
        BufferedReader iReader = new BufferedReader(new InputStreamReader(impressionPath));
        columnsChecker = iReader.readLine().replace(" ", "");
        if (!columnsChecker.equals("Date,ID,Gender,Age,Income,Context,ImpressionCost")) {
          Controller.sendErrorMessage(
              "Error! CSV file put into Impressions does not have the correct columns!"
                  + "\nColumns Needed: Date,ID,Gender,Age,Income,Context,ImpressionCost"
                  + "\nColumns Given: " + columnsChecker);
          throw new Exception("CSV file put into Impressions does not have the correct columns!");
        }

        InputStream clickPath = new FileInputStream(clicksFile);
        BufferedReader cReader = new BufferedReader(new InputStreamReader(clickPath));
        columnsChecker = cReader.readLine().replace(" ", "");
        if (!columnsChecker.equals("Date,ID,ClickCost")) {
          Controller.sendErrorMessage(
              "Error! CSV file put into Clicks does not have the correct columns!"
                  + "\nColumns Needed: Date,ID,ClickCost"
                  + "\nColumns Given: " + columnsChecker);
          throw new Exception("CSV file put into Clicks does not have the correct columns!");
        }

        InputStream serverPath = new FileInputStream(serverFile);
        BufferedReader sReader = new BufferedReader(new InputStreamReader(serverPath));
        columnsChecker = sReader.readLine().replace(" ", "");
        if (!columnsChecker.equals("EntryDate,ID,ExitDate,PagesViewed,Conversion")) {
          Controller.sendErrorMessage(
              "Error! CSV file put into Server does not have the correct columns!"
                  + "\nColumns Needed: EntryDate,ID,ExitDate,PagesViewed,Conversion"
                  + "\nColumns Given: " + columnsChecker);
          throw new Exception("CSV file put into Server does not have the correct columns!");
        }

        logger.info("Processing CSV Files...");

        iReader.lines().forEach(line -> {

          String[] arr = split(line, ',');
          Arrays.stream(arr).forEach( string -> string.trim());
          User user = users.get(Long.parseLong(arr[1]));
          if (user == null) {
            user = new User(arr);
            users.put(user.getId(), user);
          }
          String dateWithoutMS = arr[0].substring(0, 13);

          user.addImpression(new Pair<>(LocalDateTime.parse(dateWithoutMS, formatter),
              Double.parseDouble(arr[6])));
        });
        iReader.close();

        Stream<String[]> tmp = splitArray(cReader);
        tmp.forEach(click -> {
          Arrays.stream(click).forEach(string -> string.trim());
          final User user = users.get(Long.parseLong(click[1]));
          String dateWithoutMS = click[0].substring(0, 13);
          user.addClick(new Pair<>(LocalDateTime.parse(dateWithoutMS, formatter),
              Double.parseDouble(click[2])));
        });
        cReader.close();

        tmp = splitArray(sReader);

        tmp.forEach(interaction -> {
          Arrays.stream(interaction).forEach(string -> string.trim());
          final User user = users.get(Long.parseLong(interaction[1]));
          user.addServer(new Server(interaction, formatter));
        });
        sReader.close();
        logger.info("Done processing CSV Files!");

      } catch (Exception e) {
        logger.error(e.getMessage());
      }
    }

  /**
   * @param br the buffered reader containing the lines of the
   * @return
   */
    private Stream<String[]> splitArray(BufferedReader br) {
        return br.lines().map((line) -> split(line.trim(),','));
    }

    /**
     Slightly more efficient than String.split()
     @param line: the line to split
     @param delimiter: the character by which to split the string
     */
    public String[] split(final String line, final char delimiter)
    {
        CharSequence[] temp = new CharSequence[(line.length() / 2) + 1];
        int wordCount = 0;
        int i = 0;
        int j = line.indexOf(delimiter); // first substring

        while (j >= 0)
        {
            temp[wordCount++] = line.substring(i, j);
            i = j + 1;
            j = line.indexOf(delimiter, i); // rest of substrings
        }

        temp[wordCount++] = line.substring(i); // last substring

        String[] result = new String[wordCount];
        System.arraycopy(temp, 0, result, 0, wordCount);

        return result;
    }

  /**
   * @return the users
   */
    public HashMap<Long,User> getUsers() {
        return users;
    }

}