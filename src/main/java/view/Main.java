package view;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

  public static void main(String[] args) {
    launch();
  }

  //Start Menu
  @Override
  public void start(Stage stage) throws IOException {
    //Simple start menu, no fxml

    Button startButton = new Button("Import");
    startButton.setOnAction(e -> {
      System.out.println("Import button clicked");
    });
    startButton.getStyleClass().add("startButton");
    Button settingsButton = new Button("Settings");
    settingsButton.setOnAction(e -> {
      System.out.println("Settings button clicked");
    });
    settingsButton.getStyleClass().add("startButton");




    var startBorderPane = new BorderPane();
    //Add a material design card to go behind the buttons
    var startTitle = new VBox();
    startTitle.setAlignment(Pos.CENTER);
    var title = new Label("AdViz");
    title.getStyleClass().add("title");
    startTitle.getChildren().add(title);
    startBorderPane.setTop(startTitle);

    var startButtonsVBox = new VBox();
    startButtonsVBox.getChildren().addAll(startButton, settingsButton);
    startBorderPane.setCenter(startButtonsVBox);
    startButtonsVBox.setPrefSize(200,200);
    startButtonsVBox.setSpacing(20);
    startButtonsVBox.setAlignment(Pos.CENTER);

    var scene = new Scene(startBorderPane, 800, 600);
    scene.getStylesheets().add(getClass().getResource("start.css").toExternalForm());

    stage.setScene(scene);
    stage.setTitle("AdViz");

    stage.show();
  }

}
