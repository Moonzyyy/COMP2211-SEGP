package model;

import java.io.File;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Model {

    private static final Logger logger = LogManager.getLogger(Model.class);

    private HashMap<Long, User> users;

    private final ArrayList<Double> metrics = new ArrayList<>(11);

    private String bounceDef = "Page";
    private int bouncePageValue;
    private int bounceTimeValue;
    private int bounces;
    private final DecimalFormat df3 = new DecimalFormat("#.###");
  private final DecimalFormat df2 = new DecimalFormat("#.##");
    private double clickCost;
    private File clicksFile;
    private File impressionsFile;
    private File serverFile;
    private Map<String, FilterPredicate> predicates;
    private Predicate<User> predicate;
    private Map<String, Boolean> currentlySelected;


    public Model() {
        this.predicates = initPredicates();
        bouncePageValue = 1;
        bounceTimeValue = 1;
    }

    public boolean importData() {
        //Get CSV data from all 3 log files
        logger.info("Importing Data From CSV Files");
        CsvReader cr = new CsvReader(clicksFile, impressionsFile, serverFile);
        this.users = cr.getUsers();
        this.clickCost = getClicks().mapToDouble(Pair::getValue).sum();
        logger.info("Done Importing Data From CSV Files!");
        if (users.size() == 0) {
            return false;
        }
        return true;
    }

    public Stream<User> getUsers() {
        if (this.predicate != null) {
            return users != null ? users.values().stream().parallel().filter(this.predicate) : null;
        } else {
            return users != null ? users.values().stream().parallel() : null;
        }
    }

    /**
     * Gets every impression by folding every impression from every user.
     * @return stream of impressions
     */
    public Stream<Pair<LocalDateTime, Double>> getImpressions() {
        return users != null ? getUsers().flatMap(u -> u.getImpressions().stream()) : null;
    }

    /**
     * Gets every impression by folding every click from every user.
     * @return stream of click
     */
    public Stream<Pair<LocalDateTime, Double>> getClicks() {
        return users != null ? getUsers().flatMap(u -> u.getClicks().stream()) : null;
    }

    /**
     * Gets every impression by folding every server from every user.
     * @return stream of servers
     */
    public Stream<Server> getServers() {
        return users != null ? getUsers().flatMap(u -> u.getServers().stream()) : null;
    }

    /**
     * Counts every impression from the impression stream.
     * @return total number of impressions
     */
    public int totalImpressions()
    {
        return (int) this.getImpressions().count();
    }

    public Map<LocalDateTime, Double> loadImpressionData() {
        Map<LocalDateTime, Double> impressionsByDate = new HashMap<>();
        getImpressions().sequential().forEach(impression -> {
            LocalDateTime dateTime = impression.getKey();
            if (impressionsByDate.containsKey(dateTime)) {
                impressionsByDate.put(dateTime, impressionsByDate.get(dateTime) + 1.0);
            } else {
                impressionsByDate.put(dateTime, 1.0);
            }
        });
        return impressionsByDate;
    }


    /**
     * Counts every click from the click stream.
     * @return total number of clicks
     */
    public int totalClicks(){
        return (int) this.getClicks().count();
    }

  /**
   * @return load the click data by Date
   */
    public Map<LocalDateTime, Double> loadClicksData() {
        Map<LocalDateTime, Double> clickCountsByDate = new HashMap<>();
        getClicks().sequential().forEach(click -> {
            LocalDateTime dateTime = click.getKey();
            if (clickCountsByDate.containsKey(dateTime)) {
                clickCountsByDate.put(dateTime, clickCountsByDate.get(dateTime) + 1.0);
            } else {
                clickCountsByDate.put(dateTime, 1.0);
            }
        });
        return clickCountsByDate;
    }

    /**
     * Counts every unique ID from all the users.
     * @return total number of unique users
     */
    public int numberOfUniques()
    {
        return (int) getUsers().filter(u -> u.getClicks().size() > 0).count();
    }

  /**
   * @return load number of uniques by date
   */
    public Map<LocalDateTime, Double> loadNumberOfUniquesData() {
        Map<LocalDateTime, Double> numberOfUniquesByDate = new HashMap<>();
        getUsers().sequential().forEach(user -> {
            if (user.getClicks().size() > 0) {
                LocalDateTime dateTime = user.getClicks().get(0).getKey();
                if (numberOfUniquesByDate.containsKey(dateTime)) {
                    numberOfUniquesByDate.put(dateTime, numberOfUniquesByDate.get(dateTime) + 1.0);
                } else {
                    numberOfUniquesByDate.put(dateTime, 1.0);
                }
            }
        });
        return numberOfUniquesByDate;
    }

    /**
     * Counts every conversion from the server stream.
     * @return total number of conversions
     */
    public int numberOfConversions() {
        return (int) this.getServers().filter(Server::getConversion).count();
    }

  /**
   * @return load number of conversions by date
   */
    public Map<LocalDateTime, Double> loadConversionData() {
        Map<LocalDateTime,Double> conversionsByDate = new HashMap<>();
        getServers().sequential().forEach(server -> {
            LocalDateTime dateTime = server.getEntryDate();
            if (server.getConversion() && conversionsByDate.containsKey(dateTime)) {
                conversionsByDate.put(dateTime, conversionsByDate.get(dateTime) + 1);
            } else if (server.getConversion()) {
                conversionsByDate.put(dateTime, 1.0);
            }
        });
        return conversionsByDate;
    }

    /**
     * Depending on the bounce definition, calculates the bounces.
     * Gets the number of bounces from the server stream.
     * @return total number of bounces
     */
    public int numberOfBounces() {
        switch (bounceDef) {
            case "Default" -> this.bounces = (int) getServers().filter(server -> server.getPagesViewed() <= 1).count();
            case "Page" ->
                    this.bounces = (int) getServers().filter(server -> server.getPagesViewed() <= bouncePageValue).count();
            case "Time" ->
                    this.bounces = (int) getServers().filter(server -> server.getTimeSpent() <= bounceTimeValue).count();
        }
        return this.bounces;
    }

  /**
   * @return load bounces by date
   */
    public Map<LocalDateTime,Double> loadBouncesData() {
        Map<LocalDateTime, Double> bouncesByDate = new HashMap<>();
        getServers().sequential().forEach(server -> {
            LocalDateTime dateTime = server.getEntryDate();
            switch (bounceDef) {
                case "Default" -> {
                    if (server.getPagesViewed() <= 1 && bouncesByDate.containsKey(dateTime)) {
                        bouncesByDate.put(dateTime, bouncesByDate.get(dateTime) + 1);
                    } else if (server.getPagesViewed() <= 1) {
                        bouncesByDate.put(dateTime, 1.0);
                    }
                }
                case "Page" -> {
                    if (server.getTimeSpent() <= bouncePageValue && bouncesByDate.containsKey(dateTime)) {
                        bouncesByDate.put(dateTime, bouncesByDate.get(dateTime) + 1);
                    } else if (server.getTimeSpent() <= bouncePageValue) {
                        bouncesByDate.put(dateTime, 1.0);
                    }
                }
                case "Time" -> {
                    if (server.getPagesViewed() <= bounceTimeValue && bouncesByDate.containsKey(dateTime)) {
                        bouncesByDate.put(dateTime, bouncesByDate.get(dateTime) + 1);
                    } else if (server.getPagesViewed() <= bounceTimeValue) {
                        bouncesByDate.put(dateTime, 1.0);
                    }
                }
            }
//            if (!server.getConversion() && bouncesByDate.containsKey(dateTime)) {
//                bouncesByDate.put(dateTime, bouncesByDate.get(dateTime) + 1);
//            } else if (!server.getConversion()) {
//                bouncesByDate.put(dateTime, 1.0);
//            }
        });
        return bouncesByDate;
    }

    /**
     * Calculates the bounce rate, which is the average number of bounces per click.
     * This is calculated by dividing number of bounces by total clicks.
     * @return the bounce rate in 3 d.p.
     */
    public double bounceRate() {
        return Double.parseDouble(df3.format((double) numberOfBounces() / metrics.get(1)));
//        return Double.parseDouble(df3.format((double) metrics.get(3) / metrics.get(1)));
    }

  /**
   * @return Load Bounce Rate by date
   */
    public Map<LocalDateTime, Double> loadBounceRateData() {
        Map<LocalDateTime, Double> bounceRateByDate = new HashMap<>();
        Map<LocalDateTime, Double> clicksByDate = loadClicksData();
        Map<LocalDateTime, Double> bounceByDate = loadBouncesData();
        for (LocalDateTime date : clicksByDate.keySet()) {
            Double clicks = clicksByDate.getOrDefault(date, 1.0);
            Double bounces = bounceByDate.getOrDefault(date, 0.0);
            Double bounceRate = bounces / clicks;
            bounceRateByDate.put(date, bounceRate);
        }
        return bounceRateByDate;
    }

    /**
     * Calculates the total cost.
     * This is calculated by adding click and impression cost.
     * @return the total cost in 2 d.p.
     */
    public double totalCost() {
        return Double.parseDouble(df2.format(this.clickCost + getImpressions().mapToDouble(Pair::getValue).sum()));
    }

  /**
   * @return Load the total cost by date
   */
    public Map<LocalDateTime, Double> loadTotalCostData() {
        Map<LocalDateTime, Double> totalCostByDate = new HashMap<>();
        Map<LocalDateTime, Double> clickCountsByDate = loadClicksData();
        Map<LocalDateTime, Double> impressionCostByDate = new HashMap<>();
        getImpressions().sequential().forEach(impression -> {
            LocalDateTime dateTime = impression.getKey();
            Double clickCost = impression.getValue();
            if (impressionCostByDate.containsKey(dateTime)) {
                impressionCostByDate.put(dateTime, impressionCostByDate.get(dateTime) + clickCost);
            } else {
                impressionCostByDate.put(dateTime, clickCost);
            }
        });

        for (LocalDateTime date : impressionCostByDate.keySet()) {
            Double impressionCost = impressionCostByDate.getOrDefault(date, 0.0);
            Double clickCount = clickCountsByDate.getOrDefault(date, 0.0);
            Double totalCost = clickCount + impressionCost;
            totalCostByDate.put(date, totalCost);
        }
        return totalCostByDate;
    }

    /**
     * Calculates the click-through-rate, which is the average number of clicks per impression.
     * This is calculated by dividing total number of clicks by total impressions.
     * @return the click-through-rate in 3 d.p.
     */
    public Double clickThroughRate() {
        return Double.parseDouble(df3.format(metrics.get(1) / metrics.get(0)));
    }

  /**
   * @return load the CTR by date
   */
    public Map<LocalDateTime, Double> loadCTRData() {
        Map<LocalDateTime, Double> ctrByDate = new HashMap<>();
        Map<LocalDateTime, Double> impressionData = loadImpressionData();
        Map<LocalDateTime, Double> clickCountsByDate = loadClicksData();

        for (LocalDateTime date : impressionData.keySet()) {
            Double impressionCost = impressionData.getOrDefault(date, 0.0);
            Double clickCount = clickCountsByDate.getOrDefault(date, 0.0);
            Double ctr = clickCount / impressionCost;
            ctrByDate.put(date, ctr);
        }
        return ctrByDate;
    }

    /**
     * Calculates the cost-per-click, which is the average amount
     * of money spent on the campaign for each click.
     * This is calculated by dividing click cost by total clicks.
     * @return the cost-per-click in 2 d.p.
     */
    public Double costPerClick() {
        return Double.parseDouble(df2.format(this.clickCost / metrics.get(1)));
    }

  /**
   * @return load the cost per click by date
   */
    public Map<LocalDateTime, Double> loadCostPerClickData() {
        Map<LocalDateTime, Double> clickCostByDate = new HashMap<>();
        getClicks().sequential().forEach(click -> {
            LocalDateTime dateTime = click.getKey();
            Double clickCost = click.getValue();
            if (clickCostByDate.containsKey(dateTime)) {
                clickCostByDate.put(dateTime, clickCostByDate.get(dateTime) + clickCost);
            } else {
                clickCostByDate.put(dateTime, clickCost);
            }
        });
        return clickCostByDate;
    }

    /**
     * Calculates the cost-per-acquisition, which is the average amount
     * of money spent for each acquisition (also known as conversion).
     * This is calculated by dividing the total cost by number of conversions.
     * @return the cost-per-acquisition in 2 d.p.
     */
    public Double costPerAcquisition() {
        return Double.parseDouble(df2.format( metrics.get(4) / (double) metrics.get(3)));
    }


  /**
   * @return load the CPA by date
   */
    public Map<LocalDateTime, Double> loadCPAData() {
        Map<LocalDateTime, Double> cpaByDate = new HashMap<>();
        Map<LocalDateTime, Double> conversionData = loadConversionData();
        Map<LocalDateTime, Double> totalCostData = loadTotalCostData();

        for (LocalDateTime date : totalCostData.keySet()) {
            Double conversion = conversionData.getOrDefault(date, 1.0);
            Double totalCost = totalCostData.getOrDefault(date, 0.0);
            Double cpa = totalCost / conversion;
            cpaByDate.put(date, cpa);
        }
        return cpaByDate;
    }

    /**
     * Calculates the cost-per-thousand-impressions, which is the
     * average amount of money spent for every thousand impressions.
     * This is calculated by dividing the total cost by number of impressions x1000.
     * @return the cost-per-thousand-impressions in 2 d.p.
     */
    public Double costPerThousandImps()
    {
        return Double.parseDouble(df2.format((metrics.get(4) / (double) metrics.get(0) * 1000d)));
    }

  /**
   * @return load by CPTI by date
   */
    public Map<LocalDateTime, Double> loadCPTIData() {
        Map<LocalDateTime, Double> cptiByDate = new HashMap<>();
        Map<LocalDateTime, Double> impressionData = loadImpressionData();
        Map<LocalDateTime, Double> totalCostData = loadTotalCostData();

        for (LocalDateTime date : impressionData.keySet()) {
            Double impression = impressionData.getOrDefault(date, 1.0);
            Double totalCost = totalCostData.getOrDefault(date, 0.0);
            Double cpa = (totalCost / impression) * 1000d;
            cptiByDate.put(date, cpa);
        }
        return cptiByDate;
    }


    /**
     * Create arraylist of metrics from functions to persist after import
     * @return arraylist of metrics
     */
    public ArrayList<String> getMetrics() {
        metrics.clear();
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

  /**
   * @param id The ID of the data that will get returned
   * @param predicate what we use for filtering
   * @return the data in a map containing the date and value
   */
    protected Map<LocalDateTime, Double> loadData(int id, Predicate<User> predicate) {
        this.predicate = predicate;
        Map<LocalDateTime, Double> data;
        switch (id) {
            case 0 -> data = this.loadImpressionData();
            case 1 -> data = this.loadClicksData();
            case 2 -> data = this.loadBouncesData();
            case 3 -> data = this.loadConversionData();
            case 4 -> data = this.loadTotalCostData();
            case 5 -> data = this.loadCTRData();
            case 6 -> data = this.loadCPAData();
            case 7 -> data = this.loadCostPerClickData();
            case 8 -> data = this.loadCPTIData();
            case 9 -> data = this.loadBounceRateData();
            case 10 -> data = this.loadNumberOfUniquesData();
            default -> data = new HashMap<>();
        }
        return data;
    }

    public ArrayList<String> updateDashboardData(HashMap<String, Boolean> selected) {
        if (selected != null) currentlySelected = selected;


        Map<String, FilterPredicate> adjustedPredicates = this.updateSegmentFilters(predicates, currentlySelected);
        this.predicate = this.combinePredicates(adjustedPredicates);

        return getMetrics();
    }

    /**
     * Initialise the predicates used for filtering by audience segment.
     */
    public Map<String, FilterPredicate> initPredicates() {
        currentlySelected = new HashMap<>();
        Map<String, FilterPredicate> allPredicates = new HashMap<>(19);
        allPredicates.put("age_all", new FilterPredicate("age", u -> true));
        for (Age a : Age.values()) {
            Predicate<User> p = u -> u.getAge() == a;
            allPredicates.put("age_" + a.idx, new FilterPredicate("age", p));
        }

        allPredicates.put("context_all", new FilterPredicate( "context", u -> true));
        for (Context c : Context.values()) {
            Predicate<User> p = u -> u.getContext() == c;
            allPredicates.put("context_" + c.idx, new FilterPredicate("context", p));
        }

        allPredicates.put("income_all", new FilterPredicate( "income", u -> true));
        for (Income i : Income.values()) {
            Predicate<User> p = u -> u.getIncome() == i;
            allPredicates.put("income_" + i.idx, new FilterPredicate("income", p));
        }

        allPredicates.put("male_1", new FilterPredicate("gender", User::getGender));
        allPredicates.put("female_1", new FilterPredicate("gender", u -> !u.getGender()));
        return allPredicates;
    }

    public void resetFilters(Map<String, FilterPredicate> predicates) {
        currentlySelected = new HashMap<>();
        currentlySelected.put("age_all", true);
        currentlySelected.put("context_all", true);
        currentlySelected.put("income_all", true);
    }

    /**
     * Update the enabled predicates for filtering by audience segment
     *
     * @param selected A Map of predicates are either to be applied or not
     */
    public Map<String, FilterPredicate> updateSegmentFilters(Map<String, FilterPredicate> predicates, Map<String, Boolean> selected) {
        return this.updateSegmentFilters(predicates, selected, true);
    }

    public Map<String, FilterPredicate> updateSegmentFilters(Map<String, FilterPredicate> predicates, Map<String, Boolean> selected, Boolean resetBaseFilters) {
        HashMap<String, FilterPredicate> predicateMap = new HashMap<>();
        if (resetBaseFilters != null && resetBaseFilters) {
            predicateMap.put("age_all", predicates.get("age_all"));
            predicateMap.put("context_all", predicates.get("context_all"));
            predicateMap.put("income_all", predicates.get("income_all"));
            predicateMap.remove("male_1");
            predicateMap.remove("female_1");
        }
        selected.forEach((key, value) -> {
            FilterPredicate fp = predicates.get(key);
            if (value) {
                predicateMap.put(key, fp);
                if (fp.group().equals("age")) predicateMap.remove("age_all");
                if (fp.group().equals("context")) predicateMap.remove("context_all");
                if (fp.group().equals("income")) predicateMap.remove("income_all");
            }
        });
        return predicateMap;
    }

    /**
     * Combine the lists of predicates for filtering by audience segments
     * @return The combined list of predicates - "and"-ed together
     */
    public Predicate<User> combinePredicates(Map<String, FilterPredicate> predicates) {
        var ages = getPredicateGroup("age", predicates).map(FilterPredicate::predicate).reduce(u -> false, Predicate::or);
        var contexts = getPredicateGroup("context", predicates).map(FilterPredicate::predicate).reduce(u -> false, Predicate::or);
        var incomes = getPredicateGroup("income", predicates).map(FilterPredicate::predicate).reduce(u -> false, Predicate::or);
        Predicate<User> gender = u -> true;
        FilterPredicate male = predicates.get("male_1");
        FilterPredicate female = predicates.get("female_1");
        if (male != null) {
            gender = male.predicate();
        } else if (female != null) {
            gender = female.predicate();
        }
        return ages.and(contexts).and(incomes).and(gender);
    }

    public Stream<FilterPredicate> getPredicateGroup(String group, Map<String, FilterPredicate> predicates) {
        return predicates.values().stream().filter(fp -> fp.group().equals(group));
    }



    public void setClicksFile(File clicksFile) {
        this.clicksFile = clicksFile;
    }
    public void setImpressionsFile(File impressionsFile) {
        this.impressionsFile = impressionsFile;
    }
    public void setServerFile(File serverFile) {
        this.serverFile = serverFile;
    }

    public File getClicksFile() {
        return clicksFile;
    }
    public File getImpressionsFile() {
        return impressionsFile;
    }
    public File getServerFile() {
        return serverFile;
    }

    public String getBounceDef() {return bounceDef;}
    public int getBouncePageValue() { return bouncePageValue; }
    public int getBounceTimeValue() { return bounceTimeValue; }

    public void setPredicate(Predicate<User> predicate) {
        this.predicate = predicate;
    }

    public void setBounceDef(String bounceDef) { this.bounceDef = bounceDef; }
    public void setBouncePageValue(int bouncePageValue) { this.bouncePageValue = bouncePageValue; }
    public void setBounceTimeValue(int bounceTimeValue) { this.bounceTimeValue = bounceTimeValue; }

}
