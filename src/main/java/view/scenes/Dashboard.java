package view.scenes;

import java.util.List;
import java.util.Objects;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Shadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import view.components.DashboardComp;

/**
 * The dashboard scene.
 */
public class Dashboard extends AbstractScene {

  private final StackPane root;
  private final BorderPane layout;
  private VBox menuBar;
  private boolean menuOpen = false;
  private DashboardComp dashboardComp;
  private Button backButton;
  private Button bounceDefButton;

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
    var titleLabel = new Label("Dashboard");
    titleLabel.getStyleClass().add("title");
    layout.setTop(titleBox);

    dashboardComp = new DashboardComp(this);
    dashboardComp.getStyleClass().add("dashboardComp");
    var inner = new BorderPane();
    inner.setCenter(dashboardComp);
    var wrapper = new StackPane(inner);
    layout.setCenter(wrapper);
    wrapper.setAlignment(Pos.BOTTOM_LEFT);

    backButton = new Button("<");
    backButton.getStyleClass().add("backButton");

    bounceDefButton = new Button("Change Bounce Definition");
    bounceDefButton.getStyleClass().add("bounceButton");

    // Sliding Menu Pane
    menuBar = new VBox();
    menuBar.getStyleClass().add("menu");
    VBox.setVgrow(menuBar, Priority.ALWAYS);
    VBox.setMargin(menuBar, new Insets(20, 0, 30, 0));
    menuBar.setMaxWidth(350);
    wrapper.getChildren().add(menuBar);

    // Placeholder for menu items
    var menuPlaceholder = new Label("Dev Text");
    menuPlaceholder.getStyleClass().add("text");
    menuBar.getChildren().addAll(menuPlaceholder);
    menuBar.setAlignment(Pos.BOTTOM_LEFT);

    // Menu Button
    var menuButton = new Button();
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

  public void postShowEdits() {
    menuBar.setTranslateX(-menuBar.getWidth());
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
  public Button getBackButton(){
    return this.backButton;
  }

  public Button getBounceDefButton(){
    return this.bounceDefButton;
  }
}