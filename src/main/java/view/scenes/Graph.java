package view.scenes;

import core.segments.Age;
import core.segments.Context;
import core.segments.Income;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.ValueAxisPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import view.components.CompareItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;


public class Graph extends AbstractScene {

    private final BorderPane layout;

    /**
     * The id of the metric that is being graphed.
     */
    private final Integer metricId;
    private final ArrayList<CheckBox> checkboxes = new ArrayList<CheckBox>(14);
    private final JFreeChart lineChart;
    private final JFreeChart histogram;
    private final DatePicker compareControlStartDatePicker;
    private final DatePicker compareControlEndDatePicker;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    Map<LocalDateTime, Double> data;
    private Button homeButton;

    private Button saveButton;
    private Button printButton;
    private Button segmentFilterButton;
    private ListView<Node> compareList;
    private ComboBox<String> timeFilter;
    private ComboBox<CompareItem> compareControl1;
    private ComboBox<CompareItem> compareControl2;
    private ComboBox<CompareItem> compareControl3;
    private Button compareControlDateFilterButton;
    private Button dateFilterButton;

    private CheckBox maleCheckBox;
    private CheckBox femaleCheckBox;

    private ChartPanel chartPanel;

    private boolean showLineGraph = true;

    private boolean graphTheme;

    private double currentZoomFactor = 1.0;


    /**
     * Class Constructor
     *
     * @param id        the metric ID
     * @param lineChart the lineChart
     * @param histogram
     */
    public Graph(Integer id, JFreeChart lineChart, JFreeChart histogram) {
        super();
        layout = new BorderPane();
        metricId = id;
        this.lineChart = lineChart;
        this.histogram = histogram;
        this.startDatePicker = new DatePicker();
        this.endDatePicker = new DatePicker();
        this.compareControlStartDatePicker = new DatePicker();
        this.compareControlEndDatePicker = new DatePicker();
        this.graphTheme = true;
    }

    /**
     * Creates all the components of the scene, and adds them to the layout
     */
    public void createScene() {
        Font graphFontLg = new Font(getFont(), Font.PLAIN, 20);
        Font graphFontSm = new Font(getFont(), Font.PLAIN, 12);

        var topBar = new HBox();
        topBar.setAlignment(Pos.BOTTOM_CENTER);

        topBar.getStyleClass().add("topBar");

        homeButton = new Button("Home");
        homeButton.getStyleClass().add("button");
        HBox.setHgrow(homeButton, Priority.ALWAYS);

        var graphTitle = new Label("AdViz - Graph");
        graphTitle.getStyleClass().add("graphTitle");

        saveButton = new Button("Save");
        saveButton.getStyleClass().add("button");

        printButton = new Button("Print");
        printButton.getStyleClass().add("button");

        segmentFilterButton = new Button("Filter");
        segmentFilterButton.setDisable(true);
        segmentFilterButton.getStyleClass().add("button");


        topBar.getChildren().add(homeButton);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topBar.getChildren().add(spacer);
        topBar.getChildren().add(graphTitle);
        Region spacer2 = new Region();
        HBox.setHgrow(spacer2, Priority.ALWAYS);
        topBar.getChildren().add(spacer2);
        topBar.getChildren().add(saveButton);
        topBar.getChildren().add(printButton);
        topBar.getChildren().add(segmentFilterButton);

        layout.setTop(topBar);

        BorderPane.setMargin(topBar, new Insets(10, 10, 10, 10));

        var filterBar = new HBox();
        filterBar.setAlignment(Pos.CENTER);
        filterBar.setPadding(new Insets(10, 10, 10, 10));
        filterBar.setSpacing(10);

        if (metricId == 4) {
            ToggleButton toggleGraph = new ToggleButton("Histogram");
            toggleGraph.setOnAction(event -> {
                if (toggleGraph.isSelected()) {
                    toggleGraph.setText("Line Graph");
                    showLineGraph = false;
                    chartPanel.setChart(histogram);
                    updateFilterVisibility(false);
                    toggleCheckBoxes(false);
                } else {
                    toggleGraph.setText("Histogram");
                    showLineGraph = true;
                    chartPanel.setChart(lineChart);
                    updateFilterVisibility(true);
                    toggleCheckBoxes(true);
                }
            });
            toggleGraph.getStyleClass().add("button");
            filterBar.getChildren().add(toggleGraph);
        }

        if (showLineGraph) {
            lineChart.getTitle().setPadding(0, 120, 0, 0);
            lineChart.getLegend().setPadding(0, 120, 0, 0);
            chartPanel = new ChartPanel(lineChart);
        } else {
            histogram.getTitle().setPadding(0, 120, 0, 0);
            histogram.getLegend().setPadding(0, 120, 0, 0);
            chartPanel = new ChartPanel(histogram);
        }

        SwingNode swingNode = new SwingNode();

        JFreeChart chart = chartPanel.getChart();
        XYPlot xyPlot = chart.getXYPlot();
        ValueAxis domainAxis = xyPlot.getDomainAxis();
        ValueAxis rangeAxis = xyPlot.getRangeAxis();
        XYItemRenderer renderer = xyPlot.getRenderer();
        XYPlot histogramPlot = histogram.getXYPlot();
        XYBarRenderer barRenderer = (XYBarRenderer) histogramPlot.getRenderer();

        SwingUtilities.invokeLater(() -> {
            lineChart.getTitle().setFont(graphFontLg);
            histogram.getTitle().setFont(graphFontLg);

            histogramPlot.getDomainAxis()
                    .setLabelFont(graphFontSm);
            histogramPlot.getDomainAxis()
                    .setTickLabelFont(graphFontSm);
            histogramPlot.getRangeAxis()
                    .setLabelFont(graphFontSm);
            histogramPlot.getRangeAxis()
                    .setTickLabelFont(graphFontSm);
            histogramPlot.getRenderer()
                    .setDefaultItemLabelFont(graphFontSm);
            histogramPlot.getRenderer().setSeriesStroke(0, new BasicStroke(4.0f));
            histogramPlot.getRenderer().setDefaultSeriesVisible(false);
            histogramPlot.getRenderer().setSeriesVisible(0, true);
            histogramPlot.setDatasetRenderingOrder(DatasetRenderingOrder.REVERSE);

            rangeAxis.setLabelFont(graphFontSm);
            rangeAxis.setTickLabelFont(graphFontSm);

            domainAxis.setLabelFont(graphFontSm);
            domainAxis.setTickLabelFont(graphFontSm);

            renderer.setDefaultItemLabelFont(graphFontSm);
            renderer.setSeriesStroke(0, new BasicStroke(4.0f));
            renderer.setSeriesStroke(1, new BasicStroke(4.0f));
            renderer.setSeriesStroke(2, new BasicStroke(4.0f));
            renderer.setSeriesStroke(3, new BasicStroke(4.0f));
            renderer.setDefaultSeriesVisible(false);
            renderer.setSeriesVisible(0, true);

            chartPanel.setMouseWheelEnabled(true);
            chartPanel.setDomainZoomable(false);
            chartPanel.setRangeZoomable(false);
            chartPanel.addMouseWheelListener(this::handleZoom);

            chart.getLegend().setItemFont(graphFontSm);

            barRenderer.setDrawBarOutline(true);
        });

        if (getGraphTheme()) {
            // DARK MODE
            SwingUtilities.invokeLater(() -> {
                chart.setBackgroundPaint(new Color(18, 18, 18));
                lineChart.getTitle().setPaint(Color.WHITE);
                histogram.getTitle().setPaint(Color.WHITE);
                chart.getPlot().setBackgroundPaint(new Color(47, 47, 47));
                chart.getPlot().setOutlinePaint(new Color(47, 47, 47));

                domainAxis.setAxisLinePaint(Color.WHITE);
                domainAxis.setTickLabelPaint(Color.WHITE);
                domainAxis.setLabelPaint(Color.WHITE);

                rangeAxis.setAxisLinePaint(Color.WHITE);
                rangeAxis.setTickLabelPaint(Color.WHITE);
                rangeAxis.setLabelPaint(Color.WHITE);

                renderer.setSeriesPaint(0, Color.WHITE);
                renderer.setSeriesPaint(1, Color.decode("#1C7C54"));
                renderer.setSeriesPaint(2, Color.RED);
                renderer.setSeriesPaint(3, Color.YELLOW);

                histogram.setBackgroundPaint(new Color(18, 18, 18));
                histogram.getPlot().setBackgroundPaint(new Color(47, 47, 47));
                histogram.getPlot().setOutlinePaint(new Color(47, 47, 47));
                histogramPlot.getRenderer().setSeriesPaint(0, Color.WHITE);
                histogramPlot.getDomainAxis().setAxisLinePaint(Color.WHITE);
                histogramPlot.getDomainAxis().setTickLabelPaint(Color.WHITE);
                histogramPlot.getDomainAxis().setLabelPaint(Color.WHITE);

                histogramPlot.getRangeAxis().setAxisLinePaint(Color.WHITE);
                histogramPlot.getRangeAxis().setTickLabelPaint(Color.WHITE);
                histogramPlot.getRangeAxis().setLabelPaint(Color.WHITE);

                barRenderer.setSeriesOutlinePaint(0, Color.WHITE);
                barRenderer.setSeriesOutlinePaint(1, Color.decode("#1C7C54"));
                barRenderer.setDrawBarOutline(true);
                Color translucentWhite = new Color(255, 255, 255, 77);
                barRenderer.setSeriesPaint(0, translucentWhite);
                swingNode.setContent(chartPanel);

                chart.getLegend().setItemPaint(Color.WHITE);
                chart.getLegend().setBackgroundPaint(Color.decode("#121212"));
            });
        } else {
            //LIGHT MODE
            SwingUtilities.invokeLater(() -> {
                chart.setBackgroundPaint(new Color(248, 253, 255));
                lineChart.getTitle().setPaint(Color.BLACK);
                histogram.getTitle().setPaint(Color.BLACK);

                chart.getPlot().setBackgroundPaint(new Color(230, 238, 245));
                chart.getPlot().setOutlinePaint(new Color(230, 238, 245));

                domainAxis.setAxisLinePaint(Color.BLACK);
                domainAxis.setTickLabelPaint(Color.BLACK);
                domainAxis.setLabelPaint(Color.BLACK);

                rangeAxis.setAxisLinePaint(Color.BLACK);
                rangeAxis.setTickLabelPaint(Color.BLACK);
                rangeAxis.setLabelPaint(Color.BLACK);


                renderer.setSeriesPaint(0, new Color(0, 109, 62));

                histogram.setBackgroundPaint(new Color(248, 253, 255));
                histogram.getPlot().setBackgroundPaint(new Color(230, 238, 245));
                histogram.getPlot().setOutlinePaint(new Color(230, 238, 245));
                histogramPlot.getRenderer().setSeriesPaint(0, Color.BLACK);
                histogramPlot.getDomainAxis().setAxisLinePaint(Color.BLACK);
                histogramPlot.getDomainAxis().setTickLabelPaint(Color.BLACK);
                histogramPlot.getDomainAxis().setLabelPaint(Color.BLACK);
                histogramPlot.getRangeAxis().setAxisLinePaint(Color.BLACK);
                histogramPlot.getRangeAxis().setTickLabelPaint(Color.BLACK);
                histogramPlot.getRangeAxis().setLabelPaint(Color.BLACK);

                barRenderer.setSeriesOutlinePaint(0, new Color(0, 125, 82));
                Color translucentGreen = new Color(0, 125, 82, 77);
                barRenderer.setSeriesPaint(0, translucentGreen);
                swingNode.setContent(chartPanel);

                chart.getLegend().setItemPaint(Color.BLACK);
                chart.getLegend().setBackgroundPaint(Color.decode("#f8fdff"));

                compareControl1.setStyle("-fx-background-color: #1C7C54; -fx-background-insets: 0;");
            });

        }
        lineChart.getXYPlot().setDomainPannable(true);
        lineChart.getXYPlot().setRangePannable(true);

        var graphContainer = new HBox();
        graphContainer.setAlignment(Pos.CENTER);
        graphContainer.setPadding(new Insets(10, 10, 10, 10));
        graphContainer.getChildren().add(swingNode);
        layout.setCenter(graphContainer);

        startDatePicker.getStyleClass().add("start-date-picker");
        endDatePicker.getStyleClass().add("end-date-picker");

        startDatePicker.setMaxWidth(110);
        endDatePicker.setMaxWidth(110);


        timeFilter = new ComboBox<>();
        timeFilter.getItems().addAll("Hour", "Day", "Week", "Month");
        timeFilter.setValue("Day");
        timeFilter.getStyleClass().add("time-filter");

        dateFilterButton = new Button("Apply");
        dateFilterButton.setDisable(true);

        compareControl1 = new ComboBox<>();
        compareControl1.setStyle("-fx-background-color: #FFFFFF; -fx-background-insets: 0;");
        compareControl2 = new ComboBox<>();
        compareControl2.setVisible(true);
        compareControl2.setStyle("-fx-background-color: #1C7C54; -fx-background-insets: 0;");
        compareControl3 = new ComboBox<>();
        compareControl3.setVisible(false);
        compareControl3.setStyle("-fx-background-color: #CC2936");

        compareControlFactory(compareControl1);
        compareControlFactory(compareControl2);
        compareControlFactory(compareControl3);

        var compareSpacer = new Region();
        compareSpacer.setPadding(new Insets(0, 0, 0, 20));
        var compareSpacer2 = new Region();
        HBox.setHgrow(compareSpacer2, Priority.ALWAYS);

        compareControlDateFilterButton = new Button("Apply");
        compareControlDateFilterButton.setDisable(true);


        compareControlStartDatePicker.getStyleClass().add("start-date-picker");
        compareControlEndDatePicker.getStyleClass().add("end-date-picker");

        compareControlStartDatePicker.setMaxWidth(110);
        compareControlEndDatePicker.setMaxWidth(110);

        filterBar.getChildren()
                .addAll(compareControl1, compareSpacer, compareControl2, compareSpacer2, compareControl3, compareControlStartDatePicker, compareControlEndDatePicker, compareControlDateFilterButton, timeFilter, startDatePicker, endDatePicker, dateFilterButton);

        createCheckBoxes();

        layout.setBottom(filterBar);
        scene = new Scene(layout, 1280, 720);
//        scene.getStylesheets()
//                .add(Objects.requireNonNull(getClass().getResource("/view/graph.css")).toExternalForm());

    }

    private void updateFilterVisibility(boolean lineGraphVisible) {
        timeFilter.setVisible(lineGraphVisible);
        startDatePicker.setVisible(lineGraphVisible);
        endDatePicker.setVisible(lineGraphVisible);
        dateFilterButton.setVisible(lineGraphVisible);
        compareControl1.setVisible(lineGraphVisible);
        compareControl2.setVisible(lineGraphVisible);
        compareControlStartDatePicker.setVisible(lineGraphVisible);
        compareControlEndDatePicker.setVisible(lineGraphVisible);
        compareControlDateFilterButton.setVisible(lineGraphVisible);
    }

    private void toggleCheckBoxes(boolean visible) {
        if (visible) {
            layout.setRight(compareList);
        } else {
            layout.setRight(null);
        }
    }

    private void handleZoom(MouseWheelEvent e) {
        double ZOOM_INCREMENT = 0.1;
        double zoomFactor = 1.0 + (ZOOM_INCREMENT * e.getWheelRotation());
        double newZoomFactor = currentZoomFactor * zoomFactor;

        double MAX_ZOOM_FACTOR = 5.0;
        double MIN_ZOOM_FACTOR = 0.1;
        if (newZoomFactor < MIN_ZOOM_FACTOR) {
            zoomFactor = MIN_ZOOM_FACTOR / currentZoomFactor;
        } else if (newZoomFactor > MAX_ZOOM_FACTOR) {
            zoomFactor = MAX_ZOOM_FACTOR / currentZoomFactor;
        }

        XYPlot plot = chartPanel.getChart().getXYPlot();
        ValueAxis domainAxis = plot.getDomainAxis();
        ValueAxis rangeAxis = plot.getRangeAxis();

        double domainAxisLength = domainAxis.getRange().getLength();
        double rangeAxisLength = rangeAxis.getRange().getLength();

        domainAxis.setRange(domainAxis.getLowerBound() - (domainAxisLength * (zoomFactor - 1) / 2),
                domainAxis.getUpperBound() + (domainAxisLength * (zoomFactor - 1) / 2));
        rangeAxis.setRange(rangeAxis.getLowerBound() - (rangeAxisLength * (zoomFactor - 1) / 2),
                rangeAxis.getUpperBound() + (rangeAxisLength * (zoomFactor - 1) / 2));

        currentZoomFactor *= zoomFactor;
    }

    /**
     * @param compareControl
     */
    private void compareControlFactory(ComboBox<CompareItem> compareControl) {
        CompareItem defaultItem = new CompareItem("Default", null);
        compareControl.getItems().addAll(defaultItem, new CompareItem("Male", "male_1"), new CompareItem("Female", "female_1"));
        for (Age age : Age.values()) {
            compareControl.getItems().add(new CompareItem(age.label, "age_" + age.idx));
        }
        for (Income income : Income.values()) {
            compareControl.getItems().add(new CompareItem(income.label, "income_" + income.idx));
        }
        for (Context context : Context.values()) {
            compareControl.getItems().add(new CompareItem(context.label, "context_" + context.idx));
        }
        compareControl.getStyleClass().add("time-filter");
        compareControl.setValue(defaultItem);
    }

    /**
     * Creates the checkboxes for the filter bar
     */
    void createCheckBoxes() {

        compareList = new ListView<>();

        var genderText = new Label("Gender of Audience:");
        genderText.getStyleClass().add("list-cell-text");
        maleCheckBox = new CheckBox("Male");
        maleCheckBox.getStyleClass().add("checkbox");
        maleCheckBox.setId("male_1");
        femaleCheckBox = new CheckBox("Female");
        femaleCheckBox.getStyleClass().add("checkbox");
        femaleCheckBox.setId("female_1");
        this.checkboxes.add(maleCheckBox);
        this.checkboxes.add(femaleCheckBox);

        compareList.getStyleClass().add("list-cell");
        compareList.getItems().addAll(genderText, maleCheckBox, femaleCheckBox);

        var ageText = new Label("Age of Audience:");
        ageText.getStyleClass().add("list-cell-text");
        compareList.getItems().add(ageText);
        for (Age a : Age.values()) {
            CheckBox box = new CheckBox(a.label);
            box.setId("age_" + a.idx);
            compareList.getItems().add(box);
            this.checkboxes.add(box);
        }

        var incomeText = new Label("Income of Audience:");
        incomeText.getStyleClass().add("list-cell-text");
        compareList.getItems().add(incomeText);
        for (Income i : Income.values()) {
            CheckBox box = new CheckBox(i.label);
            box.setId("income_" + i.idx);
            compareList.getItems().add(box);
            this.checkboxes.add(box);
        }

        var contextText = new Label("Location of Ad Interaction:");
        contextText.getStyleClass().add("list-cell-text");
        compareList.getItems().add(contextText);
        for (Context c : Context.values()) {
            CheckBox box = new CheckBox(c.label);
            box.setId("context_" + c.idx);
            compareList.getItems().add(box);
            this.checkboxes.add(box);
        }
        this.checkboxes.forEach(c -> c.getStyleClass().add("checkbox"));
        layout.setRight(compareList);

    }

    public ChartPanel getLineChart() {
        return chartPanel;
    }

    /**
     * @return get home button
     */
    public Button getHomeButton() {
        return homeButton;
    }

    public  Button getSaveButton() {
        return saveButton;
    }

    /**
     * @return get print button
     */
    public Button getPrintButton() {
        return printButton;
    }

    /**
     * @return get compare button
     */
    public Button getSegmentFilterButton() {
        return segmentFilterButton;
    }

    /**
     * @return get the list of check boxes
     */
    public ArrayList<CheckBox> getCheckboxes() {
        return checkboxes;
    }

    /**
     * @return get the date filter button
     */
    public Button getDateFilterButton() {
        return dateFilterButton;
    }

    public Button getCompareControlDateFilterButton() {
        return compareControlDateFilterButton;
    }

    /**
     * @return get the box of time filters
     */
    public ComboBox<String> getTimeFilter() {
        return timeFilter;
    }

    public DatePicker getCompareControlStartDatePicker() {
        return compareControlStartDatePicker;
    }

    public DatePicker getCompareControlEndDatePicker() {
        return compareControlEndDatePicker;
    }

    /**
     * @return get the start date picker
     */
    public DatePicker getStartDatePicker() {
        return startDatePicker;
    }

    /**
     * @return get the end date picker
     */
    public DatePicker getEndDatePicker() {
        return endDatePicker;
    }

    /**
     * @return
     */
    public ComboBox<CompareItem> getCompareControl1() {
        return compareControl1;
    }

    public ComboBox<CompareItem> getCompareControl2() {
        return compareControl2;
    }

    public ComboBox<CompareItem> getCompareControl3() {
        return compareControl3;
    }

    /**
     * @return get the male check box
     */
    public CheckBox getMaleCheckBox() {
        return maleCheckBox;
    }

    /**
     * @return get the female check box
     */
    public CheckBox getFemaleCheckBox() {
        return femaleCheckBox;
    }

    public void setLineVisibility(int i, boolean isVisible) {
        chartPanel.getChart().getXYPlot().getRenderer().setSeriesVisible(i, isVisible);
    }

    public boolean getGraphTheme() {
        return graphTheme;
    }

    public void setGraphTheme(boolean theme) {
        this.graphTheme = theme;
    }

    public void setStyles(boolean theme) {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/view/graph.css").toExternalForm());
        setGraphTheme(theme);
    }
}
