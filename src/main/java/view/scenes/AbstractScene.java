package view.scenes;

import javafx.scene.Scene;
import javafx.stage.Stage;
import view.AppView;

/**
 * Abstract class for all scenes in the application.
 */
abstract public class AbstractScene {

  protected final AppView view;
  protected final Stage stage;
  protected Scene scene;

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
