import static org.junit.jupiter.api.Assertions.assertEquals;

import javafx.embed.swing.JFXPanel;
import model.GraphModel;
import model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class Sprint3Tests {
    private static Model model;

    @BeforeAll
    static void prep() {
        JFXPanel dummy = new JFXPanel();
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
}
