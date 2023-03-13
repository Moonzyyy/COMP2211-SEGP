package model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.util.Pair;

public class Model {
  private HashMap<Long, User> users = null;
  private ArrayList<Pair<LocalDateTime, Double>> impressions;
  private ArrayList<Pair<LocalDateTime, Double>> clicks;
  private ArrayList<Server> servers;

  private int bounces;
  //private List<Click> clicks = null;
  //private List<Server> serverInteractions = null;
  private final DecimalFormat df = new DecimalFormat("#.###");
  private double clickCost;

  public Model() {}

  public void importData() {
    //Get CSV data from all 3 log files (can be changed to for loop)
    CsvReader cr = new CsvReader();
    try {
      this.users = cr.getUsers();
      this.impressions = users.values().stream().parallel().flatMap(u -> u.getImpressions().stream()).collect(Collectors.toCollection(ArrayList<Pair<LocalDateTime, Double>>::new));
      this.clicks = users.values().stream().parallel().flatMap(u -> u.getClicks().stream()).collect(Collectors.toCollection(ArrayList<Pair<LocalDateTime, Double>>::new));;
      this.servers = users.values().stream().parallel().flatMap(u -> u.getServers().stream()).collect(Collectors.toCollection(ArrayList<Server>::new));;
      this.clickCost = clicks.stream().mapToDouble(Pair::getValue).sum();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
  public int totalImpressions()
  {
    return this.impressions.size();
  }

  public int totalClicks(){
    return this.clicks.size();
  }

  //Return number of uniques; (Distinct IDs from Click log)
  public int numberOfUniques()
  {
    return (int) users.values().stream().parallel().filter(u -> u.getClicks().size() > 0).count();
  }

  // predicate to filter the duplicates by the given key extractor
  private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
    Set<Object> seen = ConcurrentHashMap.newKeySet();
    return t -> seen.add(keyExtractor.apply(t));
  }

  //Return number of Conversions; (Conversions which are true)
  public int numberOfConversions()
  {
    return (int) servers.stream().parallel().filter(Server::getConversion).count();
//        return (int) serverInteractions.stream().filter(Server::getConversion).count();
  }

  //Bounce is defined by user in later sprints. For now keep it as number of page viewed = 1;
  public int numberOfBounces()
  {
    this.bounces = (int) servers.stream().parallel().filter(server -> server.getPagesViewed() <= 1).count();
    return this.bounces;
  }

  //Bounce Rate:	The	average	number of bounces per click
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public double bounceRate()
  {
    return Double.parseDouble(df.format((double) this.bounces / (double) totalClicks()));
  }

  //TotalCost = Click Cost + Impression Cost
  public double totalCost()
  {
    return Double.parseDouble(df.format(this.clickCost + impressions.stream().parallel().mapToDouble(Pair::getValue).sum()));
  }

  //Click-through-rate	(CTR):	The	average	number	of	clicks	per	impression
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public Double clickThroughRate()
  {
    return Double.parseDouble(df.format((double) totalClicks() / (double) totalImpressions()));
  }

  //Cost-per-click	(CPC):	The	average	amount	of	money spent	on	an	advertising	campaign	for	each click
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public Double costPerClick()
  {
    return Double.parseDouble(df.format(this.clickCost / (double) totalClicks()));
  }

  //Cost-per-acquisition	(CPA):	The	average	amount	of	money	spent	on	an	advertising	campaign for	each	acquisition	(i.e.,	conversion).
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public Double costPerAcquisition()
  {
//    long in = System.currentTimeMillis();
    Double lol = Double.parseDouble(df.format( totalCost() / (double) numberOfConversions()));
//    long out = System.currentTimeMillis();
//    System.out.print(out - in);
    return lol;
  }

  //Cost-per-thousand-impressions(CPM): The average amount of money spent on an advertising campaign for every 1000 impressions.
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public Double costPerThousandImps()
  {
    return Double.parseDouble(df.format(totalCost() / (double) totalImpressions()));
  }

  public ArrayList<String> getMetrics() {
    ArrayList<String> metrics = new ArrayList<String>();
    metrics.add(Integer.toString(totalImpressions()));
    metrics.add(Integer.toString(totalClicks()));
    metrics.add(Integer.toString(numberOfBounces()));
    metrics.add(Integer.toString(numberOfConversions()));
    metrics.add(Double.toString(totalCost()));
    metrics.add(Double.toString(clickThroughRate()));
    metrics.add(Double.toString(costPerAcquisition()));
    metrics.add(Double.toString(costPerClick()));
    metrics.add(Double.toString(costPerThousandImps()));
    metrics.add(Double.toString(bounceRate()));
    metrics.add(Integer.toString(numberOfUniques()));
    return metrics;
  }

}
