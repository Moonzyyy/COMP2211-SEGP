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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import view.components.DashboardComp;

/**
 * The dashboard scene.
 */
public class Dashboard extends AbstractScene {

  private final BorderPane layout;

  public Dashboard() {
    super();
    layout = new BorderPane();
//    createScene();
  }

  public void createScene() {
    var titleBox = new HBox();
    titleBox.setAlignment(Pos.CENTER);
    var titleLabel = new Label("Dashboard");
    HBox.setHgrow(titleLabel, Priority.ALWAYS);
    titleLabel.getStyleClass().add("title");
    layout.setTop(titleBox);

    var dashboard = new DashboardComp(this);
    dashboard.getStyleClass().add("dashboardComp");
    layout.setCenter(dashboard);

    //Make it so that the back button is circular
    var backButton = new Button("<");
    backButton.setOnAction(e -> {
//      stage.setScene(new StartMenu(stage).getScene());
    });
    backButton.getStyleClass().add("backButton");

    // Sliding Menu Pane
    var menuBar = new VBox();
    menuBar.getStyleClass().add("menu");
    VBox.setVgrow(menuBar, Priority.ALWAYS);
    VBox.setMargin(menuBar, new Insets(20, 0, 30, 0));
    menuBar.setPrefWidth(200);
    layout.setLeft(null);

    // Placeholder for menu items
    var menuPlaceholder = new Label("Dev Text");
    menuPlaceholder.getStyleClass().add("text");
    menuBar.getChildren().addAll(menuPlaceholder);
    menuBar.setAlignment(Pos.CENTER);

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

    var menuTransition = new TranslateTransition(Duration.millis(200), menuBar);
    // Menu Animation
    menuButton.setOnAction(e -> {
      if (layout.getLeft() == null) {
        menuTransition.setToX(0);
        menuTransition.setFromX(-200);
        menuTransition.setOnFinished(evt -> {
          menuBar.translateXProperty().set(0);
        });
        menuTransition.play();
        layout.setLeft(menuBar);
      } else {
        menuTransition.setToX(-menuBar.getWidth());
        menuTransition.setFromX(0);
        menuTransition.setOnFinished(evt -> layout.setLeft(null));
        menuTransition.play();
      }
    });
    titleBox.getChildren().add(menuButton);
    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    titleBox.getChildren().add(spacer);
    titleBox.getChildren().add(titleLabel);
    Region spacer2 = new Region();
    HBox.setHgrow(spacer2, Priority.ALWAYS);
    titleBox.getChildren().add(spacer2);
    titleBox.getChildren().add(new Label(""));

    BorderPane.setMargin(backButton, new Insets(20, 0, 10, 10));
    BorderPane.setMargin(titleBox, new Insets(10, 0, 20, 10));
    BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
    BorderPane.setAlignment(dashboard, Pos.CENTER);
    layout.setBottom(backButton);

    scene = new Scene(layout, 1280, 720);
    scene.getStylesheets().add(getClass().getResource("/view/dashboard.css").toExternalForm());
    scene.getStylesheets().add(getClass().getResource("/view/dashboardComp.css").toExternalForm());
    layout.setPrefHeight(scene.getHeight());
  }



}