module com.adviz {
//  requires com.opencsv;
  requires java.sql;
  requires java.desktop;
  requires org.jfree.jfreechart;
  requires javafx.graphics;
  requires javafx.controls;
  requires javafx.swing;
  requires org.apache.logging.log4j;
  requires kernel;
  requires io;
  requires org.apache.pdfbox;
    requires layout;

    opens view to javafx.fxml;
  opens core to javafx.fxml;
  exports core;
  //exports view;
  exports model;
  exports view.scenes;
  exports core.segments;
  opens view.scenes to javafx.fxml;
}