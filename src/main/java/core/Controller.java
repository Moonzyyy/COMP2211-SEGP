package core;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
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

  public static void sendErrorMessage(String message) {
    // Need to see if javafx has an alert object?
    JOptionPane.showMessageDialog(null, message, "Error!", JOptionPane.ERROR_MESSAGE);
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  /**
   * Sets the current scene. Acts as a scene switcher.
   *
   * @param newScene the new scene to be set
   */
  private void setCurrentScene(AbstractScene newScene) {
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
        Map<Date, Double> data;
        String title = "";
        String xAxisName = "";
        String yAxisName = "";
        switch (finalI) {
          case 0 -> {
            title = "Impressions Over Time";
            xAxisName = "Date";
            yAxisName = "Impressions";
            data = model.loadImpressionData();
          }
          case 1 -> {
            title = "Clicks Over Time";
            xAxisName = "Date";
            yAxisName = "Clicks";
            data = model.loadClicksData();
          }
          case 2 -> {
            title = "Bounces Over Time";
            xAxisName = "Date";
            yAxisName = "Bounces";
            data = model.loadBouncesData();
          }
          case 3 -> {
            title = "Conversions Over Time";
            xAxisName = "Date";
            yAxisName = "Conversions";
            data = model.loadConversionData();
          }
          case 4 -> {
            title = "Click Cost Over Time";
            xAxisName = "Date";
            yAxisName = "Click Costs";
            data = model.loadClickCostData();
          }
          case 5 -> {
            title = "Click-through-rate Over Time";
            xAxisName = "Date";
            yAxisName = "Click-through-rate";
            data = model.loadCTRData();
          }
          case 6 -> {
            title = "Cost-per-acquisition Over Time";
            xAxisName = "Date";
            yAxisName = "Cost-per-acquisition";
            data = new HashMap<>();
          }
          case 7 -> {
            title = "Cost-per-click Over Time";
            xAxisName = "Date";
            yAxisName = "Cost-per-click";
            data = new HashMap<>();
          }
          case 8 -> {
            title = "Cost-per-thousand impressions Over Time";
            xAxisName = "Date";
            yAxisName = "Cost-per-thousand impressions";
            data = new HashMap<>();
          }
          case 9 -> {
            title = "Bounce Rate Over Time";
            xAxisName = "Date";
            yAxisName = "Bounce Rate";
            data = new HashMap<>();
          }
          case 10 -> {
            title = "Uniques Over Time";
            xAxisName = "Date";
            yAxisName = "Uniques";
            data = new HashMap<>();
          }
          default -> data = new HashMap<>();
        }
        setUpScene(new Graph(finalI, title, xAxisName, yAxisName, data));
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
  public void setUpScene(Graph graphScene) {
    graphScene.createScene();
    this.setCurrentScene(graphScene);

    //Button action listeners
    graphScene.getHomeButton().setOnAction((event) -> {
      setUpScene(new Dashboard());
    });

    graphScene.getCompareButton().setOnAction((event) -> {
      System.out.print("Compare button pressed");
    });

    graphScene.getPrintButton().setOnAction((event) -> {
      System.out.print("Print button pressed");
    });
  }

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
    File impressionFile = null;
    File costFile = null;

    importScene.createScene();
    this.setCurrentScene(importScene);

    //Button action listeners
    importScene.getBackButton().setOnAction((event) -> {
      setUpScene(new StartMenu());
    });

    //When each button is pressed, open a file browser and set the corresponding text field
    importScene.getImportClicks().setOnAction((event) -> {
      try {
        File file = importScene.getFileChooser().showOpenDialog(stage);
        if (file != null) {
          importScene.getFileChooser().setInitialDirectory(file.getParentFile());
        }

        model.setClicksFile(file);
        importScene.getClickFileName().setText(file.getName());
        importScene.getLoadButton().setDisable(
                model.getClicksFile() == null || model.getImpressionsFile() == null
                        || model.getServerFile() == null);
      } catch (Exception e) {
        sendErrorMessage("File selected is not of type .csv!");
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
        sendErrorMessage("File selected is not of type .csv!");
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
        sendErrorMessage("File selected is not of type .csv!");
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