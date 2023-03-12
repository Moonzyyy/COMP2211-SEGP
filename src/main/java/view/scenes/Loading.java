package view.scenes;

import java.util.Objects;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Loading extends AbstractScene {

  public Loading() {
    super();
  }

  public void createScene() {
    var labelContainer = new VBox();
    Label loadingLabel = new Label("Loading...");
    loadingLabel.getStyleClass().add("title");
    labelContainer.getChildren().add(loadingLabel);
    labelContainer.setAlignment(Pos.CENTER);

    var loadingBorderPane = new BorderPane();
    loadingBorderPane.setCenter(labelContainer);
    scene = new Scene(loadingBorderPane, 1280, 720);
    scene.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource("/view/start.css")).toExternalForm());

  }

}
