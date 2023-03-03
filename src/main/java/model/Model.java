package model;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
        return 0;
    }

    //Return number of Conversions; (Conversions which are true)
    public int numberOfConversions()
    {
        return 0;
    }

    //Bounce is defined by user in later sprints. For now keep it as number of page viewed = 1;
    public int numberOfBounces()
    {
        return 0;
    }

    //Bounce Rate:	The	average	number	of	bounces	per	click
    //Convert ints to doubles before doing any calculations
    //return double in 3.dp
    public double bounceRate()
    {
        return 0.0;
    }

    //TotalCost = Click Cost + Impression Cost
    public double totalCost()
    {
        return 0.0;
    }

    //Click-through-rate	(CTR):	The	average	number	of	clicks	per	impression
    //Convert ints to doubles before doing any calculations
    //return double in 3.dp
    public Double clickThroughRate()
    {
        return 0.0;
    }

    //Cost-per-click	(CPC):	The	average	amount	of	money spent	on	an	advertising	campaign	for	each click
    //Convert ints to doubles before doing any calculations
    //return double in 3.dp
    public Double costPerClick()
    {
        return 0.0;
    }

    //Cost-per-acquisition	(CPA):	The	average	amount	of	money	spent	on	an	advertising	campaign for	each	acquisition	(i.e.,	conversion).
    //Convert ints to doubles before doing any calculations
    //return double in 3.dp
    public Double costPerAcquisition()
    {
        return 0.0;
    }



}
