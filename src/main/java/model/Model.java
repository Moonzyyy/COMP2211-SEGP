package model;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.util.Pair;

public class Model {
  private HashMap<Long, User> users;

  private final ArrayList<Double> metrics = new ArrayList<>(11);

  private int bounces;
  private final DecimalFormat df = new DecimalFormat("#.###");
  private double clickCost;

  public Model() {}

  public void importData() {
    //Get CSV data from all 3 log files (can be changed to for loop)
    CsvReader cr = new CsvReader();
    try {
      this.users = cr.getUsers();
      this.clickCost = getClicks().mapToDouble(Pair::getValue).sum();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public Stream<Pair<LocalDateTime, Double>> getImpressions() {
    return users.values().stream().parallel().flatMap(u -> u.getImpressions().stream());
  }

  public Stream<Pair<LocalDateTime, Double>> getClicks() {
    return users.values().stream().parallel().flatMap(u -> u.getClicks().stream());
  }
  public Stream<Server> getServers() {
    return users.values().stream().parallel().flatMap(u -> u.getServers().stream());
  }
  public int totalImpressions()
  {
//    return this.impressions.size();
    return (int) this.getImpressions().count();
  }

  public int totalClicks(){
    return (int) getClicks().count();
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
    return (int) getServers().filter(Server::getConversion).count();
//        return (int) serverInteractions.stream().filter(Server::getConversion).count();
  }

  //Bounce is defined by user in later sprints. For now keep it as number of page viewed = 1;
  public int numberOfBounces()
  {
    this.bounces = (int) getServers().filter(server -> server.getPagesViewed() <= 1).count();
    return this.bounces;
  }

  //Bounce Rate:	The	average	number of bounces per click
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public double bounceRate()
  {
    return Double.parseDouble(df.format((double) this.bounces / metrics.get(1)));
  }

  //TotalCost = Click Cost + Impression Cost
  public double totalCost()
  {
//    return Double.parseDouble(df.format(this.clickCost + impressions.stream().parallel().mapToDouble(Pair::getValue).sum()));
    return Double.parseDouble(df.format(this.clickCost + getImpressions().mapToDouble(Pair::getValue).sum()));
  }

  //Click-through-rate	(CTR):	The	average	number	of	clicks	per	impression
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public Double clickThroughRate()
  {
    return Double.parseDouble(df.format(metrics.get(1) / metrics.get(0)));
  }

  //Cost-per-click	(CPC):	The	average	amount	of	money spent	on	an	advertising	campaign	for	each click
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public Double costPerClick()
  {
    return Double.parseDouble(df.format(this.clickCost / metrics.get(1)));
  }

  //Cost-per-acquisition	(CPA):	The	average	amount	of	money	spent	on	an	advertising	campaign for	each	acquisition	(i.e.,	conversion).
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public Double costPerAcquisition()
  {
    return Double.parseDouble(df.format( metrics.get(4) / (double) metrics.get(3)));
  }

  //Cost-per-thousand-impressions(CPM): The average amount of money spent on an advertising campaign for every 1000 impressions.
  //Convert ints to doubles before doing any calculations
  //return double in 3.dp
  public Double costPerThousandImps()
  {
    return Double.parseDouble(df.format(metrics.get(4) / (double) metrics.get(0)));
//    return Double.parseDouble(df.format(metrics.get(4) / (double) metrics.get(0)));
  }

  public ArrayList<String> getMetrics() {
    metrics.add((double) totalImpressions());
    metrics.add((double) totalClicks());
    metrics.add((double) numberOfBounces());
    metrics.add((double) numberOfConversions());
    metrics.add(totalCost());
    metrics.add(clickThroughRate());
    metrics.add(costPerAcquisition());
    metrics.add(costPerClick());
    metrics.add(costPerThousandImps());
    metrics.add(bounceRate());
    metrics.add((double) numberOfUniques());
    return metrics.stream().map(m -> Double.toString(m)).collect(Collectors.toCollection(ArrayList::new));
  }

}
