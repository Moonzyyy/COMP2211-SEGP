package view.scenes;

import javafx.scene.Scene;

/**
 * Abstract class for all scenes in the application.
 */
abstract public class AbstractScene {

  protected Scene scene;

  AbstractScene() {
  }

  /**
   * @return get the scene
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * @param scene set the scene
   */
  public void setScene(Scene scene) {
    this.scene = scene;
  }

  /**
   * create the scene
   */
  abstract void createScene();

}
