package model;

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

public class CsvReader {

    private final HashMap<Long,User> users = new HashMap<Long, User>();

    /**
     * Insert log files and create objects of Server, Click and Impression types to put into lists in User
     * @param clicksFile click log file in CSV format input by user
     * @param impressionsFile click log file in CSV format input by user
     * @param serverFile click log file in CSV format input by user
     */
    public CsvReader(File clicksFile, File impressionsFile, File serverFile) {
        System.out.println("Loading, please wait...");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
        try {
//          InputStream impressionPath = getClass().getResourceAsStream(impressionsFile);
            InputStream impressionPath = new FileInputStream(impressionsFile);
            BufferedReader iReader = new BufferedReader(new InputStreamReader(impressionPath));

            iReader.lines().skip(1).forEach(line -> {
                String[] arr = split(line.trim(), ',');
                User user = users.get(Long.parseLong(arr[1]));
                if (user == null) {
                    user = new User(arr);
                    users.put(user.getId(), user);
                }
                String dateWithoutMS = arr[0].substring(0, 13);
                user.addImpression(new Pair<>(LocalDateTime.parse(dateWithoutMS, formatter), Double.parseDouble(arr[6])));
            });
            iReader.close();

//            InputStream clickPath = getClass().getResourceAsStream(clicksFile);
            InputStream clickPath = new FileInputStream(clicksFile);
            BufferedReader cReader = new BufferedReader(new InputStreamReader(clickPath));
            Stream<String[]> tmp = splitArray(cReader);

            tmp.forEach(click -> {
                final User user = users.get(Long.parseLong(click[1]));
              String dateWithoutMS = click[0].substring(0, 13);
                user.addClick(new Pair<>(LocalDateTime.parse(dateWithoutMS, formatter), Double.parseDouble(click[2])));
            });
            cReader.close();
//            InputStream clickPath = getClass().getResourceAsStream(serverFile);
            InputStream serverPath = new FileInputStream(serverFile);
            BufferedReader sReader = new BufferedReader(new InputStreamReader(serverPath));
            tmp = splitArray(sReader);

            tmp.forEach(interaction -> {
                final User user = users.get(Long.parseLong(interaction[1]));
                user.addServer(new Server(interaction, formatter));
            });
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
        return br.lines().skip(1).map((line) -> split(line.trim(),','));
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

    public HashMap<Long,User> getUsers() {
        return users;
    }

}