package csvTest;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;
import com.opencsv.bean.CsvToBeanBuilder;

public class csvReader {
    private static final String IMPRESSION_LOG_FILEPATH = "/Users/chris/UniLocal/COMP2211/testData/2_month_campaign/impression_log.csv";

    public static void main(String[] args) {
        try {
            FileReader fileReader = new FileReader(IMPRESSION_LOG_FILEPATH);
            CSVReader csvReader = new CSVReader(fileReader);
            System.out.println(csvReader.readAll().size());
//            List<Users> beans = new CsvToBeanBuilder<Users>(fileReader).withType(Users.class).build().parse();

//            System.out.println(beans.stream().filter(p -> p.getGender().equals("Male")).count());
//            System.out.println(beans.get(0).toString());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
