import static org.junit.jupiter.api.Assertions.assertEquals;

import model.GraphModel;
import model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

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

    /**
     * Helper function to increase modularity
     *
     * @param bounceDef the bounce definition, either page or time
     * @param bounceVal the bounce value
     */
    private void bounceTestHelper(String bounceDef, int bounceVal) {
        model.setBounceDef(bounceDef);
        model.setBounceValue(bounceVal);
    }

    @Test
    void bounceNumPages2() {
        bounceTestHelper("Page", 2);
        assertEquals(10089, model.numberOfBounces(), "Number of bounces when pages viewed is 2");
    }

    @Test
    void bounceRatePages2() {
        bounceTestHelper("Page", 2);
        assertEquals(0.422, model.bounceRate(), "Bounce rate when pages viewed is 2");
    }

    @Test
    void bounceNumTime2() {
        bounceTestHelper("Time", 2);
        assertEquals(3775, model.numberOfBounces(), "Number of bounces when time taken is 2");
    }

    @Test
    void bounceRateTime2() {
        bounceTestHelper("Time", 2);
        assertEquals(0.158, model.bounceRate(), "Bounce rate when time taken is 2");
    }
}
