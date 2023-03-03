module com.adviz {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.opencsv;
  requires java.sql;
  requires java.desktop;

  opens view to javafx.fxml;
  opens core to javafx.fxml;
  exports core;
  exports view;
  exports model;
  exports view.scenes;
  opens view.scenes to javafx.fxml;
}