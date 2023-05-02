import core.Controller;
import model.GraphModel;
import model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DefectTests {
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

    @Test
    void bouncePagesText() {
        assertFalse(Controller.validBounceDef("text", true));
    }

    @Test
    void bounceTimeHighPos() {
        assertFalse(Controller.validBounceDef("9999", false));
    }

    @Test
    void bouncePagesHighNeg() {
        assertFalse(Controller.validBounceDef("-9999", true));
    }

    @Test
    void bounceTimeDigitThenText() {
        assertFalse(Controller.validBounceDef("1text", false));
    }
}
