package core;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.scenes.StartMenu;

public class AdViz extends Application {
    private final Model theModel;
    private final Controller theController;

    public AdViz() {
        this.theModel = new Model();
//        this.theView = new AppView();
        this.theController = new Controller(theModel);
//        this.theView.setController(theController);
    }

    public static void main(String[] args) {
        AdViz app = new AdViz();
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("AdViz");
        StartMenu sm = new StartMenu();
        theController.setStage(stage);
        theController.setUpScene(sm);
//        this.theController.setHandler((StartMenu) theView.getCurrentScene());
    }
}
