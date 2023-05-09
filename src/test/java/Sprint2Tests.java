import static org.junit.jupiter.api.Assertions.*;

import core.segments.Age;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.CheckBox;
import org.apache.commons.collections4.Predicate;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesDataItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import model.*;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;

public class Sprint2Tests {

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

    @Test
    void UserStory18Au25() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("age_1", true);
        graphModel.updateGraphData(hm2);
        assertEquals(97050, testHelper(graphModel), "Impressions filtered for ages <25");
    }

    @Test
    void UserStory18Au34() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("age_2", true);
        graphModel.updateGraphData(hm2);
        assertEquals(121984, testHelper(graphModel), "Impressions filtered for ages 25-34");
    }

    @Test
    void UserStory18Au44() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("age_3", true);
        graphModel.updateGraphData(hm2);
        assertEquals(121774, testHelper(graphModel), "Impressions filtered for ages 34-44");
    }

    @Test
    void UserStory18Au54() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("age_4", true);
        graphModel.updateGraphData(hm2);
        assertEquals(84324, testHelper(graphModel), "Impressions filtered for ages 44-54");
    }

    @Test
    void UserStory18Ao54() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("age_5", true);
        graphModel.updateGraphData(hm2);
        assertEquals(60972, testHelper(graphModel), "Impressions filtered for ages >54");
    }

    @Test
    void UserStory18GM() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("male_1", true);
        graphModel.updateGraphData(hm2);
        assertEquals(161469, testHelper(graphModel), "Impressions filtered for males");
    }

    @Test
    void UserStory18GF() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("female_1", true);
        graphModel.updateGraphData(hm2);
        assertEquals(324635, testHelper(graphModel), "Impressions filtered for females");
    }

    @Test
    void UserStory18IL() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("income_3", true);
        graphModel.updateGraphData(hm2);
        assertEquals(145948, testHelper(graphModel), "Impressions filtered for low income");
    }

    @Test
    void UserStory18IM() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("income_2", true);
        graphModel.updateGraphData(hm2);
        assertEquals(243050, testHelper(graphModel), "Impressions filtered for middle income");
    }

    @Test
    void UserStory18IH() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("income_1", true);
        graphModel.updateGraphData(hm2);
        assertEquals(97106, testHelper(graphModel), "Impressions filtered for high income");
    }

    @Test
    void UserStory19B() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("context_4", true);
        graphModel.updateGraphData(hm2);
        assertEquals(69583, testHelper(graphModel), "Impressions filtered for blogs");
    }

    @Test
    void UserStory19N() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("context_1", true);
        graphModel.updateGraphData(hm2);
        assertEquals(139170, testHelper(graphModel), "Impressions filtered for blogs");
    }

    @Test
    void UserStory19Sp() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("context_2", true);
        graphModel.updateGraphData(hm2);
        assertEquals(139256, testHelper(graphModel), "Impressions filtered for shopping");
    }

    @Test
    void UserStory17Full() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        graphModel.updateDateFilters(LocalDate.parse("2015-01-01"), LocalDate.parse("2015-01-01"));
        assertEquals(486104, testHelper(graphModel), "Impressions filtered for entire data range");

    }
//
//    @Test
//    void UserStory171D() {
//        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
//        //create checkbox array list and pass that into graph model
//        graphModel.updateDateFilters(LocalDate.parse("2015-01-03", DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalDate.parse("2015-01-04", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        assertEquals(3, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for 1 day");
//
//    }

    @Test
    void UserStory19H() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("context_5", true);
        graphModel.updateGraphData(hm2);
        assertEquals(0, testHelper(graphModel), "Impressions filtered for hobbies");
    }

    @Test
    void UserStory19T() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("context_6", true);
        graphModel.updateGraphData(hm2);
        assertEquals(0, testHelper(graphModel), "Impressions filtered for travel");
    }

    @Test
    void UserStory19SM() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //Create hashmap with predicates to test, then update the graph with it
        HashMap<String, Boolean> hm2 = new HashMap<>();
        hm2.put("context_3", true);
        graphModel.updateGraphData(hm2);
        assertEquals(138095, testHelper(graphModel), "Impressions filtered for social media");
    }
}
