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
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();

            String agesTbl = "CREATE TABLE ages (id TINYINT IDENTITY PRIMARY KEY, label VARCHAR(5))";
            String incomesTbl = "CREATE TABLE incomes (id TINYINT IDENTITY PRIMARY KEY, label VARCHAR(6))";
            String contextsTbl = "CREATE TABLE contexts (id TINYINT IDENTITY PRIMARY KEY, label VARCHAR(12))";
            String userTbl = "CREATE TABLE users (id BIGINT PRIMARY KEY, gender BOOLEAN, age TINYINT FOREIGN KEY REFERENCES ages, income TINYINT FOREIGN KEY REFERENCES incomes, context TINYINT FOREIGN KEY REFERENCES contexts)";
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
            stmt.execute("INSERT INTO ages (label) VALUES ('<25'), ('25-34'), ('35-44'), ('45-54'), ('>54')");
            stmt.execute("INSERT INTO incomes (label) VALUES ('Low'), ('Medium'), ('High')");
            stmt.execute("INSERT INTO contexts (label) VALUES ('News'), ('Shopping'), ('Social Media'), ('Blog'), ('Hobbies'), ('Travel')");
            stmt.execute("SET DATABASE SQL SYNTAX MYS TRUE");

            InputStream impression_log = AdViz.class.getResourceAsStream("/testData/impression_log.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(impression_log));
            br.readLine();
//            Stream<String[]> uniques = br.lines().skip(1).map(line -> line.split(",")).filter(distinctByKey(x -> x[1]));

            final int batchSize = 50000;
//            ArrayList<Long> existing = new ArrayList<Long>();

            PreparedStatement users = conn.prepareStatement("INSERT IGNORE INTO users VALUES (?, ?, ?, ?, ?)");
            PreparedStatement impressions = conn.prepareStatement("INSERT IGNORE INTO impressions VALUES (?, ?, ?)");
            int count = 0;
            String line;

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int age = 0;
                switch (values[3]) {
                    case "<25" -> age = 0;
                    case "25-34" -> age = 1;
                    case "35-44" -> age = 2;
                    case "45-54" -> age = 3;
                    case ">54" -> age = 4;
                }

                int income = 0;
                switch (values[4]) {
                    case "Low" -> income = 0;
                    case "Medium" -> income = 1;
                    case "High" -> income = 2;
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
            stmt.close();
            users.close();
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
