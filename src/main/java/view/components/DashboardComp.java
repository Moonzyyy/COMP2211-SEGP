package view.components;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import view.scenes.AbstractScene;
import view.scenes.Graph;

public class DashboardComp extends VBox {

  private final List<VBox> numberBoxes = new ArrayList<>();

  public DashboardComp(AbstractScene scene) {
    setAlignment(Pos.CENTER);
    setSpacing(20);

    final int boxesPerRow = 6;
    final int totalBoxes = 11;
    final int rows = (int) Math.ceil((double) totalBoxes / boxesPerRow);

    for (int i = 0; i < rows; i++) {
      final HBox row = new HBox();
      row.setAlignment(Pos.CENTER);
      row.getStyleClass().add("row");
      row.setPrefWidth(USE_COMPUTED_SIZE);
      row.setPrefHeight(USE_COMPUTED_SIZE);

      for (int j = 0; j < boxesPerRow && i * boxesPerRow + j < totalBoxes; j++) {
        final int number = i * boxesPerRow + j + 1;
        final String text = getLabelText(number);
        final VBox numberBox = createNumberBox(number, text, scene);
        row.getChildren().add(numberBox);
        numberBoxes.add(numberBox);
      }

      getChildren().add(row);
    }
  }

  /**
   * Text labels for all metrics to use in buttons
   * @param number that denotes metric
   * @return metric text
   */
  private String getLabelText(int number) {
    switch (number) {
      case 1:
        return "Impressions";
      case 2:
        return "Clicks";
      case 3:
        return "Bounces";
      case 4:
        return "Conversions";
      case 5:
        return "Cost";
      case 6:
        return "CTR";
      case 7:
        return "CPA";
      case 8:
        return "CPC";
      case 9:
        return "CPM";
      case 10:
        return "Bounce Rate";
      case 11:
        return "Uniques";
      default:
        return "";
    }
  }

  /**
   * Button for each metric, with the name and value of each metric
   * @param number, the metric value
   * @param text, the name of the metric
   * @param scene
   * @return metric button
   */
  private VBox createNumberBox(int number, String text, AbstractScene scene) {
    final Label numberLabel = new Label(Integer.toString(number));
    numberLabel.getStyleClass().add("number");

    final Label textLabel = new Label(text);
    textLabel.getStyleClass().add("text");

    final VBox numberBox = new VBox(numberLabel, textLabel);
    numberBox.getStyleClass().add("numberBox");
    numberBox.setAlignment(Pos.CENTER);
    numberBox.setPrefWidth(USE_COMPUTED_SIZE);
    numberBox.setPrefHeight(USE_COMPUTED_SIZE);

    numberBox.setOnMouseClicked(e -> {
      System.out.println("Button:" + text + " clicked");
    });

    return numberBox;
  }

  public List<VBox> getNumberBoxes() {
    return numberBoxes;
  }

  public void updateNumberBoxes(List<String> numbers) {
    for (int i = 0; i < numbers.size(); i++) {
      final Label numberLabel = (Label) numberBoxes.get(i).getChildren().get(0);
      numberLabel.setText(numbers.get(i));
    }
  }
}