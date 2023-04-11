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

public class BounceDef extends AbstractScene {

    private Button backButton;
    private Button applyButton;

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
        ToggleGroup toggleGroup = new ToggleGroup();

        RadioButton pageRadio = new RadioButton("Pages Viewed:");
        pageRadio.setToggleGroup(toggleGroup);
        pageRadio.getStyleClass().add("label");
        TextField inputPageText = new TextField();
        VBox pageBox = new VBox(pageRadio, inputPageText);
        pageBox.setAlignment(Pos.CENTER);
        pageBox.setSpacing(10);

        RadioButton timeRadio = new RadioButton("Time Spent:");
        timeRadio.setToggleGroup(toggleGroup);
        timeRadio.getStyleClass().add("label");
        TextField inputTimeText = new TextField();
        VBox timeBox = new VBox(timeRadio, inputTimeText);
        timeBox.setAlignment(Pos.CENTER);
        timeBox.setSpacing(10);

        applyButton = new Button("Apply");


        VBox controls = new VBox(pageBox, timeBox, applyButton);
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
}
