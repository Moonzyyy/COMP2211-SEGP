module com.example.group30 {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.opencsv;
  requires java.sql;

  opens view to javafx.fxml;
  exports view;
  exports model;
}