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
import javafx.scene.layout.VBox;
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

    var clickDataBox = new VBox();
    var clickDataLabel = new Label("Click Data");
    VBox.setVgrow(clickDataBox, Priority.NEVER);
    clickDataLabel.getStyleClass().add("text");
    importClicks = new Button("Import");
    importClicks.getStyleClass().add("importButton");
    clickDataBox.getStyleClass().add("importBox");
    clickDataBox.getChildren().addAll(clickDataLabel, importClicks);

    var impressionDataBox = new VBox();
    var impressionDataLabel = new Label("Impression Data");
    impressionDataLabel.getStyleClass().add("text");
    importImpressions = new Button("Import");
    impressionDataBox.getChildren().addAll(impressionDataLabel, importImpressions);
    importImpressions.getStyleClass().add("importButton");
    impressionDataBox.getStyleClass().add("importBox");

    var serverDataBox = new VBox();
    var serverDataLabel = new Label("Server Data");
    serverDataLabel.getStyleClass().add("text");
    importServer = new Button("Import");
    serverDataBox.getChildren().addAll(serverDataLabel, importServer);
    importServer.getStyleClass().add("importButton");
    serverDataBox.getStyleClass().add("importBox");

    centreBox.getChildren().addAll(clickDataBox, impressionDataBox, serverDataBox);
    centreBox.setSpacing(20);

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
