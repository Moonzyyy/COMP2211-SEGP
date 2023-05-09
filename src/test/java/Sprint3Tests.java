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
    public double histogramHelper(HistogramModel hsModel, int series, int item) {
        return hsModel.getDataSet().getY(series, item).doubleValue();
    }

    @Test
    void histogramTestStartFreq() {
        HistogramModel hsModel = new HistogramModel(model, 4);
        assertEquals(23, histogramHelper(hsModel, 0, 0));
    }

    @Test
    void histogramTestMiddleFreq() {
        HistogramModel hsModel = new HistogramModel(model, 4);
        assertEquals(8, histogramHelper(hsModel, 0, (int) Math.ceil(hsModel.getDataSet().getItemCount(0)/2)));
    }

    @Test
    void histogramTestEndFreq() {
        HistogramModel hsModel = new HistogramModel(model, 4);
        assertEquals(3, histogramHelper(hsModel, 0, hsModel.getDataSet().getItemCount(0)-1));
    }
}
