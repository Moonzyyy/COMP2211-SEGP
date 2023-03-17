package model;

import core.Controller;
import java.io.File;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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

    private File clicksFile;
    private File impressionsFile;
    private File serverFile;

    public Model() {}

    public boolean importData() {
        //Get CSV data from all 3 log files
        CsvReader cr = new CsvReader(clicksFile, impressionsFile, serverFile);
        this.users = cr.getUsers();
        this.clickCost = getClicks().mapToDouble(Pair::getValue).sum();

        if (users == null) {
            Controller.sendErrorMessage("There has been an error with processing your clicks file!");
            return false;
        }
        return false;
    }

    /**
     * Gets every impression by folding every impression from every user.
     * @return stream of impressions
     */
    public Stream<Pair<LocalDateTime, Double>> getImpressions() {
        return users != null ? users.values().stream().parallel().flatMap(u -> u.getImpressions().stream()) : null;
    }

    /**
     * Gets every impression by folding every click from every user.
     * @return stream of click
     */
    public Stream<Pair<LocalDateTime, Double>> getClicks() {
        return users != null ? users.values().stream().parallel().flatMap(u -> u.getClicks().stream()) : null;
    }

    /**
     * Gets every impression by folding every server from every user.
     * @return stream of servers
     */
    public Stream<Server> getServers() {
        return users != null ? users.values().stream().parallel().flatMap(u -> u.getServers().stream()) : null;
    }

    /**
     * Counts every impression from the impression stream.
     * @return total number of impressions
     */
    public int totalImpressions()
    {
        return (int) this.getImpressions().count();
    }

    /**
     * Counts every click from the click stream.
     * @return total number of clicks
     */
    public int totalClicks(){
        return (int) this.getClicks().count();
    }

    /**
     * Counts every unique ID from all the users.
     * @return total number of unique users
     */
    public int numberOfUniques()
    {
        return (int) users.values().stream().parallel().filter(u -> u.getClicks().size() > 0).count();
    }

    /**
     * Counts every conversion from the server stream.
     * @return total number of conversions
     */
    public int numberOfConversions() {
        return (int) this.getServers().filter(Server::getConversion).count();
    }

    /**
     * Gets the number of bounces from the server stream.
     * Currently, bounce is defined as pages viewed <= 1,
     * but this will be user defined later.
     * @return total number of bounces
     */
    public int numberOfBounces() {
        this.bounces = (int) getServers().filter(server -> server.getPagesViewed() <= 1).count();
        return this.bounces;
    }

    /**
     * Calculates the bounce rate, which is the average number of bounces per click.
     * This is calculated by dividing number of bounces by total clicks.
     * @return the bounce rate in 3 d.p.
     */
    public double bounceRate() {
        return Double.parseDouble(df.format((double) this.bounces / metrics.get(1)));
    }

    /**
     * Calculates the total cost.
     * This is calculated by adding click and impression cost.
     * @return the total cost in 3 d.p.
     */
    public double totalCost() {
        return Double.parseDouble(df.format(this.clickCost + getImpressions().mapToDouble(Pair::getValue).sum()));
    }

    /**
     * Calculates the click-through-rate, which is the average number of clicks per impression.
     * This is calculated by dividing total number of clicks by total impressions.
     * @return the click-through-rate in 3 d.p.
     */
    public Double clickThroughRate() {
        return Double.parseDouble(df.format(metrics.get(1) / metrics.get(0)));
    }

    /**
     * Calculates the cost-per-click, which is the average amount
     * of money spent on the campaign for each click.
     * This is calculated by dividing click cost by total clicks.
     * @return the cost-per-click in 3 d.p.
     */
    public Double costPerClick() {
        return Double.parseDouble(df.format(this.clickCost / metrics.get(1)));
    }

    /**
     * Calculates the cost-per-acquisition, which is the average amount
     * of money spent for each acquisition (also known as conversion).
     * This is calculated by dividing the total cost by number of conversions.
     * @return the cost-per-acquisition in 3 d.p.
     */
    public Double costPerAcquisition() {
        return Double.parseDouble(df.format( metrics.get(4) / (double) metrics.get(3)));
    }

    /**
     * Calculates the cost-per-thousand-impressions, which is the
     * average amount of money spent for every thousand impressions.
     * This is calculated by dividing the total cost by number of impressions x1000.
     * @return the cost-per-thousand-impressions in 3 d.p.
     */
    public Double costPerThousandImps()
    {
        return Double.parseDouble(df.format(metrics.get(4) / (double) metrics.get(0)));
    }

    public void getTestCodeChecked() {
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");
        //var test = getImpressions().collect(Collectors.groupingBy( t -> t.getKey().format(formatter))).entrySet().stream().collect(Collectors.toMap(s -> LocalDateTime.parse(s.getKey(), formatter), s -> s.getValue().stream().mapToDouble(Pair::getValue).sum()));
        var test = getImpressions()
                .map(t -> {
                    var key = LocalDateTime.parse(t.getKey().toString(), DateTimeFormatter.ISO_LOCAL_DATE_TIME).format(formatter);
                    var value = t.getValue();
                    return new AbstractMap.SimpleEntry<>(key, value);
                })
                .collect(Collectors.toMap(
                        e -> LocalDateTime.parse(e.getKey(), formatter),
                        Map.Entry::getValue,
                        Double::sum));
    }

//    public Map<Date, Double> loadMetric(Stream dataset, )

    public Map<Date, Double> loadImpressionData() {
        Map<Date, Double> impressionCostsByDate = new HashMap<>();
        getImpressions().sequential().forEach(impression -> {
            LocalDateTime dateTime = impression.getKey();
            Double impressionCost = impression.getValue();
            Date dateWithoutTime = Date.from(dateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (impressionCostsByDate.containsKey(dateWithoutTime)) {
                impressionCostsByDate.put(dateWithoutTime, impressionCostsByDate.get(dateWithoutTime) + impressionCost);
            } else {
                impressionCostsByDate.put(dateWithoutTime, impressionCost);
            }
        });
        System.out.println(impressionCostsByDate);
        return impressionCostsByDate;
    }


    public Map<Date, Double> loadClicksData() {
        Map<Date, Double> clickCountsByDate = new HashMap<>();
        getClicks().sequential().forEach(click -> {
            LocalDateTime localDateTime = click.getKey();
            LocalDate date = localDateTime.toLocalDate();
            Date dateWithoutTime = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (clickCountsByDate.containsKey(dateWithoutTime)) {
                clickCountsByDate.put(dateWithoutTime, clickCountsByDate.get(dateWithoutTime) + 1.0);
            } else {
                clickCountsByDate.put(dateWithoutTime, 1.0);
            }
        });
        System.out.println(clickCountsByDate);
        return clickCountsByDate;
    }

    public Map<Date,Double> loadBouncesData() {
        Map<Date, Double> bouncesByDate = new HashMap<>();
        getServers().sequential().forEach(server -> {
            LocalDateTime localDateTime = server.getEntryDate();
            LocalDate date = localDateTime.toLocalDate();
            Date dateWithoutTime = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (!server.getConversion() && bouncesByDate.containsKey(dateWithoutTime)) {
                Double count = bouncesByDate.get(dateWithoutTime);
                bouncesByDate.put(dateWithoutTime, count+1);
            } else if (!server.getConversion()) {
                bouncesByDate.put(dateWithoutTime, 1.0);
            }
        });
        System.out.println(bouncesByDate);
        return bouncesByDate;
    }

    public Map<Date, Double> loadConversionData() {
        Map<Date,Double> conversionsByDate = new HashMap<>();
        getServers().sequential().forEach(server -> {
            LocalDateTime localDateTime = server.getEntryDate();
            LocalDate date = localDateTime.toLocalDate();
            Date dateWithoutTime = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (server.getConversion() && conversionsByDate.containsKey(dateWithoutTime)) {
                Double count = conversionsByDate.get(dateWithoutTime);
                conversionsByDate.put(dateWithoutTime, count+1);
            } else if (server.getConversion()) {
                conversionsByDate.put(dateWithoutTime, 1.0);
            }
        });
        System.out.println(conversionsByDate);
        return conversionsByDate;
    }


    public Map<Date, Double> loadClickCostData() {
        Map<Date, Double> clickCostByDate = new HashMap<>();
        getClicks().sequential().forEach(click -> {
            LocalDateTime localDateTime = click.getKey();
            LocalDate date = localDateTime.toLocalDate();
            Double clickCost = click.getValue();
            Date dateWithoutTime = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            if (clickCostByDate.containsKey(dateWithoutTime)) {
                clickCostByDate.put(dateWithoutTime, clickCostByDate.get(dateWithoutTime) + clickCost);
            } else {
                clickCostByDate.put(dateWithoutTime, clickCost);
            }
        });
        System.out.println(clickCostByDate);
        return clickCostByDate;
    }

    public Map<Date, Double> loadCTRData() {
        Map<Date, Double> ctrByDate = new HashMap<>();
        Map<Date, Double> impressionCostsByDate = loadImpressionData();
        Map<Date, Double> clickCountsByDate = loadClicksData();

        for (Date date : impressionCostsByDate.keySet()) {
            Double impressionCost = impressionCostsByDate.get(date);
            Double clickCount = clickCountsByDate.getOrDefault(date, 0.0);
            Double ctr = clickCount / impressionCost;
            ctrByDate.put(date, ctr);
        }

        System.out.println(ctrByDate);
        return ctrByDate;
    }

    /**
     * Create arraylist of metrics from functions to persist after import
     * @return arraylist of metrics
     */
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

}
