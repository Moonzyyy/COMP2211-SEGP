package model;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;

import java.time.LocalDateTime;
import java.util.Map;

public class HistogramModel {
    private final int id;
    private HistogramDataset dataSet;
    private JFreeChart chart;

    private final Model model;

    public HistogramModel(Model model, int id) {
        this.id = id;
        this.model = model;
        String title = "Distribution of Costs";
        String xAxisName = "Click Costs";
        String yAxisName = "Frequency";
        this.dataSet = new HistogramDataset();
        loadDataAndCreateHistogram(title, xAxisName, yAxisName);
    }

    private void loadDataAndCreateHistogram(String title, String xAxisName, String yAxisName) {
        Map<LocalDateTime, Double> rawData = model.loadTotalCostData();
        double[] values = extractCostData(rawData);
        int numberOfBins = (int) (getMaxCost(rawData) / 5);
        this.dataSet.addSeries("Values", values, numberOfBins);
        this.chart = ChartFactory.createHistogram(title, xAxisName, yAxisName, this.dataSet, PlotOrientation.VERTICAL, false, false, false);

        NumberAxis xAxis = new NumberAxis(xAxisName);
        xAxis.setTickUnit(new NumberTickUnit(5));
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainAxis(xAxis);
    }

    private double[] extractCostData(Map<LocalDateTime, Double> data) {
        double[] values = new double[data.size()];

        int i = 0;
        for (Double cost : data.values()) {
            values[i++] = cost;
        }
        return values;
    }

    private double getMaxCost(Map<LocalDateTime, Double> data) {
        double maxCost = Double.NEGATIVE_INFINITY;
        for (Double cost : data.values()) {
            maxCost = Math.max(maxCost, cost);
        }
        return maxCost;
    }

    public JFreeChart getChart() {
        return chart;
    }
}

