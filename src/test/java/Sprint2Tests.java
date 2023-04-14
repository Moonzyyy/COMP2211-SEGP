import static org.junit.jupiter.api.Assertions.*;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.CheckBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import model.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class Sprint2Tests {

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

    @Test
    void UserStory18Au25() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        CheckBox checkBox = new CheckBox();
        checkBox.setId("age_1");
        checkBox.setSelected(true);
        graphModel.newLine("age_1", true, "true");
        graphModel.updateGraphData(graphModel.getLines(), );
        assertEquals(97050, graphModel.getLines().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for ages <25");
        graphModel.resetFilters();
    }

    @Test
    void UserStory18Au34() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("age_2");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(121984, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for ages 25-34");
        graphModel.resetFilters();
    }

    @Test
    void UserStory18Au44() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("age_3");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(121774, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for ages 34-44");
        graphModel.resetFilters();
    }

    @Test
    void UserStory18Au54() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("age_4");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(84324, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for ages 44-54");
        graphModel.resetFilters();
    }

    @Test
    void UserStory18Ao54() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("age_5");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(60972, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for ages >54");
        graphModel.resetFilters();
    }

    @Test
    void UserStory18GM() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("male_1");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(161469, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for males");
        graphModel.resetFilters();
    }

    @Test
    void UserStory18GF() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("female_1");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(324635, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for females");
        graphModel.resetFilters();
    }

    @Test
    void UserStory18IL() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("income_3");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(145948, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for low income");
        graphModel.resetFilters();
    }

    @Test
    void UserStory18IM() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("income_2");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(243050, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for middle income");
        graphModel.resetFilters();
    }

    @Test
    void UserStory18IH() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("income_1");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(97106, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for high income");
        graphModel.resetFilters();
    }

    @Test
    void UserStory19B() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("context_4");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(69583, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for blogs");
        graphModel.resetFilters();
    }

    @Test
    void UserStory19N() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("context_1");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(139170, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for news");
        graphModel.resetFilters();
    }

    @Test
    void UserStory19Sp() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("context_2");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(139256, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for shopping");
        graphModel.resetFilters();
    }

    @Test
    void UserStory17Full() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        graphModel.updateDateFilters(LocalDate.parse("2015-01-01"), LocalDate.parse("2015-01-01"));
        assertEquals(486104, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for entire data range");
        graphModel.resetFilters();
    }
//
//    @Test
//    void UserStory171D() {
//        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
//        //create checkbox array list and pass that into graph model
//        graphModel.updateDateFilters(LocalDate.parse("2015-01-03", DateTimeFormatter.ofPattern("yyyy-MM-dd")), LocalDate.parse("2015-01-04", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        assertEquals(3, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for 1 day");
//        graphModel.resetFilters();
//    }

    @Test
    void UserStory19H() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("context_5");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(0, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for hobbies");
        graphModel.resetFilters();
    }

    @Test
    void UserStory19T() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("context_6");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(0, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for travel");
        graphModel.resetFilters();
    }

    @Test
    void UserStory19SM() {
        GraphModel graphModel = new GraphModel(model, "Impression", "Date", "Impression", 0, false);
        //create checkbox array list and pass that into graph model
        ArrayList<CheckBox> ch = new ArrayList<>();
        CheckBox checkBox = new CheckBox();
        checkBox.setId("context_3");
        checkBox.setSelected(true);
        ch.add(checkBox);
        graphModel.updateSegmentFilters(ch);
        assertEquals(138095, graphModel.getData().values().stream().mapToDouble(d -> d).sum(), "Impressions filtered for social media");
        graphModel.resetFilters();
    }
}
