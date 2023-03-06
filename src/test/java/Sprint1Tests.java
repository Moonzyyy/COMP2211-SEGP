import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import model.CsvReader;
import model.Model;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Sprint1Tests {
  static Model model;

  @BeforeAll
  static void setup()
  {
    long start = System.currentTimeMillis();
    model = new Model();
    long end = System.currentTimeMillis();

    if(end - start > 30000)
    {
      fail("Processing Data took to long. Expected to take less than 30 seconds for all three files."
          + "\nActual Time taken: " + (end-start));
    }

  }

  @Test
  void UserStory4()
  {
    assertEquals(8828248, model.totalImpressions(), "Total Impressions:");
  }

  @Test
  void UserStory5()
  {
    assertEquals(17708, model.numberOfUniques(), "Number of Uniques:");
  }

  @Test
  void UserStory6()
  {
    assertEquals(684, model.numberOfConversions(), "Number of Conversions:");
  }

  @Test
  void UserStory7()
  {
    assertEquals(17754, model.totalClicks(), "Total Clicks:");
  }

  @Test
  void UserStory8()
  {
    assertEquals(8122, model.numberOfBounces(), "Number of Bounces (Only 1 Page Viewed:");
  }

  @Test
  void UserStory9()
  {
    if(model.bounceRate() != 0.457)
    {
      fail("Bounce rate is not calculated correctly."
          + "\n Expected: 0.457 (3 dp) || Actual: " + model.bounceRate()
          + "\n Total Clicks: 17754 || Number of Bounces: 8122");
    }
  }

  @Test
  void UserStory10()
  {
    if(model.totalCost() != 113545.694)
    {
      fail("Total cost is not calculated correctly."
          + "\n Expected: 113545.694 (3 dp) || Actual: " + model.totalCost()
          + "\n Click Costs: 103303.5723070003 || Impression Cost: 10242.12170000238");
    }
  }

  @Test
  void UserStory14()
  {
    if(model.clickThroughRate() != 0.002)
    {
      fail("Click-Through-Rate is not calculated correctly."
          + "\n Expected: 0.002 (3 dp) || Actual: " + model.clickThroughRate()
          + "\n Total Clicks: 17754 || Impression: 8828248");
    }
  }

  @Test
  void UserStory15()
  {
    if(model.costPerClick() != 5.819)
    {
      fail("cost-per-click is not calculated correctly."
          + "\n Expected: 5.819 (3 dp) || Actual: " + model.costPerClick()
          + "\n Click Cost: 103303.5723070003 || Total Clicks: 17754");
    }
  }

  @Test
  void UserStory20()
  {
    if(model.costPerAcquisition() != 166.002)
    {
      fail("Cost-per-acquisition is not calculated correctly."
          + "\n Expected: 166.002(3 dp) || Actual: " + model.costPerAcquisition()
          + "\n Number of Conversions: 684 || Total Cost: 113545.694");
    }
  }

}
