package core;

import javafx.application.Application;
import javafx.stage.Stage;
import model.Model;
import view.scenes.StartMenu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.stream.Stream;

import static model.CsvReader.distinctByKey;

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

        try (Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:testDb", "sa", "");)
        {
            if(!conn.isClosed())
            {
                System.out.println("Connection Established");
            }
            Statement stmt = conn.createStatement();
            conn.setAutoCommit(false);
            String impressionTbl = "CREATE TABLE impressions (date VARCHAR(64) , id VARCHAR(64), gender VARCHAR(64), age VARCHAR(64), Income VARCHAR(64), context VARCHAR(64), ImpressionCost REAL)";
            //String clickTbl = "CREATE TABLE clicks (id BIGINT FOREIGN KEY REFERENCES users, date TIMESTAMP, cost double)";
            //String serverTbl = "CREATE TABLE server (id BIGINT FOREIGN KEY REFERENCES users, entryDate TIMESTAMP, exitDate TIMESTAMP, pagesViewed int, converted BOOLEAN)";
            stmt.execute(impressionTbl);
            //stmt.execute(clickTbl);
            //stmt.execute(serverTbl);


            InputStream impression_log = AdViz.class.getResourceAsStream("/testData/impression_log.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(impression_log));
            br.readLine();
//            Stream<String[]> uniques = br.lines().skip(1).map(line -> line.split(",")).filter(distinctByKey(x -> x[1]));

            final int batchSize = 50000;
//            ArrayList<Long> existing = new ArrayList<Long>();

            PreparedStatement impressions = conn.prepareStatement("INSERT INTO impressions VALUES (?, ?, ? , ?, ? , ?, ?)");
            int count = 0;
            String line;

            while ((line = br.readLine()) != null) {
              String[] values = line.split(",");

              impressions.setString(1, values[0]);
              impressions.setString(2, values[1]);
              impressions.setString(3, values[2]);
              impressions.setString(4, values[3]);
              impressions.setString(5, values[4]);
              impressions.setString(6, values[5]);
              impressions.setString(7, values[6]);

              impressions.addBatch();
              count++;
              if (count % batchSize == 0) {
                impressions.executeBatch();
                count = 0;
              }
            }
            System.out.println(stmt.executeQuery("SELECT Count(ID) FROM impressions"));
            stmt.close();
            impressions.close();
            conn.close();
            System.out.println("DONE!!!!");

        }  catch (SQLException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
