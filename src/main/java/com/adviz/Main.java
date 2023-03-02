package com.adviz;

import java.io.IOException;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

  public static void main(String[] args) {
    launch();
  }

  //Start Menu
  @Override
  public void start(Stage stage) throws IOException {


    //Simple start menu

    Button startButton = new Button("Import");
    startButton.setOnAction(e -> {
      System.out.println("Import button clicked");
    });
    Button settingsButton = new Button("Settings");
    settingsButton.setOnAction(e -> {
      System.out.println("Settings button clicked");
    });

    var startBorderPane = new BorderPane();

    var startButtonsVBox = new VBox();
    startButtonsVBox.getChildren().addAll(startButton, settingsButton);
    startBorderPane.setCenter(startButtonsVBox);
    startButtonsVBox.setSpacing(20);
    startButtonsVBox.setAlignment(Pos.CENTER);

    var scene = new Scene(startBorderPane, 300, 300);

    stage.setScene(scene);
    stage.setTitle("AdViz");
    stage.show();

    //Opens the start menu
  }

}
