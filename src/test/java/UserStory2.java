import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import model.Click;
import model.CsvReader;
import model.Impression;
import model.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserStory2
{

  static CsvReader csvReader;
  static Impression impression;
  static Click click;
  static Server server;
  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  @BeforeAll
  static void setup()
  {
    long start = System.currentTimeMillis();
    csvReader = new CsvReader();
    long end = System.currentTimeMillis();

    if(end - start > 30000)
    {
      fail("Processing Data took to long. Expected to take less than 30 seconds for all three files."
          + "\nActual Time taken: " + (end-start));
    }
    impression = csvReader.getImpressions().get(0);
    click = csvReader.getClicks().get(0);
    server = csvReader.getServerInteractions().get(0);

  }

  @Test
  void testIfAllDataProcessed()
  {
    assertEquals(8828248, csvReader.getImpressions().size(), "ImpressionList Size:");
    assertEquals(17754, csvReader.getClicks().size(), "ClickList Size:");
    assertEquals(17754, csvReader.getServerInteractions().size(), "ServerList Size:");
  }

  @Test
  void testIfAllDataInCorrectType()
  {
    //Impressions
    assertTrue(LocalDateTime.class.isInstance(impression.getDate()), "Impression: Date is type LocalDateTime");
    assertTrue(String.class.isInstance(impression.getUserId()),"Impression: ID is type String");
    assertTrue(String.class.isInstance(impression.getGender()),"Impression: Gender is type String");
    assertTrue(String.class.isInstance(impression.getAge()),"Impression: Age is type String");
    assertTrue(String.class.isInstance(impression.getIncome()),"Impression: Income is type String");
    assertTrue(String.class.isInstance(impression.getContext()),"Impression: Context is type String");
    assertTrue(Double.class.isInstance(impression.getImpressionCost()),"Impression: Impression Cost is type Double");

    //Clicks
    assertTrue(LocalDateTime.class.isInstance(click.getDate()), "Click: Date is type LocalDateTime");
    assertTrue(String.class.isInstance(click.getUserId()),"Click: ID is type String");
    assertTrue(Double.class.isInstance(click.getClickCost()),"Click: Click Cost is type Double");

    //Server
    assertTrue(LocalDateTime.class.isInstance(server.getEntryDate()), "Server: Entry Date is type LocalDateTime");
    assertTrue(String.class.isInstance(server.getUserId()),"Server: ID is type String");
    assertTrue(Integer.class.isInstance(server.getTimeSpent()),"Server: Time Spent is type int");
    assertTrue(Integer.class.isInstance(server.getPagesViewed()),"Server: Pages Viewed is type int");
    assertTrue(Boolean.class.isInstance(server.getConversion()),"Server: Conversion is type Boolean");


  }

  @Test
  void testIfLogsCorrectlyImported()
  {

    //Impression: 2015-01-01 12:00:00,2514223013017308160,Male,>54,High,Blog,0.000000
    assertEquals(LocalDateTime.parse("2015-01-01 12:00:00", formatter), impression.getDate(), "Impression: Date of first user:");
    assertEquals("2514223013017308160", impression.getUserId(), "Impression: ID of first user:");
    assertEquals("Male", impression.getGender(), "Impression: Gender of first user:");
    assertEquals(">54", impression.getAge(), "Impression: Age of first user:");
    assertEquals("High", impression.getIncome(), "Impression: Income of first user:");
    assertEquals("Blog", impression.getContext(), "Impression: Blog of first user:");
    assertEquals(0.0, impression.getImpressionCost(), "Impression: Impression Cost of first user:");

    //Click: 2015-01-01 12:03:01,6122207195878292480,10.266780
    assertEquals(LocalDateTime.parse("2015-01-01 12:03:01", formatter), click.getDate(), "Click: Date of first user:");
    assertEquals("6122207195878292480", click.getUserId(), "Click: ID of first user:");
    assertEquals(10.266780, click.getClickCost(),"Click: Click Cost of first user:" );

    //Server: 2015-01-01 12:03:01,6122207195878292480,2015-01-01 12:05:02,9,No
    assertEquals(LocalDateTime.parse("2015-01-01 12:03:01", formatter), server.getEntryDate(), "Server: Entry Date of first user:");
    assertEquals("6122207195878292480", server.getUserId(), "Server: ID of first user:");
    assertEquals(121, server.getTimeSpent(), "Server: Time Spent in seconds of first user:");
    assertEquals(9, server.getPagesViewed(), "Server: Amount of Pages Viewed by first user:");
    assertEquals(false, server.getConversion(), "Server: Conversion of first user:");


  }



}
