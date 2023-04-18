package view.scenes;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class Loading extends AbstractScene {


  public Loading() {
    super();
  }

  /**
   * Creates all the components of the scene, and adds them to the layout
   * Loading scene while waiting for log files to be parsed
   */
  public void createScene() {
    Label loadingLabel = new Label("Loading...");
    loadingLabel.getStyleClass().add("loading");

    var loadingPane = new StackPane();
    loadingPane.getChildren().add(loadingLabel);
    StackPane.setAlignment(loadingPane, Pos.CENTER);
    scene = new Scene(loadingPane, 1280, 720);
    scene.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/view/startLight.css")).toExternalForm());
  }
}
