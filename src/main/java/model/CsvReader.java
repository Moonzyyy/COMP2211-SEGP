package model;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class CsvReader {
    private final String IMPRESSION_LOG_FILEPATH = "src/main/resources/testData/2_week_campaign_2/impression_log.csv";
    private final String CLICK_LOG_FILEPATH = "src/main/resources/testData/2_week_campaign_2/click_log.csv";
    private final String SERVER_LOG_FILEPATH = "src/main/resources/testData/2_week_campaign_2/server_log.csv";
    private final String TWO_MONTH_IMPRESSION_LOG_FILEPATH = "/Users/chris/UniLocal/COMP2211/testData/2_month_campaign/impression_log.csv";
    private final String TWO_MONTH_CLICK_LOG_FILEPATH = "/Users/chris/UniLocal/COMP2211/testData/2_month_campaign/click_log.csv";

    private List<Impression> impressions = null;
    private List<Click> clicks = null;
    private List<Server> serverInteractions = null;

    public CsvReader() {
        System.out.println("Loading, please wait...");
        //Get CSV data from all 3 log files (can be changed to for loop)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            impressions = splitArray(getReader(IMPRESSION_LOG_FILEPATH)).map((p) -> new Impression(p, formatter)).toList();
            clicks = splitArray(getReader(CLICK_LOG_FILEPATH)).map((p) -> new Click(p, formatter)).toList();
            serverInteractions = splitArray(getReader(SERVER_LOG_FILEPATH)).map((p) -> new Server(p, formatter)).toList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private BufferedReader getReader(String filepath) throws FileNotFoundException {
        File inputF = new File(filepath);
        InputStream inputFS = new FileInputStream(inputF);
        return new BufferedReader(new InputStreamReader(inputFS));
    }

    private Stream<String[]> splitArray(BufferedReader br) {
        return br.lines().skip(1).map((line) -> split(line,','));
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

    public List<Impression> getImpressions() {
        return impressions;
    }

    public List<Click> getClicks() {
        return clicks;
    }

    public List<Server> getServerInteractions() {
        return serverInteractions;
    }

}
