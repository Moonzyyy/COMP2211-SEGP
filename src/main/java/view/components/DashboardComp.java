package view.components;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.scenes.AbstractScene;
import view.scenes.Graph;

import java.util.*;

// Creates a component that displays two rows of 6 clickable numbers that each have a piece of text below them.
public class DashboardComp extends VBox {

  public List<VBox> numberBoxes = new ArrayList<VBox>();

  public DashboardComp(AbstractScene scene, Stage stage) {
    var row1 = new HBox();
    var row2 = new HBox();
    row1.setAlignment(Pos.CENTER);
    row2.setAlignment(Pos.CENTER);
    row1.getStyleClass().add("row");
    row2.getStyleClass().add("row");
    row1.setPrefSize(800, 100);
    row2.setPrefSize(800, 100);
    for (int i = 1; i <= 11; i++) {
      var number = new Label(String.valueOf(i));
      number.getStyleClass().add("number");
      var text = new Label();
      text.getStyleClass().add("text");
      switch (i) {
        case 1:
          text.setText("Impressions");
          break;
        case 2:
          text.setText("Clicks");
          break;
        case 3:
          text.setText("Bounces");
          break;
        case 4:
          text.setText("Conversions");
          break;
        case 5:
          text.setText("Cost");
          break;
        case 6:
          text.setText("CTR");
          break;
        case 7:
          text.setText("CPA");
          break;
        case 8:
          text.setText("CPC");
          break;
        case 9:
          text.setText("CPM");
          break;
        case 10:
          text.setText("Bounce Rate");
          break;
        case 11:
          text.setText("Uniques");
          break;
      }
      var numberBox = new VBox();
      numberBox.getStyleClass().add("numberBox");
      numberBox.setAlignment(Pos.CENTER);
      int finalI = i;
      numberBox.setOnMouseClicked(e -> {
        System.out.println("Number " + finalI + " clicked");
        stage.setScene(new Graph(stage, scene.getView()).getScene());
      });
      numberBox.getChildren().addAll(number, text);
      if (i <= 6) {
        row1.getChildren().add(numberBox);
      } else {
        row2.getChildren().add(numberBox);
      }
      numberBoxes.add(numberBox);
    }
    this.getChildren().addAll(row1, row2);
    this.setSpacing(20);
  }

}