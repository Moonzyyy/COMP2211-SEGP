package core;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.Model;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import view.scenes.StartMenu;

import java.awt.*;
import java.util.Objects;

/**
 * The main class of the application.
 */
public class AdViz extends Application {

    private static final Logger logger = LogManager.getLogger(AdViz.class);
    private final Model theModel;
    private final Controller theController;

    /**
     * Creates model and controller class, passing the model class through controller.
     */
    public AdViz() {
        logger.info("Starting AdViz");
        this.theModel = new Model();
        this.theController = new Controller(theModel);

    }

    /**
     * The main method of the application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * The start method of the application.
     *
     * @param stage the stage of the application
     * @throws Exception if an exception occurs
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("AdViz");
        if (Taskbar.isTaskbarSupported()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                final Toolkit toolkit = Toolkit.getDefaultToolkit();
                var dockIcon = toolkit.getImage(getClass().getResource("/images/logo--dark.png"));
                taskbar.setIconImage(dockIcon);
            }
        } else {
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo--dark.png"))));
        }
        StartMenu sm = new StartMenu();
        theController.setStage(stage);
        theController.setUpScene(sm);
        stage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
