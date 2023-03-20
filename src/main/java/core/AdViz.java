package core;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.scenes.StartMenu;

/**
 * The main class of the application.
 */
public class AdViz extends Application {

  private static final Logger logger = LogManager.getLogger(AdViz.class);
    private final Model theModel;
    private final Controller theController;

    public AdViz() {

      logger.info("Starting AdViz");
        this.theModel = new Model();
//        this.theView = new AppView();
        this.theController = new Controller(theModel);
//        this.theView.setController(theController);
    }

    /**
     * The main method of the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * The start method of the application.
     * @param stage the stage of the application
     * @throws Exception if an exception occurs
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("AdViz");
        StartMenu sm = new StartMenu();
        theController.setStage(stage);
        theController.setUpScene(sm);
//        this.theController.setHandler((StartMenu) theView.getCurrentScene());
    }
}
