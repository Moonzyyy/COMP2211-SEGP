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

    private Button resumeButton;

    private Button faqButton;

    public StartMenu() {
        super();

    }

    /**
     * Creates the scene for the start menu.
     */
    public void createScene() {
        importButton = new Button("Import");
        importButton.getStyleClass().add("startButton");

        settingsButton = new Button("Settings");
        settingsButton.getStyleClass().add("startButton");

        resumeButton = new Button("Resume");
        resumeButton.getStyleClass().add("startButton");

        faqButton = new Button("FAQ");
        faqButton.getStyleClass().add("startButton");

        var title = new Label("AdViz");
        title.getStyleClass().add("title");

        var startTitle = new VBox();
        startTitle.setAlignment(Pos.CENTER);
        startTitle.getChildren().add(title);

        var startButtonsVBox = new VBox();
        startButtonsVBox.getChildren().addAll(importButton, settingsButton, faqButton, resumeButton);
        startButtonsVBox.setPrefSize(200, 200);
        startButtonsVBox.setSpacing(20);
        startButtonsVBox.setAlignment(Pos.CENTER);

        var startBorderPane = new BorderPane();
        startBorderPane.setTop(startTitle);
        startBorderPane.setCenter(startButtonsVBox);

        startBorderPane.getStyleClass().add("light");
        scene = new Scene(startBorderPane, 1280, 720);

        scene.getStylesheets().add(getClass().getResource("/view/startLight.css").toExternalForm());
    }

    /**
     * @return get the Import button
     */
    public Button getImportButton() {
        return this.importButton;
    }

    /**
     * @return get the Settings button
     */
    public Button getSettingsButton() {
        return this.settingsButton;
    }

    /**
     * @return get the Resume Button
     */
    public Button getResumeButton() {
        return this.resumeButton;
    }

    public Button getFaqButton() {return this.faqButton;}

    public void setStyles(boolean theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/view/start.css").toExternalForm());
//        if (theme) {
//            scene.getStylesheets().add(getClass().getResource("/view/start.css").toExternalForm());
//        } else {
//            scene.getStylesheets().add(getClass().getResource("/view/startLight.css").toExternalForm());
//        }
    }
}