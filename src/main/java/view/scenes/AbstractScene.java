package view.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.AppView;

abstract public class AbstractScene {
    protected final AppView view;
    protected Scene scene;
    protected final Stage stage;

    AbstractScene(Stage stage, AppView view) {
        this.view = view;
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Stage getStage() {
        return stage;
    }

    public AppView getView() {
        return view;
    }

    abstract void createScene();

}
