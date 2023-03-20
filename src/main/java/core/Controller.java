package core;

import java.io.File;
import java.util.List;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.GraphModel;
import model.Model;
import view.components.DashboardComp;
import view.scenes.AbstractScene;
import view.scenes.Dashboard;
import view.scenes.Graph;
import view.scenes.Import;
import view.scenes.Loading;
import view.scenes.Settings;
import view.scenes.StartMenu;

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
    // All number box listeners
    for (int i = 0; i < dashboardComp.getNumberBoxes().size(); i++) {
      int finalI = i;
      dashboardComp.getNumberBoxes().get(i).setOnMouseClicked((event) -> {
        String title = "";
        String xAxisName = "";
        String yAxisName = "";
        boolean needDivisionForChangeTime = false;
        switch (finalI) {
          case 0 -> {
            title = "Impressions Over Time";
            xAxisName = "Date";
            yAxisName = "Impressions";
            needDivisionForChangeTime = false;
          }
          case 1 -> {
            title = "Clicks Over Time";
            xAxisName = "Date";
            yAxisName = "Clicks";
            needDivisionForChangeTime = false;
          }
          case 2 -> {
            title = "Bounces Over Time";
            xAxisName = "Date";
            yAxisName = "Bounces";
            needDivisionForChangeTime = false;
          }
          case 3 -> {
            title = "Conversions Over Time";
            xAxisName = "Date";
            yAxisName = "Conversions";
            needDivisionForChangeTime = false;
          }
          case 4 -> {
            title = "Total Cost Over Time";
            xAxisName = "Date";
            yAxisName = "Click Costs";
            needDivisionForChangeTime = false;
          }
          case 5 -> {
            title = "Click-Through-Rate Over Time";
            xAxisName = "Date";
            yAxisName = "Click-through-rate";
            needDivisionForChangeTime = true;
          }
          case 6 -> {
            title = "Cost-per-acquisition Over Time";
            xAxisName = "Date";
            yAxisName = "Cost-per-acquisition";
            needDivisionForChangeTime = true;
          }
          case 7 -> {
            title = "Cost-per-click Over Time";
            xAxisName = "Date";
            yAxisName = "Cost-per-click";
            needDivisionForChangeTime = true;
          }
          case 8 -> {
            title = "Cost-per-thousand impressions Over Time";
            xAxisName = "Date";
            yAxisName = "Cost-per-thousand impressions";
            needDivisionForChangeTime = true;
          }
          case 9 -> {
            title = "Bounce Rate Over Time";
            xAxisName = "Date";
            yAxisName = "Bounce Rate";
            needDivisionForChangeTime = true;
          }
          case 10 -> {
            title = "Uniques Over Time";
            xAxisName = "Date";
            yAxisName = "Uniques";
            needDivisionForChangeTime = false;
          }
        }
        GraphModel gm = new GraphModel(model, title, xAxisName, yAxisName, finalI, needDivisionForChangeTime);
        setUpScene(new Graph(finalI, gm.getChart(), gm.getStartDate()), gm);
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

  /**
   * Sets up the graph scene.
   *
   * @param graphScene the graph scene
   */
  public void setUpScene(Graph graphScene, GraphModel graphModel) {
    graphScene.createScene();
    this.setCurrentScene(graphScene);

    //Button action listeners
    graphScene.getHomeButton().setOnAction((event) -> {
      model.setPredicate(null);
      setUpScene(new Dashboard());
    });

    graphScene.getCompareButton().setOnAction((event) -> {
      System.out.print("Compare button pressed");
    });

    graphScene.getPrintButton().setOnAction((event) -> {
      System.out.print("Print button pressed");
    });

    graphModel.configureDatePickers(graphScene.getStartDatePicker(), graphScene.getEndDatePicker(),
        graphScene.getDateFilterButton());

    // Checkbox Listener
    // When the filter button is pressed, get all the currently selected filters
    // and update the graph
    graphScene.getCompareButton()
        .setOnAction((event) -> graphModel.updateFilters(graphScene.getCheckboxes()));

    graphScene.getDateFilterButton().setOnAction(e -> {
      graphModel.updateDateFilters(graphScene.getStartDatePicker().getValue(),
          graphScene.getEndDatePicker().getValue());
      graphScene.getDateFilterButton().setDisable(true);
    });

    graphScene.getTimeFilter().setOnAction(event -> {
      graphModel.updateGraphData(graphScene.getTimeFilter().getValue(), null);
    });

    // compareController listener
    graphScene.getCompareControl1().setOnAction(event -> {
      if (!Objects.equals(graphScene.getCompareControl1().getValue(), "No Filter")) {
        graphScene.getCompareControl2().setVisible(true);
      } else {
        graphScene.getCompareControl2().setVisible(false);
        graphScene.getCompareControl2().setValue("No Filter");
        graphScene.getCompareControl3().setVisible(false);
        graphScene.getCompareControl3().setValue("No Filter");
      }
    });

    graphScene.getCompareControl2().setOnAction(event -> {
      if (!Objects.equals(graphScene.getCompareControl2().getValue(), "No Filter")) {
        graphScene.getCompareControl3().setVisible(true);
      } else {
        graphScene.getCompareControl3().setVisible(false);
        graphScene.getCompareControl3().setValue("No Filter");
      }
      if (!graphScene.getCompareControl3().isVisible()) {
        graphScene.getCompareControl3().setVisible(false);
      }
    });

    graphScene.getCompareControl3().setOnAction(event -> {
      System.out.println("Compare Control 3: " + graphScene.getCompareControl3().getValue());
    });

    //Checkbox listeners
    graphScene.getMaleCheckBox().setOnAction(event -> {
      if (graphScene.getFemaleCheckBox().isSelected()) {
        graphScene.getFemaleCheckBox().setSelected(false);
      }
    });
    graphScene.getFemaleCheckBox().setOnAction(event -> {
      if (graphScene.getMaleCheckBox().isSelected()) {
        graphScene.getMaleCheckBox().setSelected(false);
      }
    });


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