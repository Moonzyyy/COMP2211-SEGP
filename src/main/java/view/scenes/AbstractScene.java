package view.scenes;

import javafx.scene.Scene;

/**
 * Abstract class for all scenes in the application.
 */
abstract public class AbstractScene {

  protected Scene scene;

  AbstractScene() {
  }

  public Scene getScene() {
    return scene;
  }

  public void setScene(Scene scene) {
    this.scene = scene;
  }

  abstract void createScene();

}
