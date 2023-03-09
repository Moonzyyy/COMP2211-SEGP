package view.scenes;

import java.util.Objects;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class Loading extends AbstractScene {

  public Loading() {
    super();
  }

  public void createScene() {
    Label loadingLabel = new Label("Loading...");
    loadingLabel.getStyleClass().add("title");

    var loadingBorderPane = new BorderPane();
    loadingBorderPane.setCenter(loadingLabel);
    scene = new Scene(loadingBorderPane, 1280, 720);
    scene.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/view/start.css")).toExternalForm());

  }

}
