package view.scenes;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Settings extends AbstractScene {

    ObjectProperty<javafx.scene.paint.Color> bgColor;

    private Button backButton;
    private Button applyButton;
    private ColorPicker bgColorPicker;
    private ComboBox<String> themeDropdown;

    private ComboBox<String> fontDropdown;

    public Settings() {
        super();
    }

    /**
     * Creates all the components of the scene, and adds them to the layout
     * Scene which contains settings to change the software visually
     */
    public void createScene() {


        BorderPane settingsPane = new BorderPane();

        var title = new Label("Settings");
        title.getStyleClass().add("title");

        var settingsTitle = new VBox();
        settingsTitle.setAlignment(Pos.CENTER);
        settingsTitle.getChildren().add(title);
        settingsPane.setTop(settingsTitle);

        Label fontLabel = new Label("Text Font:");
        fontDropdown = new ComboBox<>();
        fontDropdown.getItems().addAll("Roboto", "Arial", "Times New Roman", "Verdana");
        HBox fontBox = new HBox(fontLabel, fontDropdown);
        fontBox.setAlignment(Pos.CENTER);
        fontBox.setSpacing(10);

//        bgColorPicker = new ColorPicker();
//        bgColor = new SimpleObjectProperty<>();
//        Bindings.bindBidirectional(bgColor, bgColorPicker.valueProperty());
//        bgColor.addListener((observable, oldValue, newValue) -> {
//            settingsPane.setStyle("-fx-background-color: " + toRGBString(newValue));
//        });


        Label themeLabel = new Label("Colour Scheme:");
        themeDropdown = new ComboBox<>();
        themeDropdown.getItems().addAll("Light Mode", "Dark Mode");
        themeDropdown.getStyleClass().add("combo-box");
        HBox themeBox = new HBox(themeLabel, themeDropdown);
        themeBox.setAlignment(Pos.CENTER);
        themeBox.setSpacing(10);

        applyButton = new Button("Save Settings");

        VBox controls = new VBox(fontBox, themeBox , applyButton);
        controls.setAlignment(Pos.CENTER);
        controls.setPrefSize(200, 200);
        controls.setSpacing(20);
        settingsPane.setCenter(controls);

        backButton = new Button("Back");
        var bottomBar = new HBox();
        bottomBar.getChildren().add(backButton);
        bottomBar.setAlignment(Pos.BOTTOM_LEFT);
        bottomBar.setPadding(new Insets(20));
        settingsPane.setBottom(bottomBar);
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        BorderPane.setMargin(backButton, new Insets(20, 0, 10, 10));

        scene = new Scene(settingsPane, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/view/settings.css").toExternalForm());
//    setScene(settingsScene);
    }

    public Button getBackButton() {
        return backButton;
    }

    public ColorPicker getBgColorPicker() {
        return bgColorPicker;
    }

    private String toRGBString(javafx.scene.paint.Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    public ComboBox<String> getThemeDropdown() {
        return themeDropdown;
    }

    public ComboBox<String> getFontDropdown() {
        return fontDropdown;
    }

    public Button getApplyButton(){return applyButton; }

    public void setStyles(boolean theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/view/settings.css").toExternalForm());
//        if (theme) {
//            scene.getStylesheets().add(getClass().getResource("/view/settings.css").toExternalForm());
//        } else {
//            scene.getStylesheets().add(getClass().getResource("/view/settingsLight.css").toExternalForm());
//        }
    }
}
