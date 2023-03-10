package model;

import javafx.util.Pair;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvReader {

    private HashMap<Long,User> users = new HashMap<Long, User>();
    private List<Click> clicks = null;
    private List<Server> serverInteractions = null;

    public CsvReader() {
        System.out.println("Loading, please wait...");
        //Get CSV data from all 3 log files (can be changed to for loop)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            InputStream impressionPath = getClass().getResourceAsStream("/testdata/impression_log.csv");
            BufferedReader iReader = new BufferedReader(new InputStreamReader(impressionPath));
//            impressions = splitArray(iReader).map((p) -> new Impression(p, formatter)).toList();
//            users = splitArray(iReader).map((p) -> new User(p, formatter)).toList();
//            iReader.readLine();
//            System.out.println(splitArray(iReader).map((p) -> p[1]).distinct().count());
            int existing = 0;
            int newUsers = 0;
            var test = splitArray(iReader).collect(Collectors.groupingBy(u -> u[1]));
            System.out.println(test);
            test.forEach((id, value) -> {
                User user = new User(value.get(0), formatter);
                value.forEach(val -> {
                    user.addImpression(new Pair<>(LocalDateTime.parse(val[0], formatter), Double.parseDouble(val[6])));
                });
                users.put(user.getId(), user);
            });
//            System.out.println(users.size());
//            iReader.lines().skip(1).parallel().forEach(line -> {
//                String[] array = split(line, ',');
//                if (users.containsKey(Long.parseLong(array[1]))) {
////                    existing++;
//                } else {
////                    newUsers++;
//                    User user = new User(array, formatter);
//                    users.put(user.getId(), user);
//                }
//            });
            System.out.println("done!");
//            System.out.println("existing: " + existing);
//            System.out.println("new " + newUsers);
//            users = splitArray(iReader).map((p) -> new User(p, formatter)).collect(HashMap::new, (m, u) -> m.put(u.getId(), u), HashMap::putAll);
//            System.out.println("Users: " + users.size());
//            iReader.close();
//
//            InputStream clickPath = getClass().getResourceAsStream("/testdata/click_log.csv");
//            BufferedReader cReader = new BufferedReader(new InputStreamReader(clickPath));
//            clicks = splitArray(cReader).map((p) -> new Click(p, formatter)).toList();
//            cReader.close();
//
//            InputStream serverPath = getClass().getResourceAsStream("/testdata/server_log.csv");
//            BufferedReader sReader = new BufferedReader(new InputStreamReader(serverPath));
//            serverInteractions = splitArray(sReader).map((p) -> new Server(p, formatter)).toList();
//            sReader.close();

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

    public HashMap<Long,User> getImpressions() {
        return users;
    }

    public List<Click> getClicks() {
        return clicks;
    }

    public List<Server> getServerInteractions() {
        return serverInteractions;
    }

}
