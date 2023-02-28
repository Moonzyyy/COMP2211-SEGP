package csvTest;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.opencsv.*;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
public class csvReader {
    private static final String IMPRESSION_LOG_FILEPATH = "src/main/resources/testData/2_week_campaign_2/impression_log.csv";
    private static final String CLICK_LOG_FILEPATH = "src/main/resources/testData/2_week_campaign_2/click_log.csv";
    private static final String SERVER_LOG_FILEPATH = "src/main/resources/testData/2_week_campaign_2/server_log.csv";
    private static final String TWO_MONTH_IMPRESSION_LOG_FILEPATH = "/Users/chris/UniLocal/COMP2211/testData/2_month_campaign/impression_log.csv";
    private static final String TWO_MONTH_CLICK_LOG_FILEPATH = "/Users/chris/UniLocal/COMP2211/testData/2_month_campaign/click_log.csv";

    public static void main(String[] args) {
        try {
            File inputF = new File(TWO_MONTH_IMPRESSION_LOG_FILEPATH);
            InputStream inputFS = new FileInputStream(inputF);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

//            BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
//
//            String nextLine;
//            while ((nextLine = br.readLine()) != null) {
//                queue.put(nextLine);
//            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse("2015-02-01 12:01:21", formatter);
            LocalDateTime end = LocalDateTime.parse("2015-03-01 12:01:21", formatter);

            List<Impression> inputList = br.lines().skip(1).map((line) -> {
                String[] p = split(line,',');


                Impression user = new Impression();

                user.setGender(p[2]);
                LocalDateTime date = LocalDateTime.parse(p[0], formatter);
                user.setDate(date);
                return user;
            }).toList();

            List<Impression> men = inputList.parallelStream().filter(p -> p.getGender().equals("Male")).toList();
//            List<Impression> men = inputList.stream().filter(p -> p.getGender().equals("Male")).toList();
            List<Impression> dates = inputList.parallelStream().filter(p -> {
                var date = p.getDate();
                return date.isAfter(start) && date.isBefore(end);
            }).toList();

            System.out.println(inputList.size());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    Slightly more efficient than String.split()
     */
    public static String[] split(final String line, final char delimiter)
    {
        CharSequence[] temp = new CharSequence[(line.length() / 2) + 1];
        int wordCount = 0;
        int i = 0;
        int j = line.indexOf(delimiter, 0); // first substring

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
}
