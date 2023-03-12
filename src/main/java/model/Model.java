package model;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class Model {
    private List<Impression> impressions = null;
    private List<Click> clicks = null;
    private List<Server> serverInteractions = null;
    private final DecimalFormat df = new DecimalFormat("#.###");

    private File clicksFile;
    private File impressionsFile;
    private File serverFile;

    public Model() {}

    public void importData() {
        //Get CSV data from all 3 log files (can be changed to for loop)
        CsvReader cr = new CsvReader(clicksFile, impressionsFile, serverFile);
        try {
            this.impressions = cr.getImpressions();
            this.clicks = cr.getClicks();
            this.serverInteractions= cr.getServerInteractions();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public int totalImpressions()
    {
        return impressions.size();
    }

    public int totalClicks(){
        return clicks.size();
    }

    //Return number of uniques; (Distinct IDs from Click log)
    public int numberOfUniques()
    {
        return (int) clicks.stream().filter(distinctByKey(Click::getUserId)).count();
    }

    // predicate to filter the duplicates by the given key extractor
    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    //Return number of Conversions; (Conversions which are true)
    public int numberOfConversions()
    {
        return (int) serverInteractions.stream().filter(Server::getConversion).count();
    }

    //Bounce is defined by user in later sprints. For now keep it as number of page viewed = 1;
    public int numberOfBounces()
    {
        return (int) serverInteractions.stream().filter(Server -> !Server.getConversion() && Server.getPagesViewed() <= 1).count();
    }

    //Bounce Rate:	The	average	number of bounces per click
    //Convert ints to doubles before doing any calculations
    //return double in 3.dp
    public double bounceRate()
    {
        return Double.parseDouble(df.format((double) numberOfBounces() / (double) totalClicks()));
    }

    //TotalCost = Click Cost + Impression Cost
    public double totalCost()
    {
        return Double.parseDouble(df.format(impressions.stream().mapToDouble(Impression::getImpressionCost).sum() + clicks.stream().mapToDouble(Click::getClickCost).sum()));
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
        return Double.parseDouble(df.format(clicks.stream().mapToDouble(Click::getClickCost).sum() / (double) totalClicks()));
    }

    //Cost-per-acquisition	(CPA):	The	average	amount	of	money	spent	on	an	advertising	campaign for	each	acquisition	(i.e.,	conversion).
    //Convert ints to doubles before doing any calculations
    //return double in 3.dp
    public Double costPerAcquisition()
    {
        return Double.parseDouble(df.format(totalCost() / (double) serverInteractions.stream().filter(Server::getConversion).count()));
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

    public List<Impression> getImpressions() {
        return impressions;
    }


}
