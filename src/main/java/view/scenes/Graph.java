package view.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Graph {

  private final Stage stage;
  private final BorderPane layout;
  private Scene scene;

  public Graph(Stage stage) {
    this.stage = stage;
    layout = new BorderPane();
    createScene();
  }

  private void createScene() {
    scene = new Scene(layout, 800, 600);
    scene.getStylesheets().add(getClass().getResource("/view/graph.css").toExternalForm());

    var topBar = new HBox();
    topBar.setAlignment(Pos.BOTTOM_CENTER);
    topBar.getStyleClass().add("topBar");

    var homeButton = new Button("Home");
    homeButton.setOnAction(e -> {
      stage.setScene(new Dashboard(stage).getScene());
    });
    homeButton.getStyleClass().add("button");
    HBox.setHgrow(homeButton, Priority.ALWAYS);

    var graphTitle = new Label("AdViz - Graph");
    graphTitle.getStyleClass().add("graphTitle");
    HBox.setHgrow(graphTitle, Priority.ALWAYS);

    var printButton = new Button("Print");
    printButton.getStyleClass().add("button");
    HBox.setHgrow(printButton, Priority.ALWAYS);
    var compareButton = new Button("Compare");
    compareButton.getStyleClass().add("button");
    HBox.setHgrow(compareButton, Priority.ALWAYS);

    topBar.getChildren().addAll(homeButton, graphTitle, printButton, compareButton);
    layout.setTop(topBar);

    var graph = new Rectangle();
    graph.getStyleClass().add("graph");
    layout.setCenter(graph);


  }

  public Scene getScene() {
    return scene;
  }
}
