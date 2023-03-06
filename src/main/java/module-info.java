module com.adviz {
  requires com.opencsv;
  requires java.sql;
  requires java.desktop;
  requires javafx.swing;
  requires org.jfree.jfreechart;
  requires javafx.graphics;
  requires javafx.controls;
  requires javafx.fxml;

  opens view to javafx.fxml;
  opens core to javafx.fxml;
  exports core;
  exports view;
  exports model;
  exports view.scenes;
  opens view.scenes to javafx.fxml;
}