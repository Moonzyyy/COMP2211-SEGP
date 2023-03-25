package model;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.statistics.HistogramDataset;

public class HistogramModel {
    private final int id;
    private HistogramDataset dataSet;
    private JFreeChart chart;

    public HistogramModel(String title, String xAxisName, String yAxisName, int id) {
        this.id = id;
        double[] values = { 1.2, 2.3, 3.4, 4.5, 5.6, 6.7, 7.8, 8.9, 9.0 };
        this.dataSet = new HistogramDataset();
        this.dataSet.addSeries("Values", values, 10);
        this.chart = ChartFactory.createHistogram(title, xAxisName, yAxisName, this.dataSet);
    }

    public JFreeChart getChart() {
        return chart;
    }
}
