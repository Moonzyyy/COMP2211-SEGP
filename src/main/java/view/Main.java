package view;

import javafx.application.Application;
import javafx.stage.Stage;
import view.scenes.StartMenu;

public class Main extends Application {

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) {
    stage.setTitle("AdViz");
    stage.setScene(new StartMenu(stage).getScene());
    stage.show();
  }
}