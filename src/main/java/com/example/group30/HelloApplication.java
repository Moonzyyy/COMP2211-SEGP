package com.example.group30;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class HelloApplication extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));

    Scene scene = new Scene(fxmlLoader.load(), 800, 600);
    scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto&display=swap");
    stage.setTitle("AdViz");
    stage.setScene(scene);
    stage.show();
  }
}