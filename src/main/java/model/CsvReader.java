package model;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class CsvReader {

    private List<Impression> impressions = null;
    private List<Click> clicks = null;
    private List<Server> serverInteractions = null;

    public CsvReader(File clicksFile, File impressionsFile, File serverFile) {
        System.out.println("Loading, please wait...");
        //Get CSV data from all 3 log files (can be changed to for loop)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
//            InputStream impressionPath = getClass().getResourceAsStream(impressionsFile);
            InputStream impressionPath = new FileInputStream(impressionsFile);
            BufferedReader iReader = new BufferedReader(new InputStreamReader(impressionPath));
            impressions = splitArray(iReader).map((p) -> new Impression(p, formatter)).toList();
            iReader.close();

//            InputStream clickPath = getClass().getResourceAsStream(clicksFile);
            InputStream clickPath = new FileInputStream(clicksFile);
            BufferedReader cReader = new BufferedReader(new InputStreamReader(clickPath));
            clicks = splitArray(cReader).map((p) -> new Click(p, formatter)).toList();
            cReader.close();

//            InputStream serverPath = getClass().getResourceAsStream(serverFile);
            InputStream serverPath = new FileInputStream(serverFile);
            BufferedReader sReader = new BufferedReader(new InputStreamReader(serverPath));
            serverInteractions = splitArray(sReader).map((p) -> new Server(p, formatter)).toList();
            sReader.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //DO NOT DELETE!!!!
    /*private BufferedReader getReader(String filepath) throws FileNotFoundException {
        //Can not be included in a jar file, might need to be re-used later on
        File inputF = new File(filepath);
        InputStream inputFS = new FileInputStream(inputF);
        return new BufferedReader(new InputStreamReader(inputFS));
    }*/


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
