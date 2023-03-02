package csvTest;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Stream;

public class CsvReader {
    private static final String IMPRESSION_LOG_FILEPATH = "src/main/resources/testData/2_week_campaign_2/impression_log.csv";
    private static final String CLICK_LOG_FILEPATH = "src/main/resources/testData/2_week_campaign_2/click_log.csv";
    private static final String SERVER_LOG_FILEPATH = "src/main/resources/testData/2_week_campaign_2/server_log.csv";
    private static final String TWO_MONTH_IMPRESSION_LOG_FILEPATH = "/Users/chris/UniLocal/COMP2211/testData/2_month_campaign/impression_log.csv";
    private static final String TWO_MONTH_CLICK_LOG_FILEPATH = "/Users/chris/UniLocal/COMP2211/testData/2_month_campaign/click_log.csv";

    private static List<Impression> impressions = null;
    private static List<Click> clicks = null;
    private static List<Server> serverInteractions = null;

    public static void main(String[] args) {
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

//            List<Impression> men = inputList.parallelStream().filter(p -> p.getGender().equals("Male")).toList();
//            List<Impression> men = inputList.stream().filter(p -> p.getGender().equals("Male")).toList();
//            List<Impression> dates = inputList.parallelStream().filter(p -> {
//                var date = p.getDate();
//                return date.isAfter(start) && date.isBefore(end);
//            }).toList();
//            System.out.println(dates);

//            System.out.println(inputList.size());
//            System.out.println(inputList.parallelStream().distinct());

        //Commented out because inputlist was changed to 3 different types so function no longer works
        //takeUserInput();

    }

    private static BufferedReader getReader(String filepath) throws FileNotFoundException {
        File inputF = new File(filepath);
        InputStream inputFS = new FileInputStream(inputF);
        return new BufferedReader(new InputStreamReader(inputFS));
    }

    private static Stream<String[]> splitArray(BufferedReader br) {
        return br.lines().skip(1).map((line) -> split(line,','));
    }

//    /**
//    Takes in the input from user for developers only. To disable comment the function in main function.
//     */
//    private static void takeUserInput() {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Data loaded");
//        if (inputList != null) {
//            while (true) {
//                System.out.println("Please choose a metric to check: \n 1: Total impression cost; \n 99: Exit");
//                System.out.print("Option: ");
//                while (!scanner.hasNextInt()) {
//                    System.out.print("Please enter an integer: ");
//                    scanner.next();
//                }
//                int option = scanner.nextInt();
//                handleUserInput(option);
//            }
//        }
//    }

//    /**
//    Executes the required function for the selected option.
//     */
//    private static void handleUserInput(int option) {
//        switch (option) {
//            case 1 -> System.out.println(inputList.parallelStream().mapToDouble(Impression::getImpressionCost).sum());
//            case 99 -> System.exit(0);
//            default -> System.out.println("Please enter a valid option");
//        }
//    }

    /**
    Slightly more efficient than String.split()
    @param line: the line to split
    @param delimiter: the character by which to split the string
     */
    public static String[] split(final String line, final char delimiter)
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
}
