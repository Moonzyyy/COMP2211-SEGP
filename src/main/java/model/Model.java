package model;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Model {
    private List<Impression> impressions = null;
    private List<Click> clicks = null;
    private List<Server> serverInteractions = null;

    public Model() {
        importData();
        System.out.println(serverInteractions.size());
        System.out.print(impressions.size());
    }

    public void importData() {
        //Get CSV data from all 3 log files (can be changed to for loop)
        CsvReader cr = new CsvReader();
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
        BigDecimal bRate = BigDecimal.valueOf((double) numberOfBounces() / (double) totalClicks()).setScale(3, RoundingMode.HALF_EVEN);
        return bRate.doubleValue();
    }

    //TotalCost = Click Cost + Impression Cost
    public double totalCost()
    {
        return impressions.stream().mapToDouble(Impression::getImpressionCost).sum() + clicks.stream().mapToDouble(Click::getClickCost).sum();
    }

    //Click-through-rate	(CTR):	The	average	number	of	clicks	per	impression
    //Convert ints to doubles before doing any calculations
    //return double in 3.dp
    public Double clickThroughRate()
    {
        return Math.round(((double) totalClicks() / (double) totalImpressions()) * 1000d) / 1000d;
    }

    //Cost-per-click	(CPC):	The	average	amount	of	money spent	on	an	advertising	campaign	for	each click
    //Convert ints to doubles before doing any calculations
    //return double in 3.dp
    public Double costPerClick()
    {
        return Math.round((clicks.stream().mapToDouble(Click::getClickCost).sum() / (double) totalClicks()) * 1000d) / 1000d;
    }

    //Cost-per-acquisition	(CPA):	The	average	amount	of	money	spent	on	an	advertising	campaign for	each	acquisition	(i.e.,	conversion).
    //Convert ints to doubles before doing any calculations
    //return double in 3.dp
    public Double costPerAcquisition()
    {
        return Math.round((totalCost() / (double) serverInteractions.stream().filter(Server::getConversion).count()) * 1000d) / 1000d;
    }
}
