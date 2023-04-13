import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class Sprint1Tests {

    static Model model;

    @BeforeAll
    static void setUp() {
        model = new Model();
        model.setClicksFile(new File("src/test/testData/click_log.csv"));
        model.setImpressionsFile(new File("src/test/testData/impression_log.csv"));
        model.setServerFile(new File("src/test/testData/server_log.csv"));
        model.importData();
        model.getMetrics();
    }

    @Test
    void UserStory4() {
        assertEquals(486104, model.totalImpressions(), "Number of impressions");
    }

    @Test
    void UserStory5() {
        assertEquals(23806, model.numberOfUniques(), "Number of uniques");
    }

    @Test
    void UserStory6() {
        assertEquals(2026, model.numberOfConversions(), "Number of conversions");
    }

    @Test
    void UserStory7() { assertEquals(23923, model.totalClicks(), "Number of clicks"); }

    @Test
    void UserStory8() {
        assertEquals(8665, model.numberOfBounces(), "Number of bounces");
    }

    @Test
    void UserStory9() {
        assertEquals(0.362, model.bounceRate(), "Bounce rate");
    }

    @Test
    void UserStory10() {
        assertEquals(118097.92, model.totalCost(), "Total cost");
    }

    @Test
    void UserStory14() {
        assertEquals(0.049, model.clickThroughRate(), "CTR");
    }

    @Test
    void UserStory15() {
        assertEquals(4.92, model.costPerClick(), "CPC");
    }
    @Test
    void UserStory20() {
        assertEquals(58.29, model.costPerAcquisition(), "CPA");
    }

    @Test
    void ThousandImps() {
        assertEquals(0.24, model.costPerThousandImps(), "CPTI");
    }
}

