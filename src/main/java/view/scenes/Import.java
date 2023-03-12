package view.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;

/**
 * Scene to walk user through importing data.
 */
public class Import extends AbstractScene {

  private final BorderPane layout;
  private Button backButton;
  private Button loadButton;
  private Button importClicks;
  private Button importImpressions;
  private Button importServer;

  private FileChooser fileChooser;

  public Import() {
    super();
    layout = new BorderPane();
  }

  public void createScene() {
    var importTitle = new HBox();
    var title = new Label("Import");
    importTitle.setAlignment(Pos.CENTER);
    title.getStyleClass().add("title");
    importTitle.getChildren().add(title);
    layout.setTop(importTitle);

    HBox centreBox = new HBox();
    centreBox.setAlignment(Pos.CENTER);
    layout.setCenter(centreBox);

    importClicks = new Button("Import Click Data");
    importClicks.getStyleClass().add("importButton");

    importImpressions = new Button("Import Impression Data");
    importImpressions.getStyleClass().add("importButton");

    importServer = new Button("Import Server Data");
    importServer.getStyleClass().add("importButton");

    centreBox.getChildren().addAll(importClicks, importImpressions, importServer);
    centreBox.setSpacing(20);
    centreBox.setAlignment(Pos.CENTER);

    var bottomBar = new HBox();

    loadButton = new Button("Load");
    loadButton.getStyleClass().add("button");
    loadButton.setVisible(false);

    backButton = new Button("<");
    backButton.getStyleClass().add("backButton");
    BorderPane.setMargin(backButton, new Insets(20, 0, 10, 10));
    BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);

    var spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    bottomBar.getChildren().addAll(backButton, spacer, loadButton);
    layout.setBottom(bottomBar);

    fileChooser = new FileChooser();
    fileChooser.setTitle("Import Data");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

    scene = new Scene(layout, 1280, 720);
    scene.getStylesheets().add(getClass().getResource("/view/import.css").toExternalForm());


  }

  public Button getBackButton() {
    return backButton;
  }

  public Button getImportClicks() {
    return importClicks;
  }

  public Button getImportImpressions() {
    return importImpressions;
  }

  public Button getImportServer() {
    return importServer;
  }

  public FileChooser getFileChooser() {
    return fileChooser;
  }

  public Button getLoadButton() {
    return loadButton;
  }


}
