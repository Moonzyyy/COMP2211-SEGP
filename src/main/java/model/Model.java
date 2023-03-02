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
        System.out.println(impressions.size());
    }

    public void importData() {
        System.out.println("Loading, please wait...");
        //Get CSV data from all 3 log files (can be changed to for loop)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        CsvReader cr = new CsvReader();
        try {
            this.impressions = cr.getImpressions();
            this.clicks = cr.getClicks();
            this.serverInteractions= cr.getServerInteractions();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
