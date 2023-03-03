module com.adviz {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.opencsv;
  requires java.sql;

  opens view to javafx.fxml;
  exports view;
  exports model;
  exports view.scenes;
  opens view.scenes to javafx.fxml;
}