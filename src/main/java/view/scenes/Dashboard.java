package view.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.AppView;
import view.components.DashboardComp;

/**
 * The dashboard scene.
 */
public class Dashboard extends AbstractScene {

  private final BorderPane layout;

  public Dashboard(Stage stage, AppView view) {
    super(stage, view);
    layout = new BorderPane();
    createScene();
  }

  void createScene() {
    var titleBox = new VBox();
    titleBox.setAlignment(Pos.CENTER);
    var titleLabel = new Label("Dashboard");
    titleLabel.getStyleClass().add("title");
    titleBox.getChildren().add(titleLabel);
    layout.setTop(titleBox);

    var dashboard = new DashboardComp(this, stage);
    dashboard.getStyleClass().add("dashboardComp");
    layout.setCenter(dashboard);

    //Make it so that the back button is circular
    var backButton = new Button("<");
    backButton.setOnAction(e -> {
      stage.setScene(new StartMenu(stage, getView()).getScene());
    });
    backButton.getStyleClass().add("backButton");

    BorderPane.setMargin(backButton, new Insets(0, 0, 10, 10));
    BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
    BorderPane.setAlignment(dashboard, Pos.CENTER);
    layout.setBottom(backButton);

    scene = new Scene(layout, 1280, 720);
    scene.getStylesheets().add(getClass().getResource("/view/dashboard.css").toExternalForm());
    scene.getStylesheets().add(getClass().getResource("/view/dashboardComp.css").toExternalForm());
  }

}