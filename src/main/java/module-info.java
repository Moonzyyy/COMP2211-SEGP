module com.example.group30 {
  requires javafx.controls;
  requires javafx.fxml;

  opens com.example.group30 to javafx.fxml;
  exports com.example.group30;
}