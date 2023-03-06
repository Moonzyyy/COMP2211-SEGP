package view;

import core.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import view.scenes.StartMenu;

public class AppView {

private StartMenu sm = null;
private Controller controller = null;

  public AppView() {
  }

  public StartMenu getSm() {
    return sm;
  }

  public void setSm(StartMenu sm) {
    this.sm = sm;
  }

  public Controller getController() {
    return controller;
  }

  public void setController(Controller controller) {
    this.controller = controller;
  }
}