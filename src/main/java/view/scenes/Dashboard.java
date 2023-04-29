package view.scenes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import view.components.DashboardComp;

/**
 * The dashboard scene.
 */
public class Dashboard extends AbstractScene {

    private final StackPane root;
    private final BorderPane layout;
    private final ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>(14);
    public Text bounceDefinition;
    private VBox menuBar;
    private boolean menuOpen = false;
    private DashboardComp dashboardComp;
    private Button backButton;
    private Button bounceDefButton;
    private ListView<Object> compareList;
    private CheckBox maleCheckBox;
    private CheckBox femaleCheckBox;
    private Button menuButton;
    private Label titleLabel;
    private Button filterButton;


    /**
     * The Dashboard constructor
     */
    public Dashboard() {
        super();
        root = new StackPane();
        root.setAlignment(Pos.BOTTOM_LEFT);
        layout = new BorderPane();
//    root.getChildren().add(layout);
  }

    /**
     * Creates all the components of the scene, and adds them to the layout.
     */
    public void createScene() {
        var titleBox = new HBox();
        titleBox.setAlignment(Pos.CENTER);
        titleLabel = new Label("Dashboard");
        titleLabel.getStyleClass().add("title");
        layout.setTop(titleBox);

        dashboardComp = new DashboardComp(this);
        dashboardComp.getStyleClass().add("dashboardComp");
        var inner = new BorderPane();
        inner.setCenter(dashboardComp);
        var wrapper = new StackPane(inner);
        layout.setCenter(wrapper);
        wrapper.setAlignment(Pos.BOTTOM_LEFT);


        backButton = new Button("Back");
        backButton.getStyleClass().add("backButton");

        bounceDefButton = new Button("Bounce Def");
        bounceDefButton.getStyleClass().add("bounceButton");

        // Sliding Menu Pane
        menuBar = new VBox();
        menuBar.getStyleClass().add("menu");
        VBox.setVgrow(menuBar, Priority.ALWAYS);
        VBox.setMargin(menuBar, new Insets(20, 0, 30, 0));
        menuBar.setMaxWidth(350);
        wrapper.getChildren().add(menuBar);


        // Shows Current bounce Definition
        bounceDefinition = new Text("");
        bounceDefinition.getStyleClass().add("text");
        menuBar.getChildren().add(bounceDefinition);

      menuBar.getChildren().add(createCheckBoxes());
      menuBar.setAlignment(Pos.BOTTOM_LEFT);


      menuBar.setAlignment(Pos.BOTTOM_LEFT);


        filterButton = new Button("Filter");
        menuBar.getChildren().add(filterButton);


        // Menu Button
        menuButton = new Button();
        menuButton.setContentDisplay(ContentDisplay.CENTER);
        menuButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        menuButton.setAlignment(Pos.CENTER);
        menuButton.getStyleClass().add("menuButton");
        Image menu = new Image(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/menu.png")));
        var menuImg = new ImageView(menu);
        menuImg.setFitHeight(20);
        menuImg.setFitWidth(20);
        menuButton.setGraphic(menuImg);

        // Menu Animation
        var menuTransition = new TranslateTransition(Duration.millis(200), menuBar);
        menuButton.setOnAction(e -> {
            if (menuOpen) {
                menuTransition.setToX(-menuBar.getWidth());
                menuTransition.setFromX(0);
                menuTransition.setOnFinished(evt -> layout.setLeft(null));
                menuTransition.play();
            } else {
                menuTransition.setToX(0);
                menuTransition.setFromX(-200);
                menuTransition.setOnFinished(evt -> {
                    menuBar.translateXProperty().set(0);
                });
                menuTransition.play();
//        layout.setLeft(menuBar);
            }
            menuOpen = !menuOpen;
        });

        // Title box positioning
        titleBox.getChildren().add(menuButton);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        titleBox.getChildren().add(spacer);
        titleBox.getChildren().add(titleLabel);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        titleBox.getChildren().add(spacer2);
        titleBox.getChildren().add(new Label(""));
        titleBox.getStyleClass().add("titleBox");

        //Back and bounce def buttons
        BorderPane bottomButtons = new BorderPane();

        BorderPane.setMargin(backButton, new Insets(20, 0, 10, 10));
        BorderPane.setMargin(titleBox, new Insets(10, 0, 0, 10));
        BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
        BorderPane.setAlignment(dashboardComp, Pos.CENTER);

        BorderPane.setMargin(bounceDefButton, new Insets(20, 10, 10, 10));
        BorderPane.setMargin(titleBox, new Insets(10, 0, 0, 10));
        BorderPane.setAlignment(bounceDefButton, Pos.BOTTOM_RIGHT);
        //BorderPane.setAlignment(dashboardComp, Pos.CENTER);

        bottomButtons.setLeft(backButton);
        bottomButtons.setRight(bounceDefButton);
        inner.setBottom(bottomButtons);


        scene = new Scene(layout, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/view/dashboard.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/view/dashboardComp.css").toExternalForm());
        layout.setPrefHeight(scene.getHeight());
    }

    ListView<Object> createCheckBoxes() {

        compareList = new ListView<>();

        var genderText = new Label("Gender of Audience:");
        genderText.getStyleClass().add("list-cell-text");
        maleCheckBox = new CheckBox("Male");
        maleCheckBox.getStyleClass().add("checkbox");
        maleCheckBox.setId("male_1");
        femaleCheckBox = new CheckBox("Female");
        femaleCheckBox.getStyleClass().add("checkbox");
        femaleCheckBox.setId("female_1");
        this.checkboxes.add(maleCheckBox);
        this.checkboxes.add(femaleCheckBox);

        compareList.getStyleClass().add("list-cell");
        compareList.getItems().addAll(genderText, maleCheckBox, femaleCheckBox);

        var ageText = new Label("Age of Audience:");
        ageText.getStyleClass().add("list-cell-text");
        compareList.getItems().add(ageText);
        for (Age a : Age.values()) {
            CheckBox box = new CheckBox(a.label);
            box.setId("age_" + a.idx);
            compareList.getItems().add(box);
            this.checkboxes.add(box);
        }

        var incomeText = new Label("Income of Audience:");
        incomeText.getStyleClass().add("list-cell-text");
        compareList.getItems().add(incomeText);
        for (Income i : Income.values()) {
            CheckBox box = new CheckBox(i.label);
            box.setId("income_" + i.idx);
            compareList.getItems().add(box);
            this.checkboxes.add(box);
        }

        var contextText = new Label("Location of Ad Interaction:");
        contextText.getStyleClass().add("list-cell-text");
        compareList.getItems().add(contextText);
        for (Context c : Context.values()) {
            CheckBox box = new CheckBox(c.label);
            box.setId("context_" + c.idx);
            compareList.getItems().add(box);
            this.checkboxes.add(box);
        }
        this.checkboxes.forEach(c -> c.getStyleClass().add("checkbox"));
        return compareList;

    }


    public void setLoading() {
        StackPane stackPane = new StackPane();
        Text loadingText = new Text("Loading...");
        loadingText.getStyleClass().add("loading");
        stackPane.getChildren().add(loadingText);
        root.getChildren().add(stackPane);
    }

    public void removeLoading() {
        root.getChildren().remove(root.getChildren().size() - 1);
    }


    public void postShowEdits() {
        menuBar.setTranslateX(-menuBar.getWidth());
        titleLabel.setTranslateX(-menuButton.getWidth() / 2);
    }

    /**
     * @return get the dashboard components
     */
    public DashboardComp getDashboardComp() {
        return this.dashboardComp;
    }

    /**
     * @return get the back button
     */
    public Button getBackButton() {
        return this.backButton;
    }

    public Button getBounceDefButton() {
        return this.bounceDefButton;
    }

    public void setStyles(boolean theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/view/dashboard.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/view/dashboardComp.css").toExternalForm());
//        if (theme) {
//            scene.getStylesheets().add(getClass().getResource("/view/dashboard.css").toExternalForm());
//            scene.getStylesheets().add(getClass().getResource("/view/dashboardComp.css").toExternalForm());
////            menuImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/menu.png"))));
//
//        } else {
//            scene.getStylesheets().add(getClass().getResource("/view/dashboardLight.css").toExternalForm());
//            scene.getStylesheets().add(getClass().getResource("/view/dashboardCompLight.css").toExternalForm());
////            menuImg.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/menuLight.png"))));
//
//        }
    }

    public Button getFilterButton() {
        return this.filterButton;
    }

    public ArrayList<CheckBox> getCheckboxes() {
        return checkboxes;
    }

    /**
     * @return get the male check box
     */
    public CheckBox getMaleCheckBox() {
        return maleCheckBox;
    }

    /**
     * @return get the female check box
     */
    public CheckBox getFemaleCheckBox() {
        return femaleCheckBox;
    }


}