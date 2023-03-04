module com.adviz {
  requires com.opencsv;
  requires java.sql;
  requires javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;

  opens view to javafx.fxml;
  exports view;
  exports model;
}