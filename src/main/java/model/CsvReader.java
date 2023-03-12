package model;

import javafx.util.Pair;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CsvReader {

    private HashMap<Long,User> users = new HashMap<Long, User>();

    public CsvReader() {
        System.out.println("Loading, please wait...");
        //Get CSV data from all 3 log files (can be changed to for loop)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        try {
            InputStream impressionPath = getClass().getResourceAsStream("/testdata/impression_log.csv");
            BufferedReader iReader = new BufferedReader(new InputStreamReader(impressionPath));

            iReader.lines().skip(1).forEach(line -> {
                String[] arr = split(line, ',');
                User user = users.get(Long.parseLong(arr[1]));
                if (user == null) {
                    user = new User(arr, formatter);
                    users.put(user.getId(), user);
                }
                user.addImpression(new Pair<>(LocalDateTime.parse(arr[0], formatter), Double.parseDouble(arr[6])));
                final User finalUser = user;
            });
            iReader.close();

            InputStream clickPath = getClass().getResourceAsStream("/testdata/click_log.csv");
            BufferedReader cReader = new BufferedReader(new InputStreamReader(clickPath));
            List<String[]> tmp = splitArray(cReader).toList();
            cReader.close();

            tmp.forEach(click -> {
                final User user = users.get(Long.parseLong(click[1]));
                var dt = LocalDateTime.parse(click[0], formatter);
                user.addClick(new Pair<>(LocalDateTime.parse(click[0], formatter), Double.parseDouble(click[2])));
            });

            InputStream serverPath = getClass().getResourceAsStream("/testdata/server_log.csv");
            BufferedReader sReader = new BufferedReader(new InputStreamReader(serverPath));
            tmp = splitArray(sReader).toList();
            sReader.close();

            tmp.forEach(interaction -> {
                final User user = users.get(Long.parseLong(interaction[1]));
                user.addServer(new Server(interaction, formatter));
            });

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean containsId(final List<User> list, final long id){
        return list.stream().anyMatch(o -> id == o.getId());
    }

    //DO NOT DELETE!!!!
    /*private BufferedReader getReader(String filepath) throws FileNotFoundException {
        //Can not be included in a jar file, might need to be re-used later on
        File inputF = new File(filepath);
        InputStream inputFS = new FileInputStream(inputF);
        return new BufferedReader(new InputStreamReader(inputFS));
    }*/
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
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

    public HashMap<Long,User> getUsers() {
        return users;
    }

}
