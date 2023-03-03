package view.scenes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.components.DashboardComp;

public class Dashboard {

  private final Stage stage;
  private final BorderPane layout;
  private Scene scene;

  public Dashboard(Stage stage) {
    this.stage = stage;
    layout = new BorderPane();
    createScene();
  }

  private void createScene() {
    var titleBox = new VBox();
    titleBox.setAlignment(Pos.CENTER);
    var titleLabel = new Label("Dashboard");
    titleLabel.getStyleClass().add("title");
    titleBox.getChildren().add(titleLabel);
    layout.setTop(titleBox);

    var dashboard = new DashboardComp();
    dashboard.getStyleClass().add("dashboardComp");
    layout.setCenter(dashboard);

    //Make it so that the back button is circular, and at the bottom left of the screen.
    var backButton = new Button("<");
    backButton.setOnAction(e -> {
      stage.setScene(new StartMenu(stage).getScene());
    });
    backButton.getStyleClass().add("backButton");

    BorderPane.setMargin(backButton, new Insets(0, 0, 10, 10));
    BorderPane.setAlignment(backButton, Pos.BOTTOM_LEFT);
    BorderPane.setAlignment(dashboard, Pos.CENTER);
    layout.setBottom(backButton);

    scene = new Scene(layout, 800, 600);
    scene.getStylesheets().add(getClass().getResource("/view/dashboard.css").toExternalForm());
    scene.getStylesheets().add(getClass().getResource("/view/dashboardComp.css").toExternalForm());
  }

  public Scene getScene() {
    return scene;
  }
}