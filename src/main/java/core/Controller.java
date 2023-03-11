package core;

import java.util.List;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import model.Model;
import one.microstream.afs.nio.types.NioFileSystem;
import one.microstream.storage.embedded.types.EmbeddedStorage;
import one.microstream.storage.embedded.types.EmbeddedStorageManager;
import view.components.DashboardComp;
import view.scenes.AbstractScene;
import view.scenes.Dashboard;
import view.scenes.Graph;
import view.scenes.Loading;
import view.scenes.Settings;
import view.scenes.StartMenu;

public class Controller {

  private final Model model;
  private AbstractScene currentScene;
  private Stage stage;

  private static Controller instance;

  private volatile EmbeddedStorageManager storageManager;


  /**
   * The constructor of the controller.
   *
   * @param model the model of the application
   */
  public Controller(Model model) {
    this.model = model;
    Controller.instance = this;
  }

  public EmbeddedStorageManager storageManager()
  {
    /*
     * Double-checked locking to reduce the overhead of acquiring a lock
     * by testing the locking criterion.
     * The field (this.storageManager) has to be volatile.
     */
    if(this.storageManager == null)
    {
      synchronized(this)
      {
        if(this.storageManager == null)
        {
          this.storageManager = this.createStorageManager();
        }
      }
    }

    return this.storageManager;
  }

  private EmbeddedStorageManager createStorageManager() {
    NioFileSystem fileSystem = NioFileSystem.New();
    EmbeddedStorageManager storageManager = EmbeddedStorage.start(fileSystem.ensureDirectoryPath("data"));
    DataRoot rootInstance;

    if (storageManager.root() == null) {
      rootInstance = new DataRoot("SEG Test");
      storageManager.setRoot(rootInstance);
      storageManager.storeRoot();
    }
    return storageManager;
  }

  /**
   * Gets the {@link DataRoot} root object of this demo.
   * This is the entry point to all the data used in this application, basically the "database".
   *
   * @return the {@link DataRoot} root object of this demo
   */
  public DataRoot data()
  {
    return (DataRoot)this.storageManager().root();
  }

  /**
   * Shuts down the {@link EmbeddedStorageManager} of this demo.
   */
  public synchronized void shutdown()
  {
    if(this.storageManager != null)
    {
      this.storageManager.shutdown();
      this.storageManager = null;
    }
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
      //Load the data, switch to a loading screen, then switch to the dashboard
      Task task = new Task<Void>() {
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

//      setUpScene(new Dashboard());
    });

    menu.getSettingsButton().setOnAction((event) -> {
      setUpScene(new Settings());
    });


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
        setUpScene(new Graph(finalI));
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
   * @return the single instance of this class
   */
  public static Controller getInstance()
  {
    return instance;
  }

}
