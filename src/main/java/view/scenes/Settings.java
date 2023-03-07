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

    Label titleLabel = new Label("Settings");
    titleLabel.setStyle("-fx-text-fill: white;");
    var titleBox = new HBox(titleLabel);
    titleBox.getStyleClass().add("title");
    titleBox.setAlignment(Pos.CENTER);
    settingsPane.setTop(titleBox);

    Label fontLabel = new Label("Text Font:");
    fontLabel.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
    ComboBox<String> fontDropdown = new ComboBox<>();
    fontDropdown.getItems().addAll("Arial", "Times New Roman", "Verdana");
    fontDropdown.setStyle("-fx-font-size: 18px;");
    HBox fontBox = new HBox(fontLabel, fontDropdown);
    fontBox.setAlignment(Pos.CENTER);
    fontBox.setSpacing(10);

    Label sizeLabel = new Label("Text Size:");
    sizeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
    HBox sizeLabelBox = new HBox(sizeLabel);
    sizeLabelBox.setAlignment(Pos.CENTER);
    sizeLabelBox.setSpacing(10);

    Button decreaseButton = new Button("-");
    decreaseButton.setStyle("-fx-font-size: 25px;");
    decreaseButton.setOnAction(e -> {
      // handle decreasing font size
    });
    decreaseButton.getStyleClass().add("button");
    decreaseButton.setStyle("-fx-font-size: 18px;");

    Button increaseButton = new Button("+");
    increaseButton.setStyle("-fx-font-size: 25px;");
    increaseButton.setOnAction(e -> {
      // handle increasing font size
    });
    increaseButton.getStyleClass().add("button");
    increaseButton.setStyle("-fx-font-size: 18px;");

    Label filler = new Label(". . . . . . .");
    sizeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");

    HBox sizeBox = new HBox(sizeLabelBox, decreaseButton, filler, increaseButton);
    sizeBox.setAlignment(Pos.CENTER);
    sizeBox.setSpacing(10);

    Label themeLabel = new Label("Colour Scheme:");
    themeLabel.setStyle("-fx-text-fill: white; -fx-font-size: 25px;");
    ComboBox<String> themeDropdown = new ComboBox<>();
    themeDropdown.getItems().addAll("Light Mode", "Dark Mode");
    themeDropdown.setStyle("-fx-font-size: 18px;");
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
    settingsScene.getStylesheets()
        .add(Objects.requireNonNull(getClass().getResource("/view/settings.css")).toExternalForm());
    setScene(settingsScene);
  }

  public Button getBackButton() {
    return backButton;
  }
}
