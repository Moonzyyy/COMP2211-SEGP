import static org.junit.jupiter.api.Assertions.assertEquals;

import model.GraphModel;
import model.HistogramModel;
import model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Arrays;

public class Sprint3Tests {
    private static Model model;

    @BeforeAll
    static void prep() {
//        JFXPanel dummy = new JFXPanel();
        model = new Model();
        model.setClicksFile(new File("src/test/TestData/click_log.csv"));
        model.setImpressionsFile(new File("src/test/TestData/impression_log.csv"));
        model.setServerFile(new File("src/test/TestData/server_log.csv"));
        model.importData();
        model.getMetrics();
    }

    /**
     * Helper function for pages in bounce def to increase modularity
     *
     * @param bounceVal the bounce value
     */
    private void bouncePageTestHelper(int bounceVal) {
        model.setBounceDef("Page");
        model.setBouncePageValue(bounceVal);
    }

    /**
     * Helper function for time in bounce def to increase modularity
     *
     * @param bounceVal the bounce value
     */
    private void bounceTimeTestHelper(int bounceVal) {
        model.setBounceDef("Time");
        model.setBounceTimeValue(bounceVal);
    }

    @Test
    void bounceNumPages2() {
        bouncePageTestHelper(2);
        assertEquals(10089, model.numberOfBounces(), "Number of bounces when pages viewed is 2");
    }

    @Test
    void bounceRatePages2() {
        bouncePageTestHelper(2);
        assertEquals(0.422, model.bounceRate(), "Bounce rate when pages viewed is 2");
    }

    @Test
    void bounceNumTime2() {
        bounceTimeTestHelper(2);
        assertEquals(3775, model.numberOfBounces(), "Number of bounces when time taken is 2");
    }

    @Test
    void bounceRateTime2() {
        bounceTimeTestHelper(2);
        assertEquals(0.158, model.bounceRate(), "Bounce rate when time taken is 2");
    }

    //Histogram test
    @Test
    void histogramTest() {
        HistogramModel hsModel = new HistogramModel(model, 4);
        //maybe test a few different click costs and check if their frequency is correct?
        assertEquals(2.1, Arrays.stream(hsModel.extractCostData(model.loadTotalCostData())).sum());
    }
}
