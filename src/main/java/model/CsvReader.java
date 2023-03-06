package model;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class CsvReader {
    private final String IMPRESSION_LOG_FILEPATH = "src/test/TestData/impression_log.csv";
    private final String CLICK_LOG_FILEPATH = "src/test/TestData/click_log.csv";
    private final String SERVER_LOG_FILEPATH = "src/test/TestData/server_log.csv";

    private List<Impression> impressions = null;
    private List<Click> clicks = null;
    private List<Server> serverInteractions = null;

    public CsvReader() {
        System.out.println("Loading, please wait...");
        //Get CSV data from all 3 log files (can be changed to for loop)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            var iReader = getReader(IMPRESSION_LOG_FILEPATH);
            impressions = splitArray(iReader).map((p) -> new Impression(p, formatter)).toList();
            iReader.close();
            var cReader = getReader(CLICK_LOG_FILEPATH);
            clicks = splitArray(cReader).map((p) -> new Click(p, formatter)).toList();
            cReader.close();
            var sReader = getReader(SERVER_LOG_FILEPATH);
            serverInteractions = splitArray(sReader).map((p) -> new Server(p, formatter)).toList();
            sReader.close();
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
        return br.lines().skip(1).parallel().map((line) -> split(line,','));
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
