package view.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.AppView;

import java.util.Objects;


public class Settings extends AbstractScene{
    Settings(Stage stage, AppView view) {
        super(stage, view);
        createScene();
    }

    @Override
    void createScene() {
        BorderPane settingsPane = new BorderPane();

        // Create a label for the page title
        Label titleLabel = new Label("Settings");
        titleLabel.setStyle("-fx-text-fill: white;");
        var titleBox = new HBox(titleLabel);
        titleBox.getStyleClass().add("title");
        titleBox.setAlignment(Pos.CENTER);
        settingsPane.setTop(titleBox);

        // Create a dropdown menu for the text font
        Label fontLabel = new Label("Text Font:");
        fontLabel.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
        ComboBox<String> fontDropdown = new ComboBox<>();
        fontDropdown.getItems().addAll("Arial", "Times New Roman", "Verdana");
        fontDropdown.setStyle("-fx-font-size: 18px;");
        HBox fontBox = new HBox(fontLabel, fontDropdown);
        fontBox.setAlignment(Pos.CENTER);
        fontBox.setSpacing(10);

        // Create a label for the text size
        Label sizeLabel = new Label("Text Size:");
        sizeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
        HBox sizeLabelBox = new HBox(sizeLabel);
        sizeLabelBox.setAlignment(Pos.CENTER);
        sizeLabelBox.setSpacing(10);

        // Create a button for decreasing font size
        Button decreaseButton = new Button("-");
        decreaseButton.setStyle("-fx-font-size: 25px;");
        decreaseButton.setOnAction(e -> {
            // handle decreasing font size
        });
        decreaseButton.getStyleClass().add("button");
        decreaseButton.setStyle("-fx-font-size: 18px;");

        // Create a button for increasing font size
        Button increaseButton = new Button("+");
        increaseButton.setStyle("-fx-font-size: 25px;");
        increaseButton.setOnAction(e -> {
            // handle increasing font size
        });
        increaseButton.getStyleClass().add("button");
        increaseButton.setStyle("-fx-font-size: 18px;");

        Label filler = new Label(". . . . . . .");
        sizeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");

        HBox sizeBox = new HBox(sizeLabelBox, decreaseButton, filler,increaseButton);
        sizeBox.setAlignment(Pos.CENTER);
        sizeBox.setSpacing(10);

        // Create a dropdown menu for the colour scheme
        Label themeLabel = new Label("Colour Scheme:");
        themeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
        ComboBox<String> themeDropdown = new ComboBox<>();
        themeDropdown.getItems().addAll("Light Mode", "Dark Mode");
        themeDropdown.setStyle("-fx-font-size: 18px;");
        HBox themeBox = new HBox(themeLabel, themeDropdown);
        themeBox.setAlignment(Pos.CENTER);
        themeBox.setSpacing(10);

        // Create a VBox to align the controls vertically
        VBox controls = new VBox(fontBox, sizeBox, themeBox);
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(20);
        settingsPane.setCenter(controls);

        // Create a button for the bottom left corner
        Button backButton = new Button("<");
        backButton.setOnAction(e -> {
            stage.setScene(new StartMenu(stage, getView()).getScene());
        });
        backButton.setPrefSize(40,40);
        backButton.getStyleClass().add("backButton");
        settingsPane.setBottom(backButton);
        BorderPane.setMargin(backButton, new Insets(0, 0, 10, 10));

        Scene settingsScene = new Scene(settingsPane, 800, 600);
        settingsScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/view/settings.css")).toExternalForm());
        setScene(settingsScene);
    }
}
