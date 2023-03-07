package view.scenes;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class StartMenu extends AbstractScene {

  private Button importButton;
  private Button settingsButton;

  public StartMenu() {
    super();
//    createScene();

  }

  /**
   * Creates the scene for the start menu.
   */
  public void createScene() {
    importButton = new Button("Import");
    importButton.getStyleClass().add("startButton");

    settingsButton = new Button("Settings");
    settingsButton.getStyleClass().add("startButton");

    var title = new Label("AdViz");
    title.getStyleClass().add("title");

    var startTitle = new VBox();
    startTitle.setAlignment(Pos.CENTER);
    startTitle.getChildren().add(title);

    var startButtonsVBox = new VBox();
    startButtonsVBox.getChildren().addAll(importButton, settingsButton);
    startButtonsVBox.setPrefSize(200, 200);
    startButtonsVBox.setSpacing(20);
    startButtonsVBox.setAlignment(Pos.CENTER);

    var startBorderPane = new BorderPane();
    startBorderPane.setTop(startTitle);
    startBorderPane.setCenter(startButtonsVBox);

    scene = new Scene(startBorderPane, 1280, 720);
    scene.getStylesheets().add(getClass().getResource("/view/start.css").toExternalForm());
  }

  public Button getImportButton() {
    return this.importButton;
  }

  public Button getSettingsButton() {
    return this.settingsButton;
  }
}