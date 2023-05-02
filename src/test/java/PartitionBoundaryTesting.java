import core.Controller;
import model.GraphModel;
import model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PartitionBoundaryTesting {
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

    //Bounce def partitions
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
     * Helper function for pages in bounce def to increase modularity
     *
     * @param bounceVal the bounce value
     */
    private void bounceTimeTestHelper(int bounceVal) {
        model.setBounceDef("Time");
        model.setBounceTimeValue(bounceVal);
    }

    //Valid inputs
    @Test
    void bounceNumPages5Valid() {
        bouncePageTestHelper(5);
        assertEquals(14852, model.numberOfBounces(), "Number of bounces when pages viewed is 5");
    }

    @Test
    void bounceRatePages5Valid() {
        bouncePageTestHelper(5);
        assertEquals(0.621, model.bounceRate(), "Bounce rate when pages viewed is 5");
    }

    @Test
    void bounceNumTime5Valid() {
        bounceTimeTestHelper(5);
        assertEquals(5134, model.numberOfBounces(), "Number of bounces when time taken is 5");
    }

    @Test
    void bounceRateTime5Valid() {
        bounceTimeTestHelper(5);
        assertEquals(0.215, model.bounceRate(), "Bounce rate when time taken is 5");
    }

    //Invalid inputs
    @Test
    void bouncePages11Invalid() {
        assertFalse(Controller.validBounceDef("11", true));
    }

    @Test
    void bounceTime301Invalid() {
        assertFalse(Controller.validBounceDef("301", false));
    }

    @Test
    void bouncePages0Invalid() {
        assertFalse(Controller.validBounceDef("0", true));
    }

    @Test
    void bounceTime0Invalid() {
        assertFalse(Controller.validBounceDef("0", false));
    }

    //Boundary
    @Test
    void bounceNumPages1Boundary() {
        bouncePageTestHelper(1);
        assertEquals(8665, model.numberOfBounces(), "Number of bounces when pages viewed is 1");
    }

    @Test
    void bounceRatePages1Boundary() {
        bouncePageTestHelper(1);
        assertEquals(0.362, model.bounceRate(), "Bounce rate when pages viewed is 1");
    }

    @Test
    void bounceNumPages2Boundary() {
        bouncePageTestHelper(2);
        assertEquals(10089, model.numberOfBounces(), "Number of bounces when pages viewed is 2");
    }

    @Test
    void bounceRatePages2Boundary() {
        bouncePageTestHelper(2);
        assertEquals(0.422, model.bounceRate(), "Bounce rate when pages viewed is 2");
    }

    @Test
    void bounceNumPages9Boundary() {
        bouncePageTestHelper(9);
        assertEquals(21186, model.numberOfBounces(), "Number of bounces when pages viewed is 9");
    }

    @Test
    void bounceRatePages9Boundary() {
        bouncePageTestHelper(9);
        assertEquals(0.886, model.bounceRate(), "Bounce rate when pages viewed is 9");
    }

    @Test
    void bounceNumPages10Boundary() {
        bouncePageTestHelper(10);
        assertEquals(22819, model.numberOfBounces(), "Number of bounces when pages viewed is 10");
    }

    @Test
    void bounceRatePages10Boundary() {
        bouncePageTestHelper(10);
        assertEquals(0.954, model.bounceRate(), "Bounce rate when pages viewed is 10");
    }

    @Test
    void bounceNumTime1Boundary() {
        bounceTimeTestHelper(1);
        assertEquals(3259, model.numberOfBounces(), "Number of bounces when time taken is 1");
    }

    @Test
    void bounceRateTime1Boundary() {
        bounceTimeTestHelper(1);
        assertEquals(0.136, model.bounceRate(), "Bounce rate when time taken is 1");
    }

    @Test
    void bounceNumTime2Boundary() {
        bounceTimeTestHelper(2);
        assertEquals(3775, model.numberOfBounces(), "Number of bounces when time taken is 2");
    }

    @Test
    void bounceRateTime2Boundary() {
        bounceTimeTestHelper(2);
        assertEquals(0.158, model.bounceRate(), "Bounce rate when time taken is 2");
    }

    @Test
    void bounceNumTime299Boundary() {
        bounceTimeTestHelper(299);
        assertEquals(21387, model.numberOfBounces(), "Number of bounces when time taken is 9");
    }

    @Test
    void bounceRateTime299Boundary() {
        bounceTimeTestHelper(299);
        assertEquals(0.894, model.bounceRate(), "Bounce rate when time taken is 9");
    }

    @Test
    void bounceNumTime300Boundary() {
        bounceTimeTestHelper(300);
        assertEquals(21404, model.numberOfBounces(), "Number of bounces when time taken is 10");
    }

    @Test
    void bounceRateTime300Boundary() {
        bounceTimeTestHelper(300);
        assertEquals(0.895, model.bounceRate(), "Bounce rate when time taken is 10");
    }
}
