package view.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// Creates a component that displays two rows of 6 clickable numbers that each have a piece of text below them.
public class DashboardComp extends VBox {

  public DashboardComp() {
    var row1 = new HBox();
    var row2 = new HBox();
    row1.setAlignment(Pos.CENTER);
    row2.setAlignment(Pos.CENTER);
    row1.setSpacing(20);
    row2.setSpacing(20);
    row1.setPrefSize(800, 300);
    row2.setPrefSize(800, 300);
    for (int i = 1; i <= 12; i++) {
      var number = new Label(String.valueOf(i));
      number.getStyleClass().add("number");
      var text = new Label("Text");
      text.getStyleClass().add("text");
      var numberBox = new VBox();
      numberBox.setAlignment(Pos.CENTER);
      numberBox.getChildren().addAll(number, text);
      if (i <= 6) {
        row1.getChildren().add(numberBox);
      } else {
        row2.getChildren().add(numberBox);
      }
    }
    this.getChildren().addAll(row1, row2);
  }

}