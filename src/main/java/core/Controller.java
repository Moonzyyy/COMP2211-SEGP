package core;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jfree.data.time.TimeSeries;
import view.components.CompareItem;
import view.components.DashboardComp;
import view.scenes.*;

public class Controller {

  private static final Logger logger = LogManager.getLogger(Controller.class);
  private final Model model;
  private AbstractScene currentScene;
  private Stage stage;

  /**
   * The constructor of the controller.
   *
   * @param model the model of the application
   */
  public Controller(Model model) {
    this.model = model;
  }


  /**
   * Pops up an alert to the user
   *
   * @param message send the error message that you want to show the user
   */
  public static void sendErrorMessage(String message) {
    logger.error("Error Occurred!");
    Platform.runLater(() ->
    {
      Alert alert = new Alert(Alert.AlertType.ERROR, message);
      alert.showAndWait();
    });
  }

  /**
   * @param stage Sets the stage within the class
   */
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * Sets the current scene. Acts as a scene switcher.
   *
   * @param newScene the new scene to be set
   */
  private void setCurrentScene(AbstractScene newScene) {
    logger.info("Creating Scene: " + newScene.getClass().getSimpleName());
    stage.setScene(newScene.getScene());
    this.currentScene = newScene;
    stage.show();
    newScene.postShowEdits();
  }

  /**
   * Sets up the start menu scene. This is the first scene that is shown. Does both the creation of
   * the scene and the action listeners.
   *
   * @param menu the start menu scene
   */
  public void setUpScene(StartMenu menu) {
    menu.createScene();
    this.setCurrentScene(menu);

    //Add the action listeners

    menu.getImportButton().setOnAction((event) -> {
      setUpScene(new Import());
    });

    menu.getSettingsButton().setOnAction((event) -> {
      setUpScene(new Settings());
    });

    menu.getResumeButton().setOnAction((event) -> {
      if (model.getMetrics().size() > 0) {
        setUpScene(new Dashboard());
      }
    });

    // Predicates
    menu.getResumeButton()
        .setVisible(model.getImpressions() != null && model.getMetrics().size() > 0);
  }

  /**
   * Sets up the dashboard scene.
   *
   * @param dashboard the dashboard scene
   */
  public void setUpScene(Dashboard dashboard) {
    dashboard.createScene();
    this.setCurrentScene(dashboard);

    List<String> metrics = model.getMetrics();
    DashboardComp dashboardComp = dashboard.getDashboardComp();
    dashboardComp.updateNumberBoxes(metrics);

    //Button action listeners
    dashboard.getBackButton().setOnAction((event) -> {
      setUpScene(new StartMenu());
    });
    dashboard.getBounceDefButton().setOnAction((event) -> {
      setUpScene(new BounceDef());
    });
    // All number box listeners
    for (int i = 0; i < dashboardComp.getNumberBoxes().size(); i++) {
      int finalI = i;
      dashboardComp.getNumberBoxes().get(i).setOnMouseClicked((event) -> {
        String title = "";
        String xAxisName = "Date";
        String yAxisName = "";
        boolean needDivisionForChangeTime = false;
        switch (finalI) {
          case 0 -> {
            title = "Impressions Over Time";
            yAxisName = "Impressions";
          }
          case 1 -> {
            title = "Clicks Over Time";
            yAxisName = "Clicks";
          }
          case 2 -> {
            title = "Bounces Over Time";
            yAxisName = "Bounces";
          }
          case 3 -> {
            title = "Conversions Over Time";
            yAxisName = "Conversions";
          }
          case 4 -> {
            title = "Total Cost Over Time";
            yAxisName = "Click Costs";
          }
          case 5 -> {
            title = "Click-Through-Rate Over Time";
            yAxisName = "Click-through-rate";
            needDivisionForChangeTime = true;
          }
          case 6 -> {
            title = "Cost-per-acquisition Over Time";
            yAxisName = "Cost-per-acquisition";
            needDivisionForChangeTime = true;
          }
          case 7 -> {
            title = "Cost-per-click Over Time";
            yAxisName = "Cost-per-click";
            needDivisionForChangeTime = true;
          }
          case 8 -> {
            title = "Cost-per-thousand impressions Over Time";
            yAxisName = "Cost-per-thousand impressions";
            needDivisionForChangeTime = true;
          }
          case 9 -> {
            title = "Bounce Rate Over Time";
            yAxisName = "Bounce Rate";
            needDivisionForChangeTime = true;
          }
          case 10 -> {
            title = "Uniques Over Time";
            yAxisName = "Uniques";
          }
        }
        GraphModel gm = new GraphModel(model, title, xAxisName, yAxisName, finalI,
            needDivisionForChangeTime);
        HistogramModel histogramModel = new HistogramModel(title,xAxisName,yAxisName,finalI);
        setUpScene(new Graph(finalI, gm.getChart(), histogramModel.getChart()), gm, histogramModel);
      });
    }
  }

  /**
   * Sets up the settings scene.
   *
   * @param settings the settings scene
   */
  public void setUpScene(Settings settings) {

    settings.createScene();
    this.setCurrentScene(settings);

    //Button action listeners
    settings.getBackButton().setOnAction((event) -> {
      setUpScene(new StartMenu());
    });

  }

  private void setUpScene(BounceDef bounceDef) {
    bounceDef.createScene();
    this.setCurrentScene(bounceDef);

    bounceDef.getBackButton().setOnAction((event) -> {
      setUpScene(new Dashboard());
    });

    bounceDef.getApplyButton().setOnAction((event) -> {
      if (bounceDef.getPageRadio().isSelected()) {
        model.setBounceDef("page");
        model.setBounceValue(Integer.parseInt(bounceDef.getInputPageText().getText()));
      } else {
        model.setBounceDef("time");
        model.setBounceValue(Integer.parseInt(bounceDef.getInputTimeText().getText()));
      }
      setUpScene(new Dashboard());
    });

    bounceDef.getResetButton().setOnAction((event) -> {
      model.setBounceDef("default");
      setUpScene(new Dashboard());
    });
  }

  /**
   * Sets up the graph scene.
   *
   * @param graphScene the graph scene
   */
  public void setUpScene(Graph graphScene, GraphModel graphModel, HistogramModel histogramModel) {
    HashMap<String, Boolean> preds = initPredicates();
    graphScene.createScene();
    this.setCurrentScene(graphScene);

    //Button action listeners
    graphScene.getHomeButton().setOnAction((event) -> {
      model.setPredicate(null);
      setUpScene(new Dashboard());
    });
    // Creates a print job, works for physical printers, not PDFs
    graphScene.getPrintButton().setOnAction((event) -> {
//      graphScene.getChart().createChartPrintJob();

    });

    graphModel.configureDatePickers(graphScene.getStartDatePicker(), graphScene.getEndDatePicker(),
        graphScene.getDateFilterButton());

    graphModel.configureDatePickers(graphScene.getCompareControlStartDatePicker(), graphScene.getCompareControlEndDatePicker(), graphScene.getCompareControlDateFilterButton());
    graphModel.updateGraphData(preds);

    // Checkbox Listener
    // When the filter button is pressed, get all the currently selected filters
    // and update the graph

    graphScene.getDateFilterButton().setOnAction(e -> {
      graphModel.updateDateFilters(graphScene.getStartDatePicker().getValue(),
              graphScene.getEndDatePicker().getValue());
      graphScene.getDateFilterButton().setDisable(true);
    });

    graphScene.getCompareControlDateFilterButton().setOnAction(e -> {
      boolean setVisible = graphModel.updateCompareDateFilters(graphScene.getCompareControlStartDatePicker().getValue(),
          graphScene.getCompareControlEndDatePicker().getValue());
      graphScene.getCompareControlDateFilterButton().setDisable(true);
//      System.out.println(setVisible);
      graphScene.setLineVisibility(2, setVisible);
      graphScene.setLineVisibility(3, setVisible && graphModel.getLines().get(1).isEnabled());
    });

    graphScene.getTimeFilter().setOnAction(event -> {
      graphModel.updateGraphData(graphScene.getTimeFilter().getValue(), preds);
      graphScene.getLineChart().restoreAutoBounds();
    });

    graphScene.getCheckboxes().forEach(box -> {
      box.setOnAction(event -> {
        var male = graphScene.getMaleCheckBox();
        var female = graphScene.getFemaleCheckBox();
        if (box.equals(male)) {
          if (female.isSelected()) togglePred(preds, female);
        } else if (box.equals(female)) {
          if (male.isSelected()) togglePred(preds, male);
        }
        preds.replace(box.getId(), box.isSelected());
      });
    });

    graphScene.getSegmentFilterButton().setOnAction((event) -> {
      graphModel.updateGraphData(preds);
    });

    graphScene.getCompareControl1().setOnAction(event -> {
      this.updateLine(graphModel, graphScene, 0);
      graphModel.updateGraphData(preds);
    });

    graphScene.getCompareControl2().setOnAction(event -> {
      ComboBox<CompareItem> box = graphScene.getCompareControl2();
      CompareItem item = box.getSelectionModel().getSelectedItem();
      GraphLine line = graphModel.getLines().get(1);
      if (item.value() != null) {
        this.updateLine(graphModel, graphScene, 1);
        line.setEnabled(true);
        graphScene.setLineVisibility(1, true);
        graphScene.setLineVisibility(3, true);
        graphModel.removeLine(line.getDatedSeries1());
        line.setDatedSeries1(new TimeSeries(item.label()));
      } else {
        graphScene.setLineVisibility(1, false);
        graphScene.setLineVisibility(3, false);
        toggleBoxDisable(graphScene.getCheckboxes(), line.getBasePredicate());
        line.setBasePredicate(null);
        line.setEnabled(false);
      }
      graphModel.updateGraphData(preds);
    });

    //Checkbox listeners
//    graphScene.getMaleCheckBox().setOnAction(event -> {
//      if (graphScene.getFemaleCheckBox().isSelected()) {
//        graphScene.getFemaleCheckBox().setSelected(false);
//      }
//    });
//    graphScene.getFemaleCheckBox().setOnAction(event -> {
//      if (graphScene.getMaleCheckBox().isSelected()) {
//        graphScene.getMaleCheckBox().setSelected(false);
//      }
//    });
  }

  private void updateLine(GraphModel graphModel, Graph graphScene, int index) {
    var line = graphModel.getLines().get(index);
    ComboBox<CompareItem> box = index == 0 ? graphScene.getCompareControl1() : graphScene.getCompareControl2();
    CompareItem item = box.getSelectionModel().getSelectedItem();
    toggleBoxDisable(graphScene.getCheckboxes(), line.getBasePredicate());
//    line.setTitle(item.label());
    line.setBasePredicate(item.value());
    line.setTitle(index == 0 ? graphModel.getTitle() : item.label());
    toggleBoxDisable(graphScene.getCheckboxes(), item.value());
  }

  private void togglePred(HashMap<String, Boolean> predicates, CheckBox box) {
    box.setSelected(!box.isSelected());
    predicates.replace(box.getId(), box.isSelected());
  }

  private void toggleBoxDisable(ArrayList<CheckBox> checkBoxes, String id) {
    var box =  checkBoxes.stream().filter(c -> Objects.equals(c.getId(), id)).findFirst();
    box.ifPresent(b -> b.setDisable(!b.isDisabled()));
  }

  /**
   * Initialise the predicates used for filtering by audience segment.
   */
//  public ArrayList<ArrayList<FilterPredicate>> initPredicates() {
  public HashMap<String, Boolean> initPredicates() {
    HashMap<String, Boolean> predicates = new HashMap<>();
    for (Age a : Age.values()) {
      predicates.put("age_" + a.idx, false);
    }

    for (Context c : Context.values()) {
      predicates.put("context_" + c.idx, false);
    }

    for (Income i : Income.values()) {
      predicates.put("income_" + i.idx, false);
    }

    predicates.put("male_1", false);
    predicates.put("female_1", false);

    return predicates;
  }


  /**
   * @param loading Loading Scene
   */
  public void setUpScene(Loading loading) {
    loading.createScene();
    this.setCurrentScene(loading);
  }

  /**
   * Sets up an import screen that walks the user through the import process.
   *
   * @param importScene the import scene
   */
  public void setUpScene(Import importScene) {

    importScene.createScene();
    this.setCurrentScene(importScene);

    //Button action listeners
    importScene.getBackButton().setOnAction((event) -> {
      setUpScene(new StartMenu());
    });

    if (model.getImpressionsFile() != null && model.getServerFile() != null
        && model.getClicksFile() != null) {
      importScene.getImpressionFileName().setText(model.getImpressionsFile().getName());
      importScene.getClickFileName().setText(model.getClicksFile().getName());
      importScene.getServerFileName().setText(model.getServerFile().getName());

    }

    //When each button is pressed, open a file browser and set the corresponding text field
    importScene.getImportClicks().setOnAction((event) -> {
      try {
        File file = importScene.getFileChooser().showOpenDialog(stage);
        importScene.getFileChooser().setInitialDirectory(file.getParentFile());

        model.setClicksFile(file);
        importScene.getClickFileName().setText(file.getName());
        importScene.getLoadButton().setDisable(
            model.getClicksFile() == null || model.getImpressionsFile() == null
                || model.getServerFile() == null);
      } catch (Exception e) {

      }

    });

    importScene.getImportImpressions().setOnAction((event) -> {
      try {
        var file = importScene.getFileChooser().showOpenDialog(stage);
        importScene.getFileChooser().setInitialDirectory(file.getParentFile());
        model.setImpressionsFile(file);
        importScene.getImpressionFileName().setText(file.getName());
        importScene.getLoadButton().setDisable(
            model.getClicksFile() == null || model.getImpressionsFile() == null
                || model.getServerFile() == null);
      } catch (Exception e) {

      }
    });

    importScene.getImportServer().setOnAction((event) -> {
      try {
        var file = importScene.getFileChooser().showOpenDialog(stage);
        importScene.getFileChooser().setInitialDirectory(file.getParentFile());
        model.setServerFile(file);
        importScene.getServerFileName().setText(file.getName());
        importScene.getLoadButton().setDisable(
            model.getClicksFile() == null || model.getImpressionsFile() == null
                || model.getServerFile() == null);
      } catch (Exception e) {

      }
    });

    importScene.getLoadButton().setDisable(
        model.getClicksFile() == null || model.getImpressionsFile() == null
            || model.getServerFile() == null);

    importScene.getLoadButton().setOnAction((event) -> {
      Task<Void> task = new Task<>() {
        @Override
        public Void call() {
          if (!model.importData()) {
            this.cancel();
          }
          return null;
        }
      };
      setUpScene(new Loading());
      task.setOnSucceeded(e -> {
        setUpScene(new Dashboard());
      });
      task.setOnCancelled(e -> {
        setUpScene(new Import());
      });
      Thread thread = new Thread(task);
      thread.start();
    });
  }
}