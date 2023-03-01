package com.example.group30;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

  @FXML
  private Label welcomeText;

  @FXML
  protected void onHelloButtonClick() {
    welcomeText.setText("Welcome to JavaFX Application!");
  }

  @FXML
  //Change scene to settings
  protected void onSettingsButtonClick() {
    System.out.println("Settings button clicked");
  }
}