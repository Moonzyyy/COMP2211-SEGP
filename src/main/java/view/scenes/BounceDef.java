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
        VBox pageBox = new VBox(pageRadio, inputPageText);
        pageBox.setAlignment(Pos.CENTER);
        pageBox.setSpacing(10);

        timeRadio = new RadioButton("Time Spent:");
        timeRadio.setToggleGroup(toggleGroup);
        timeRadio.getStyleClass().add("label");
        inputTimeText = new TextField();
        VBox timeBox = new VBox(timeRadio, inputTimeText);
        timeBox.setAlignment(Pos.CENTER);
        timeBox.setSpacing(10);

        resetButton = new Button("Reset");
        resetButton.getStyleClass().add("backButton");
        applyButton = new Button("Apply");
        applyButton.getStyleClass().add("backButton");

        VBox controls = new VBox(resetButton, pageBox, timeBox, applyButton);
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(20);
        bounceDefPane.setCenter(controls);

        backButton = new Button("Back");
        backButton.getStyleClass().add("backButton");
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

    public ToggleGroup getToggleGroup() { return toggleGroup; }

    public RadioButton getTimeRadio() {
        return timeRadio;
    }

    public RadioButton getPageRadio() {
        return pageRadio;
    }

    public TextField getInputPageText() { return inputPageText; }

    public TextField getInputTimeText() { return inputTimeText; }
}
