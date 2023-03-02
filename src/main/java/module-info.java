module com.adviz {
  requires javafx.controls;
  requires javafx.fxml;
  requires com.opencsv;
  requires java.sql;

  opens com.adviz to javafx.fxml;
  exports com.adviz;
  exports csvTest;
}