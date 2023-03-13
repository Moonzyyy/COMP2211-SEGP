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
import javafx.scene.text.Text;
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

  private Text clickFileName;
  private Text impressionFileName;
  private Text serverFileName;

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
    clickFileName = new Text("Null");
    clickFileName.getStyleClass().add("fileNameText");
    var importClicksTooltip = new Tooltip("Should be of the form click_log.csv");
    importBoxFactory(clickDataBox, clickDataLabel, importClicksTooltip, importClicks, clickFileName);

    VBox impressionDataBox = new VBox();
    var impressionDataLabel = new Label("Impression Data");
    impressionDataLabel.getStyleClass().add("text");
    importImpressions = new Button("Import");
    impressionFileName = new Text("Null");
    impressionFileName.getStyleClass().add("fileNameText");
    var importImpressionsTooltip = new Tooltip("Should be of the form impression_log.csv");
    importBoxFactory(impressionDataBox, impressionDataLabel, importImpressionsTooltip, importImpressions, impressionFileName);

    VBox serverDataBox = new VBox();
    var serverDataLabel = new Label("Server Data");
    serverDataLabel.getStyleClass().add("text");
    importServer = new Button("Import");
    serverFileName = new Text("Null");
    serverFileName.getStyleClass().add("fileNameText");
    var importServerTooltip = new Tooltip("Should be of the form server_log.csv");
    importBoxFactory(serverDataBox, serverDataLabel, importServerTooltip, importServer, serverFileName);

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
      Tooltip importClicksTooltip, Button importClicks, Text fileName) {
    importClicksTooltip.setShowDelay(javafx.util.Duration.millis(500));
    importClicksTooltip.setHideDelay(javafx.util.Duration.millis(100));
    importClicks.setTooltip(importClicksTooltip);
    clickDataBox.getChildren().addAll(clickDataLabel, importClicks, fileName);
    importClicks.getStyleClass().add("importButton");
    clickDataBox.getStyleClass().add("importBox");

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

  public Text getClickFileName() {return clickFileName;}

  public Text getImpressionFileName() {return impressionFileName;}

  public Text getServerFileName() {return serverFileName;}

}
