package view.scenes;

import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;



public class Settings extends AbstractScene {

  private Button backButton;

  public Settings() {
    super();
    createScene();
  }

  @Override
  public void createScene() {
    BorderPane settingsPane = new BorderPane();

    var titleBox = new HBox();
    titleBox.setAlignment(Pos.CENTER);
    var titleLabel = new Label("Settings");
    HBox.setHgrow(titleLabel, Priority.ALWAYS);
    titleLabel.getStyleClass().add("title");
    titleBox.getChildren().add(titleLabel);
    settingsPane.setTop(titleBox);

    Label fontLabel = new Label("Text Font:");
    fontLabel.getStyleClass().add("label");
    ComboBox<String> fontDropdown = new ComboBox<>();
    fontDropdown.getItems().addAll("Arial", "Times New Roman", "Verdana");
    fontDropdown.getStyleClass().add("combo-box");
    HBox fontBox = new HBox(fontLabel, fontDropdown);
    fontBox.setAlignment(Pos.CENTER);
    fontBox.setSpacing(10);

    Label sizeLabel = new Label("Text Size:");
    sizeLabel.getStyleClass().add("label");
    HBox sizeLabelBox = new HBox(sizeLabel);
    sizeLabelBox.setAlignment(Pos.CENTER);
    sizeLabelBox.setSpacing(10);

    Button decreaseButton = new Button("-");
    decreaseButton.setOnAction(e -> {
      // handle decreasing font size
    });
    decreaseButton.getStyleClass().add("button");

    Button increaseButton = new Button("+");
    increaseButton.setOnAction(e -> {
      // handle increasing font size
    });
    increaseButton.getStyleClass().add("button");

    Label filler = new Label(". . . . . . .");
    filler.getStyleClass().add("label");

    HBox sizeBox = new HBox(sizeLabelBox, decreaseButton, filler, increaseButton);
    sizeBox.setAlignment(Pos.CENTER);
    sizeBox.setSpacing(10);

    Label themeLabel = new Label("Colour Scheme:");
    themeLabel.getStyleClass().add("label");
    ComboBox<String> themeDropdown = new ComboBox<>();
    themeDropdown.getItems().addAll("Light Mode", "Dark Mode");
    themeDropdown.getStyleClass().add("combo-box");
    HBox themeBox = new HBox(themeLabel, themeDropdown);
    themeBox.setAlignment(Pos.CENTER);
    themeBox.setSpacing(10);

    VBox controls = new VBox(fontBox, sizeBox, themeBox);
    controls.setAlignment(Pos.CENTER);
    controls.setSpacing(20);
    settingsPane.setCenter(controls);

    backButton = new Button("<");
    backButton.getStyleClass().add("backButton");
    settingsPane.setBottom(backButton);
    BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
    BorderPane.setMargin(backButton, new Insets(20, 0, 10, 10));

    Scene settingsScene = new Scene(settingsPane, 1280, 720);
    settingsScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/view/settings.css")).toExternalForm());
    setScene(settingsScene);
  }

  public Button getBackButton() {
    return backButton;
  }
}
