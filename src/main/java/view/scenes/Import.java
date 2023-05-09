package view.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
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

    /**
     * Creates all the components of the scene, and adds them to the layout
     * Import scene with 3 buttons that allow adding log files
     * Once a log file has been added it is shown
     */
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
        clickFileName = new Text("");
        clickFileName.getStyleClass().add("fileNameText");
        var importClicksTooltip = new Tooltip("Should be of the form click_log.csv");
        importBoxFactory(clickDataBox, clickDataLabel, importClicksTooltip, importClicks, clickFileName);

        VBox impressionDataBox = new VBox();
        var impressionDataLabel = new Label("Impression Data");
        impressionDataLabel.getStyleClass().add("text");
        importImpressions = new Button("Import");
        impressionFileName = new Text("");
        impressionFileName.getStyleClass().add("fileNameText");
        var importImpressionsTooltip = new Tooltip("Should be of the form impression_log.csv");
        importBoxFactory(impressionDataBox, impressionDataLabel, importImpressionsTooltip, importImpressions, impressionFileName);

        VBox serverDataBox = new VBox();
        var serverDataLabel = new Label("Server Data");
        serverDataLabel.getStyleClass().add("text");
        importServer = new Button("Import");
        serverFileName = new Text("");
        serverFileName.getStyleClass().add("fileNameText");
        var importServerTooltip = new Tooltip("Should be of the form server_log.csv");
        importBoxFactory(serverDataBox, serverDataLabel, importServerTooltip, importServer, serverFileName);

        centreBox.getChildren().addAll(clickDataBox, impressionDataBox, serverDataBox);
        centreBox.setSpacing(20);

        var bottomBar = new HBox();

        loadButton = new Button("Load");

        backButton = new Button("Back");
//    backButton.getStyleClass().add("backButton");

        var spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        bottomBar.getChildren().addAll(backButton, spacer, loadButton);
        bottomBar.setPadding(new Insets(20));
        layout.setBottom(bottomBar);

        fileChooser = new FileChooser();
        fileChooser.setTitle("Import Data");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        scene = new Scene(layout, 1280, 720);
//    scene.getStylesheets().add(getClass().getResource("/view/import.css").toExternalForm());
    }

    /**
     * Creates the box for the import buttons
     *
     * @param clickDataBox        - the box to add the elements to
     * @param clickDataLabel      - the label for the box
     * @param importClicksTooltip - the tooltip for the import button
     * @param importClicks        - the import button
     * @param fileName            - the text to show the file name
     */
    private void importBoxFactory(VBox clickDataBox, Label clickDataLabel,
                                  Tooltip importClicksTooltip, Button importClicks, Text fileName) {
        importClicksTooltip.setShowDelay(javafx.util.Duration.millis(500));
        importClicksTooltip.setHideDelay(javafx.util.Duration.millis(100));
        importClicks.setTooltip(importClicksTooltip);
        clickDataBox.getChildren().addAll(clickDataLabel, importClicks, fileName);
//    importClicks.getStyleClass().add("importButton");
        clickDataBox.getStyleClass().add("importBox");
    }

    /**
     * @return get back button
     */
    public Button getBackButton() {
        return backButton;
    }

    /**
     * @return get the button for import Clicks
     */
    public Button getImportClicks() {
        return importClicks;
    }

    /**
     * @return get the button for importing Impressions
     */
    public Button getImportImpressions() {
        return importImpressions;
    }


    /**
     * @return get the button for importing Server Interactions
     */
    public Button getImportServer() {
        return importServer;
    }

    /**
     * @return get the file chooser
     */
    public FileChooser getFileChooser() {
        return fileChooser;
    }

    /**
     * @return get the load button
     */
    public Button getLoadButton() {
        return loadButton;
    }

    /**
     * @return get the text containing the clicks file names
     */
    public Text getClickFileName() {
        return clickFileName;
    }

    /**
     * @return get the text containing the impressions file name
     */
    public Text getImpressionFileName() {
        return impressionFileName;
    }

    /**
     * @return get the text containing the server interactions file name
     */
    public Text getServerFileName() {
        return serverFileName;
    }

    public void setStyles(boolean theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/view/import.css").toExternalForm());
//        if (theme) {
//            scene.getStylesheets().add(getClass().getResource("/view/import.css").toExternalForm());
//        } else {
//            scene.getStylesheets().add(getClass().getResource("/view/importLight.css").toExternalForm());
//        }
    }

}
