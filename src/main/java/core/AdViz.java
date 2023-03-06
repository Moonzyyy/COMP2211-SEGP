package core;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.AppView;
import view.scenes.StartMenu;

public class AdViz extends Application {
    private AppView theView ;
    private Model theModel;
    private Controller theController;

    public AdViz() {
        this.theModel = new Model();
        this.theView = new AppView();
        this.theController = new Controller(theView, theModel);
        this.theView.setController(theController);
    }

    public static void main(String[] args) {
        AdViz app = new AdViz();
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("AdViz");
        StartMenu sm = new StartMenu(stage, theView);
        stage.setScene(sm.getScene());
        this.theView.setSm(sm);
        this.theModel.importData();
        stage.show();
    }
}
