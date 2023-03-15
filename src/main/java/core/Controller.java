package core;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.concurrent.Task;
import javafx.stage.Stage;
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
          case 0:
            title = "Impressions Over Time";
            xAxisName = "Date";
            yAxisName = "Impressions";
            data = model.loadImpressionData();
            break;
          case 1:
            title = "Clicks Over Time";
            xAxisName = "Date";
            yAxisName = "Clicks";
            data = model.loadClicksData();
            break;
          case 2:
            title = "Bounces Over Time";
            xAxisName = "Date";
            yAxisName = "Bounces";
            data = model.loadBouncesData();
            break;
          case 3:
            title = "Conversions Over Time";
            xAxisName = "Date";
            yAxisName = "Conversions";
            data = model.loadConversionData();
            break;
          case 4:
            title = "Click Cost Over Time";
            xAxisName = "Date";
            yAxisName = "Click Costs";
            data = model.loadClickCostData();
            break;
          case 5:
            title = "Click-through-rate Over Time";
            xAxisName = "Date";
            yAxisName = "Click-through-rate";
            data = model.loadCTRData();
            break;
          case 6:
            title = "Cost-per-acquisition Over Time";
            xAxisName = "Date";
            yAxisName = "Cost-per-acquisition";
            data = new HashMap<>();
            break;
          case 7:
            title = "Cost-per-click Over Time";
            xAxisName = "Date";
            yAxisName = "Cost-per-click";
            data = new HashMap<>();
            break;
          case 8:
            title = "Cost-per-thousand impressions Over Time";
            xAxisName = "Date";
            yAxisName = "Cost-per-thousand impressions";
            data = new HashMap<>();
            break;
          case 9:
            title = "Bounce Rate Over Time";
            xAxisName = "Date";
            yAxisName = "Bounce Rate";
            data = new HashMap<>();
            break;
          case 10:
            title = "Uniques Over Time";
            xAxisName = "Date";
            yAxisName = "Uniques";
            data = new HashMap<>();
            break;
          default:
            data = new HashMap<>();
        }
        setUpScene(new Graph(finalI,title,xAxisName,yAxisName,data));
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
      var file = importScene.getFileChooser().showOpenDialog(stage);
      importScene.getFileChooser().setInitialDirectory(file.getParentFile());
      model.setClicksFile(file);
      importScene.getClickFileName().setText(file.getName());
//      if (clicksFile != null) {
//        importScene.getClicksTextField().setText(clicksFile.getName());
//      } else {
//        importScene.getClicksTextField().setText("No file selected");
//      }
      importScene.getLoadButton().setDisable(
          model.getClicksFile() == null || model.getImpressionsFile() == null
              || model.getServerFile() == null);

    });

    importScene.getImportImpressions().setOnAction((event) -> {
      var file = importScene.getFileChooser().showOpenDialog(stage);
      importScene.getFileChooser().setInitialDirectory(file.getParentFile());
      model.setImpressionsFile(file);
      importScene.getImpressionFileName().setText(file.getName());
      importScene.getLoadButton().setDisable(
          model.getClicksFile() == null || model.getImpressionsFile() == null
              || model.getServerFile() == null);
    });

    importScene.getImportServer().setOnAction((event) -> {
      var file = importScene.getFileChooser().showOpenDialog(stage);
      importScene.getFileChooser().setInitialDirectory(file.getParentFile());
      model.setServerFile(file);
      importScene.getServerFileName().setText(file.getName());
      importScene.getLoadButton().setDisable(
          model.getClicksFile() == null || model.getImpressionsFile() == null
              || model.getServerFile() == null);
    });

    importScene.getLoadButton().setDisable(
        model.getClicksFile() == null || model.getImpressionsFile() == null
            || model.getServerFile() == null);

    importScene.getLoadButton().setOnAction((event) -> {
      Task<Void> task = new Task<>() {
        @Override
        public Void call() {
          model.importData();
          return null;
        }
      };
      setUpScene(new Loading());
      task.setOnSucceeded(e -> {
        setUpScene(new Dashboard());
      });
      Thread thread = new Thread(task);
      thread.start();
    });
  }
}
