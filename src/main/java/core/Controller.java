package core;

import java.awt.event.ActionListener;
import java.util.EventListener;
import javafx.event.ActionEvent;
//import listeners.MetricListener;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import model.Model;
import view.scenes.AbstractScene;
import view.scenes.Dashboard;
import view.scenes.Graph;
import view.scenes.Settings;
import view.scenes.StartMenu;

public class Controller {
    private final Model model;
    private AbstractScene currentScene;
    private Stage stage;

    public Controller(Model model) {
        this.model = model;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void setCurrentScene(AbstractScene newScene) {
        stage.setScene(newScene.getScene());
        this.currentScene = newScene;
        stage.show();
    }

    public void setUpScene(StartMenu menu)
    {
        menu.createScene();
        this.setCurrentScene(menu);
        menu.getImportButton().setOnAction((event) -> {
            model.importData();
            setUpScene(new Dashboard());
        });
    }

    public void setUpScene(Dashboard dashboard)
    {
        dashboard.createScene();
        this.setCurrentScene(dashboard);
        //Do the button shit
    }

    public void setUpScene(Settings menu)
    {

    }

    public void setUpScene(Graph graphScene)
    {

    }




}
