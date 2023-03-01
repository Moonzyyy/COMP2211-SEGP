package csvTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;

public class csvReader {

    private static final String IMPRESSION_LOG_FILEPATH = "C:\\Users\\danie\\OneDrive\\Desktop\\SEGCOURSEWORK\\week_campaign";

    public static void main(String[] args) {
        connector();
    }

    public static void connector()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\danie\\OneDrive\\Desktop\\SEGCOURSEWORK\\src\\Database\\identifier.sqlite");
            if(conn.isClosed())
            {
                System.out.println("Connection is closed");
            }
            else
            {
                System.out.println("Connection is open");
            }

            Statement stat = conn.createStatement();
            stat.execute("Delete From myDatabase");

            PreparedStatement statement = conn.prepareStatement("INSERT INTO myDatabase VALUES(?,?,?,?,?,?,?)");
            BufferedReader br = new BufferedReader(new FileReader(IMPRESSION_LOG_FILEPATH));
            br.readLine();
            final int batchSize = 10000;
            int count = 0;
            String line;
            while ( (line=br.readLine()) != null)
            {
                String[] values = line.split(",");
                statement.setString(1, values[0]);
                statement.setString(2, values[1]);
                statement.setString(3, values[2]);
                statement.setString(4, values[3]);
                statement.setString(5, values[4]);
                statement.setString(6, values[5]);
                statement.setString(7, values[6]);
                statement.addBatch();
                if (++count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            stat.close();
            statement.executeBatch();
            statement.close();
            br.close();


        }
        catch (Exception e)
        {
            System.out.println("Error = " + e.getMessage());
        }

    }
}
