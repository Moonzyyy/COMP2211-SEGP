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
public class AdViz {
    //private final Model theModel;
    //private final Controller theController;

    public AdViz() {
        /*this.theModel = new Model();
//        this.theView = new AppView();
        this.theController = new Controller(theModel);
//        this.theView.setController(theController);
        theModel.importData();*/
    }

    /**
     * The main method of the application.
     */
    public void main() {

      try {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:identifier.sqlite");
        if (conn.isClosed()) {
          System.out.println("Connection is closed");
        } else {
          System.out.println("Connection is open");
        }


        conn.setAutoCommit(false);

        PreparedStatement statement = conn.prepareStatement(
            "INSERT INTO impressions VALUES(?,?,?,?,?,?,?)");
        InputStream inputStream = getClass().getResourceAsStream("/testdata/impression_log.csv");
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        br.readLine();
        final int batchSize = 50000;
        int count = 0;
        String line;
        while ((line = br.readLine()) != null) {
          String[] values = line.split(",");
          statement.setString(1, values[0]);
          statement.setString(2, values[1]);
          statement.setString(3, values[2]);
          statement.setString(4, values[3]);
          statement.setString(5, values[4]);
          statement.setString(6, values[5]);
          statement.setString(7, values[6]);
          statement.addBatch();
          count++;
          if (count % batchSize == 0) {
            statement.executeBatch();
            count = 0;
          }
        }
        statement.executeBatch();
        conn.setAutoCommit(true);
        statement.close();
        br.close();
        conn.close();
        System.out.println("DONE!!");

      }
      catch(Exception e)
      {

      }

        /*try (Connection conn = DriverManager.getConnection("jdbc:hsqldb:mem:testDb", "sa", "");)
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
            PreparedStatement users = conn.prepareStatement("INSERT IGNORE INTO users VALUES (?, ?, ?, ?, ?)");
            PreparedStatement impressions = conn.prepareStatement("INSERT IGNORE INTO impressions VALUES (?, ?, ?)");
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

                int context = 0;
                switch (values[5]) {
                    case "News" -> context = 0;
                    case "Shopping" -> context = 1;
                    case "Social Media" -> context = 2;
                    case "Blog" -> context = 3;
                    case "Hobbies" -> context = 4;
                    case "Travel" -> context = 5;
                }
                long id = Long.parseLong(values[1]);
                Timestamp ts = Timestamp.valueOf(values[0]);
                users.setLong(1, id);
                users.setBoolean(2, values[2].equals("male"));
                users.setInt(3, age);
                users.setInt(4, income);
                users.setInt(5, context);
                users.addBatch();
                impressions.setLong(1, id);
                impressions.setTimestamp(2, ts);
                impressions.setDouble(3, Double.parseDouble(values[6]));
                impressions.addBatch();
                count++;
                if (count % batchSize == 0) {
                    users.executeBatch();
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
//        launch();*/
    }

    /**
     * The start method of the application.
     * @param stage the stage of the application
     * @throws Exception if an exception occurs
     */
    /*@Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("AdViz");
        StartMenu sm = new StartMenu();
        theController.setStage(stage);
        theController.setUpScene(sm);
//        this.theController.setHandler((StartMenu) theView.getCurrentScene());
    }*/
}
