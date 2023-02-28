module com.example.group30 {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.opencsv;
  requires java.sql;

  opens com.example.group30 to javafx.fxml;
  exports com.example.group30;
  exports csvTest;
}