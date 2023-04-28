import model.GraphModel;
import model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    /**
     * Helper function to increase modularity
     *
     * @param gm the graph model
     * @return the sum of all the values as a double
     */
    private double testHelper(GraphModel gm) {
        double sum = 0.0;
        for (int a = 0; a < gm.getLines().get(0).getDataSeries().getItemCount(); a++) {
            sum += (double) gm.getLines().get(0).getDataSeries().getDataItem(a).getValue();
        }
        return sum;
    }

    //Bounce def partitions
    /**
     * Helper function for pages in bounce def to increase modularity
     *
     * @param bounceDef the bounce definition, either page or time
     * @param bounceVal the bounce value
     */
    private void bouncePageTestHelper(int bounceVal) {
        model.setBounceDef("Page");
        model.setBouncePageValue(bounceVal);
    }

    /**
     * Helper function for pages in bounce def to increase modularity
     *
     * @param bounceDef the bounce definition, either page or time
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

//    @Test
//    void bounceNumPages2Valid() {
//        bounceTestHelper("Page", 2);
//        assertEquals(10089, model.numberOfBounces(), "Number of bounces when pages viewed is 2");
//    }
//
//    @Test
//    void bounceRatePages2Valid() {
//        bounceTestHelper("Page", 2);
//        assertEquals(0.422, model.bounceRate(), "Bounce rate when pages viewed is 2");
//    }
//
//    @Test
//    void bounceNumTime2Valid() {
//        bounceTestHelper("Time", 2);
//        assertEquals(3775, model.numberOfBounces(), "Number of bounces when time taken is 2");
//    }
//
//    @Test
//    void bounceRateTime2Valid() {
//        bounceTestHelper("Time", 2);
//        assertEquals(0.158, model.bounceRate(), "Bounce rate when time taken is 2");
//    }
}
