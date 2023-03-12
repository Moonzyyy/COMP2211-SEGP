module com.example.group30 {
  requires com.opencsv;
  requires java.sql;
  requires javafx.graphics;
  requires javafx.fxml;
  requires javafx.controls;

  opens com.example.group30 to javafx.fxml;
  exports com.example.group30;
  exports csvTest;
}