package view.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
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
    clickDataLabel.getStyleClass().add("text");
    importClicks = new Button("Import");
    var importClicksTooltip = new Tooltip("Should be of the form click_log.csv");
    importBoxFactory(clickDataBox, clickDataLabel, importClicksTooltip, importClicks);

    VBox impressionDataBox = new VBox();
    var impressionDataLabel = new Label("Impression Data");
    impressionDataLabel.getStyleClass().add("text");
    importImpressions = new Button("Import");
    var importImpressionsTooltip = new Tooltip("Should be of the form impression_log.csv");
    importBoxFactory(impressionDataBox, impressionDataLabel, importImpressionsTooltip,
        importImpressions);

    VBox serverDataBox = new VBox();
    var serverDataLabel = new Label("Server Data");
    serverDataLabel.getStyleClass().add("text");
    importServer = new Button("Import");
    var importServerTooltip = new Tooltip("Should be of the form server_log.csv");
    importBoxFactory(serverDataBox, serverDataLabel, importServerTooltip, importServer);

    centreBox.getChildren().addAll(clickDataBox, impressionDataBox, serverDataBox);
    centreBox.setSpacing(20);

    var bottomBar = new HBox();

    loadButton = new Button("Load");
    loadButton.getStyleClass().add("button");


    backButton = new Button("<");
    backButton.getStyleClass().add("backButton");

    var spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    bottomBar.getChildren().addAll(backButton, spacer, loadButton);
    bottomBar.setPadding(new Insets(20));
    layout.setBottom(bottomBar);

    fileChooser = new FileChooser();
    fileChooser.setTitle("Import Data");
    fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

    scene = new Scene(layout, 1280, 720);
    scene.getStylesheets().add(getClass().getResource("/view/import.css").toExternalForm());


  }

  private void importBoxFactory(VBox clickDataBox, Label clickDataLabel,
      Tooltip importClicksTooltip, Button importClicks) {
    importClicksTooltip.setShowDelay(javafx.util.Duration.millis(500));
    importClicksTooltip.setHideDelay(javafx.util.Duration.millis(100));
    importClicks.setTooltip(importClicksTooltip);
    clickDataBox.getChildren().addAll(clickDataLabel, importClicks);
    importClicks.getStyleClass().add("importButton");
    clickDataBox.getStyleClass().add("importBox");

    var impressionDataBox = new VBox();
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
