package view.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.scenes.Dashboard;

public class StartMenu {

  private final Stage stage;
  private Scene scene;

  public StartMenu(Stage stage) {
    this.stage = stage;
    createScene();
  }

  public Scene getScene() {
    return scene;
  }

  private void createScene() {
    Button startButton = new Button("Import");
    startButton.setOnAction(e -> {
      System.out.println("Import button clicked");
      stage.setScene(new Dashboard(stage).getScene());
    });
    startButton.getStyleClass().add("startButton");

    Button settingsButton = new Button("Settings");
    settingsButton.setOnAction(e -> {
      System.out.println("Settings button clicked");
    });
    settingsButton.getStyleClass().add("startButton");

    var title = new Label("AdViz");
    title.getStyleClass().add("title");

    var startTitle = new VBox();
    startTitle.setAlignment(Pos.CENTER);
    startTitle.getChildren().add(title);

    var startButtonsVBox = new VBox();
    startButtonsVBox.getChildren().addAll(startButton, settingsButton);
    startButtonsVBox.setPrefSize(200, 200);
    startButtonsVBox.setSpacing(20);
    startButtonsVBox.setAlignment(Pos.CENTER);

    var startBorderPane = new BorderPane();
    startBorderPane.setTop(startTitle);
    startBorderPane.setCenter(startButtonsVBox);

    scene = new Scene(startBorderPane, 800, 600);
    scene.getStylesheets().add(getClass().getResource("/view/start.css").toExternalForm());
  }
}