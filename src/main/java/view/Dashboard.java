package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Components.DashboardComp;

public class Dashboard {

  private final Stage stage;
  private Scene scene;
  private final BorderPane layout;

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
    layout.setCenter(dashboard);

    var backButton = new Button("Back");
    backButton.setOnAction(e -> {
      stage.setScene(new StartMenu(stage).getScene());
    });
    backButton.getStyleClass().add("startButton");
    layout.setBottom(backButton);


    scene = new Scene(layout, 800, 600);
    scene.getStylesheets().add(getClass().getResource("dashboard.css").toExternalForm());
    scene.getStylesheets().add(getClass().getResource("dashboardComp.css").toExternalForm());
  }

  public Scene getScene() {
    return scene;
  }
}