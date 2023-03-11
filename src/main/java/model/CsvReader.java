package model;

import core.AdViz;
import core.Controller;
import core.DataRoot;
import javafx.util.Pair;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvReader {

    private HashMap<Long,User> users = new HashMap<Long, User>();
    private List<Click> clicks = null;
    private List<Server> serverInteractions = null;

//    public CsvReader() {
//        System.out.println("Loading, please wait...");
//        //Get CSV data from all 3 log files (can be changed to for loop)
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        try {
//            InputStream clickPath = getClass().getResourceAsStream("/testdata/click_log.csv");
//            BufferedReader cReader = new BufferedReader(new InputStreamReader(clickPath));
////            clicks = splitArray(cReader).map((p) -> new Click(p, formatter)).toList();
////            clicks = splitArray(cReader).map((p) -> new Click(p, formatter)).toList();
////            cReader.close();
//
//            InputStream serverPath = getClass().getResourceAsStream("/testdata/server_log.csv");
//            BufferedReader sReader = new BufferedReader(new InputStreamReader(serverPath));
////            serverInteractions = splitArray(sReader).map((p) -> new Server(p, formatter)).toList();
////            sReader.close();
//
//
//            InputStream impressionPath = getClass().getResourceAsStream("/testdata/impression_log.csv");
//            BufferedReader iReader = new BufferedReader(new InputStreamReader(impressionPath));
////            var newUsers = s1.filter(distinctByKey(x -> x[1]));
//            HashMap<Long, User> map = new HashMap<>();
//
//            //OPTION 1
////            Stream<String[]> s1 = splitArray(iReader);
////            s1.forEach(line -> {
////                User user = map.get(Long.parseLong(line[1]));
////                if (user == null) {
////                    user = new User(line, formatter);
////                    map.put(user.getId(), user);
////                } else {
////                    user.addImpression(new Pair<>(LocalDateTime.parse(line[0], formatter), Double.parseDouble(line[6])));
////                }
////            });
//            iReader.lines().skip(1).forEach(line -> {
//                String[] arr = split(line, ',');
//                User user = map.get(Long.parseLong(arr[1]));
//                if (user == null) {
//                    user = new User(arr, formatter);
//                    user.addImpression(new Pair<>(LocalDateTime.parse(arr[0], formatter), Double.parseDouble(arr[6])));
//                    map.put(user.getId(), user);
//                } else {
//                    user.addImpression(new Pair<>(LocalDateTime.parse(arr[0], formatter), Double.parseDouble(arr[6])));
//                }
//            });
//            //END OPTION 1
//            //OPTION 2
////            iReader.readLine();
////            String line;
////            while ((line = iReader.readLine()) != null) {
////                String[] arr = split(line, ',');
////                User user = map.get(Long.parseLong(arr[1]));
////                if (user == null) {
////                    user = new User(arr, formatter);
////                    map.put(user.getId(), user);
////                } else {
////                    user.addImpression(new Pair<>(LocalDateTime.parse(arr[0], formatter), Double.parseDouble(arr[6])));
////                }
////            }
//            //END OPTION 2
//            System.out.println("done!");
//
////            users = splitArray(iReader).map((p) -> new User(p, formatter)).collect(HashMap::new, (m, u) -> m.put(u.getId(), u), HashMap::putAll);
////            System.out.println("Users: " + users.size());
////            iReader.close();
////
//
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }
    public CsvReader() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            InputStream impressionPath = getClass().getResourceAsStream("/testdata/impression_log.csv");
            BufferedReader iReader = new BufferedReader(new InputStreamReader(impressionPath));

            DataRoot root = (DataRoot) Controller.getInstance().storageManager().root();
            HashMap<Long, User> users = root.getUsers();
            iReader.lines().skip(1).forEach(line -> {
                String[] arr = line.split(",");
                User user = users.get(Long.parseLong(arr[1]));
                if (user == null) {
                    user = new User(arr, formatter);
                    users.put(user.getId(), user);
                }
                try {
                    user.addImpression(new Pair<>(formatter.parse(arr[0]), Double.parseDouble(arr[6])));
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            });
            System.out.println(root.getUsers().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        Controller.getInstance().shutdown();
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
