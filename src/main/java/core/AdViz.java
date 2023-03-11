package core;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import one.microstream.afs.nio.types.NioFileSystem;
import one.microstream.storage.embedded.types.EmbeddedStorage;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;
import view.scenes.StartMenu;

/**
 * The main class of the application.
 */
public class AdViz extends Application {
    private final Model theModel;
    private final Controller theController;

    public AdViz() {
        this.theModel = new Model();
//        this.theView = new AppView();
        this.theController = new Controller(theModel);
        this.theController.storageManager();
//        this.theView.setController(theController);
        theModel.importData();
    }

    /**
     * The main method of the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AdViz app = new AdViz();
//        launch();
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
