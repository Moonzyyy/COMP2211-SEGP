import static org.junit.jupiter.api.Assertions.*;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import model.*;

import java.io.File;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Sprint2Tests {

    private static Model model;
    private static GraphModel graphModel;
    @BeforeAll
    static void prep() {
        model = new Model();
        model.setClicksFile(new File("src/test/TestData/click_log.csv"));
        model.setImpressionsFile(new File("src/test/TestData/impression_log.csv"));
        model.setServerFile(new File("src/test/TestData/server_log.csv"));
        model.importData();
        model.getMetrics();
    }

    @Test
    void UserStory18Au25() {
        graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //add age filter
        assertEquals(97050, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for ages <24");
    }

    @Test
    void UserStory18Au34() {
        assertEquals(121984, 19, "Filtered for ages 25-34");
    }

    @Test
    void UserStory18Au44() {
        assertEquals(121774, 19, "Filtered for ages 35-44");
    }

    @Test
    void UserStory18Au54() {
        assertEquals(84324, 19, "Filtered for ages 45-54");
    }

    @Test
    void UserStory18Ao54() {
        assertEquals(60972, 19, "Filtered for ages >54");
    }

    @Test
    void UserStory18GM() {
        assertEquals(161469, 19, "Filtered for Males");
    }

    @Test
    void UserStory18GF() {
        assertEquals(324635, 19, "Filtered for Females");
    }

    @Test
    void UserStory18IL() {
        assertEquals(145948, 19, "Filtered for Low Income");
    }

    @Test
    void UserStory18IM() {
        assertEquals(243050, 19, "Filtered for Middle Income");
    }

    @Test
    void UserStory18IH() {
        assertEquals(97106, 19, "Filtered for High Income");
    }

    @Test
    void UserStory19B() {
        assertEquals(69583, 19, "Filtered for Blogs");
    }

    @Test
    void UserStory19N() {
        assertEquals(139170, 19, "Filtered for News");
    }

    @Test
    void UserStory19Sp() {
        assertEquals(139256, 19, "Filtered for Shopping");
    }

    @Test
    void UserStory19SM() {
        assertEquals(138095, 19, "Filtered for Social Media");
    }
    @Test
    public void testTotalImpressions() {
        assertEquals(13, model.totalImpressions(),"Tests if TotalImpressions counts properly");
    }

    @Test
    public void testLoadImpressionData() {
        Map<LocalDateTime,Double> impressionData = model.loadImpressionData();
        assertEquals(12, impressionData.size(), "Testing if the output has the correct size");
        assertEquals(1.0,impressionData.get(LocalDateTime.parse("2015-01-03T12:00")),"Checks if the data is mapped correctly");
        assertEquals(2.0,impressionData.get(LocalDateTime.parse("2015-01-01T12:00")), "Checks if it calculates the data correctly");
    }

    @Test
    public void testTotalClicks() {
        assertEquals(12, model.totalClicks(),"Tests if TotalClicks counts properly");
    }
    @Test
    public void testLoadClicksData() {
        Map<LocalDateTime,Double> clicksData = model.loadClicksData();
        assertEquals(12,clicksData.size(), "Testing if the output has the correct size");
        assertEquals(2.0,clicksData.get(LocalDateTime.parse("2015-01-01T12:00")), "Checks if it calculates the data correctly");
    }
}
