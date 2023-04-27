package view.scenes;

import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.w3c.dom.Text;

public class BounceDef extends AbstractScene {
    private RadioButton timeRadio;
    private RadioButton pageRadio;
    private TextField inputPageText;
    private TextField inputTimeText;
    private Button backButton;
    private Button resetButton;
    private Button applyButton;
    private ToggleGroup toggleGroup;

    public BounceDef() {
        super();
        createScene();
    }

    /**
     * Creates all the components of the scene, and adds them to the layout
     * Scene which contains settings to change the software visually
     */
    @Override
    public void createScene() {
        BorderPane bounceDefPane = new BorderPane();

        var titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        var titleLabel = new Label("Bounce");
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.getStyleClass().add("title");
        titleBox.getChildren().add(titleLabel);
        bounceDefPane.setTop(titleBox);

        //Pages viewed and time spent radio buttons
        toggleGroup = new ToggleGroup();

        pageRadio = new RadioButton("Pages Viewed:");
        pageRadio.setToggleGroup(toggleGroup);
        pageRadio.getStyleClass().add("label");
        inputPageText = new TextField();
        inputPageText.setAlignment(Pos.CENTER);
        HBox pageBox = new HBox(pageRadio, inputPageText);
        pageRadio.maxWidthProperty().bind(pageBox.widthProperty().multiply(0.8));
        inputPageText.maxWidthProperty().bind(pageBox.widthProperty().multiply(0.15));
        pageBox.setAlignment(Pos.CENTER);
        pageBox.setSpacing(10);

        timeRadio = new RadioButton("Time Spent:");
        timeRadio.setToggleGroup(toggleGroup);
        timeRadio.getStyleClass().add("label");
        inputTimeText = new TextField();
        inputTimeText.setAlignment(Pos.CENTER);
        HBox timeBox = new HBox(timeRadio, inputTimeText);
        timeRadio.maxWidthProperty().bind(timeBox.widthProperty().multiply(0.8));
        inputTimeText.maxWidthProperty().bind(pageBox.widthProperty().multiply(0.15));
        timeBox.setAlignment(Pos.CENTER);
        timeBox.setSpacing(10);

        resetButton = new Button("Reset");
        applyButton = new Button("Apply");
        HBox buttons = new HBox(applyButton, resetButton);
        buttons.setSpacing(20);
        buttons.setPadding(new Insets(10, 0, 0, 0));
        buttons.setAlignment(Pos.CENTER);

        VBox controls = new VBox(pageBox, timeBox, buttons);
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(20);
        controls.setMinWidth(200);
        controls.maxWidthProperty().bind(bounceDefPane.widthProperty().multiply(0.33));
        bounceDefPane.setCenter(controls);

        backButton = new Button("Back");
        bounceDefPane.setBottom(backButton);
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        BorderPane.setMargin(backButton, new Insets(20, 0, 10, 10));

        Scene bounceDefScene = new Scene(bounceDefPane, 1280, 720);
        bounceDefScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/view/bounceDef.css")).toExternalForm());
        setScene(bounceDefScene);
    }

    public Button getBackButton() {
        return backButton;
    }

    public Button getResetButton() {
        return resetButton;
    }

    public Button getApplyButton() {
        return applyButton;
    }

    public ToggleGroup getToggleGroup() {
        return toggleGroup;
    }

    public RadioButton getTimeRadio() {
        return timeRadio;
    }

    public RadioButton getPageRadio() {
        return pageRadio;
    }

    public TextField getInputPageText() {
        return inputPageText;
    }

    public TextField getInputTimeText() {
        return inputTimeText;
    }
}
