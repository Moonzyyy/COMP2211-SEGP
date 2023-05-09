import core.Controller;
import model.GraphModel;
import model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;

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

    /**
     * Helper function to increase modularity
     *
     * @param gm the graph model
     * @return the sum of all the values as a double
     */
    private double graphModelTestHelper(GraphModel gm) {
        double sum = 0.0;
        for (int a = 0; a < gm.getLines().get(0).getDataSeries().getItemCount(); a++) {
            sum += (double) gm.getLines().get(0).getDataSeries().getDataItem(a).getValue();
        }
        return sum;
    }

    @Test
    void allFiltersBoundary() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("age_1", true);
        hm2.put("age_2", true);
        hm2.put("age_3", true);
        hm2.put("age_4", true);
        hm2.put("age_5", true);
        hm2.put("context_1", true);
        hm2.put("context_2", true);
        hm2.put("context_3", true);
        hm2.put("context_4", true);
        hm2.put("context_5", true);
        hm2.put("context_6", true);
        hm2.put("income_1", true);
        hm2.put("income_2", true);
        hm2.put("income_3", true);
        graphModel.updateGraphData(hm2);
        assertEquals(486104, graphModelTestHelper(graphModel), "Impressions with all but gender filters applied");
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
