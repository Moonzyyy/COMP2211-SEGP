package core;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.scenes.StartMenu;

import java.sql.*;

/**
 * The main class of the application.
 */
public class AdViz extends Application {
    private final Model theModel;
    private final Controller theController;

    public AdViz() {
        this.theModel = new Model();
//        this.theView = new AppView();
        this.theController = new Controller(theModel);
//        this.theView.setController(theController);
        theModel.importData();
    }

    /**
     * The main method of the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try (Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:testDb", "sa", "");
             Statement stmt = conn.createStatement();
        ){

            String agesTbl = "CREATE TABLE ages (id TINYINT NOT NULL, label VARCHAR(10), PRIMARY KEY (id))";
            String incomesTbl = "CREATE TABLE incomes (id TINYINT NOT NULL, label VARCHAR(10), PRIMARY KEY (id))";
            String contextsTbl = "CREATE TABLE contexts (id TINYINT NOT NULL, label VARCHAR(10), PRIMARY KEY (id))";
            String userTbl = "CREATE TABLE users (id BIGINT, gender BOOLEAN, age TINYINT FOREIGN KEY REFERENCES ages, income TINYINT FOREIGN KEY REFERENCES incomes, context TINYINT FOREIGN KEY REFERENCES contexts, PRIMARY KEY (id))";
            String impressionTbl = "CREATE TABLE impressions (id BIGINT FOREIGN KEY REFERENCES users, date TIMESTAMP, cost double)";
            String clickTbl = "CREATE TABLE clicks (id BIGINT FOREIGN KEY REFERENCES users, date TIMESTAMP, cost double)";
            String serverTbl = "CREATE TABLE server (id BIGINT FOREIGN KEY REFERENCES users, entryDate TIMESTAMP, exitDate TIMESTAMP, pagesViewed int, converted BOOLEAN)";
            stmt.execute(agesTbl);
            stmt.execute(incomesTbl);
            stmt.execute(contextsTbl);
            stmt.execute(userTbl);
            stmt.execute(impressionTbl);
            stmt.execute(clickTbl);
            stmt.execute(serverTbl);
            stmt.execute("INSERT INTO ages VALUES (1, 'test')");
            ResultSet rs = stmt.executeQuery("SELECT * FROM ages");
            while(rs.next()) {
                System.out.println(rs.getString("label"));
            }
        }  catch (SQLException e) {
            e.printStackTrace(System.out);
        }
//        AdViz app = new AdViz();
//        launch();
    }

    /**
     * The start method of the application.
     * @param stage the stage of the application
     * @throws Exception if an exception occurs
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("AdViz");
        StartMenu sm = new StartMenu();
        theController.setStage(stage);
        theController.setUpScene(sm);
//        this.theController.setHandler((StartMenu) theView.getCurrentScene());
    }
}
