/*
 * Stress Corrosion Cracking Modeller
 */
package ru.gss.sccmodeller.chart;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItemSource;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import ru.gss.sccmodeller.data.DataList;

/**
 * Chart.
 * @version 1.1.0 16.03.2020
 * @author Sergey Guskov
 */
public class ChartMaker {

    /**
     * Parent frame.
     */
    private Component parent;
    /**
     * Data.
     */
    private DataList data;
    /**
     * CGR chart dialog.
     */
    private DlgChart dlgChartCGR;
    /**
     * SIF chart dialog.
     */
    private DlgChart dlgChartSIF;
    /**
     * CCD chart dialog.
     */
    private DlgChart dlgChartCCD;
    
    /**
     * Constructor.
     * @param aParent parent frame
     * @param aData data
     */
    public ChartMaker(final Component aParent, final DataList aData) {
        parent = aParent;
        data = aData;
    }

    /**
     * Create chart.
     * @param dataset data
     * @param labelX name of axis x
     * @param labelY name of axis y
     * @param isStepPlot step chart
     * @return chart
     */
    public static JFreeChart createChart(final XYDataset dataset, final String labelX, final String labelY, final boolean isStepPlot) {
        XYPlot plot = createPlot(dataset, labelX, labelY, isStepPlot);
        JFreeChart chart = new JFreeChart("", plot);
        //Settings
        chart.setBackgroundPaint(Color.white);
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        chart.getLegend().setBorder(0, 0, 0, 0);
        return chart;  
    }

    /**
     * Create plot.
     * @param dataset data
     * @param labelX name of axis x
     * @param labelY name of axis y
     * @param isStepPlot step chart
     * @return plot
     */
    private static XYPlot createPlot(final XYDataset dataset, 
            final String labelX, final String labelY, final boolean isStepPlot) {
        NumberAxis xAxis = new NumberAxis(labelX);
        xAxis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        xAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 12));
        xAxis.setNumberFormatOverride(new DecimalFormat("0.00"));
        xAxis.setAutoRangeIncludesZero(false);
        xAxis.setLowerMargin(0);
        xAxis.setUpperMargin(0);
        NumberAxis yAxis = new NumberAxis(labelY);
        yAxis.setTickLabelFont(new Font("Tahoma", Font.PLAIN, 12));
        yAxis.setLabelFont(new Font("Tahoma", Font.BOLD, 12));
        yAxis.setNumberFormatOverride(new DecimalFormat("0.00"));
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setLowerMargin(0);
        yAxis.setUpperMargin(0);      
        //Parameters of series
        XYItemRenderer renderer;
        if (isStepPlot) {
            renderer = new XYStepRenderer();
        } else {
            renderer = new XYLineAndShapeRenderer();
            for (int i = 0; i < 10; i++) {
                ((XYLineAndShapeRenderer) renderer).setSeriesShapesVisible(i, false);
            }
        }
        //Colors
        renderer.setSeriesPaint(0, Color.BLUE);
        renderer.setSeriesPaint(1, Color.RED);
        renderer.setSeriesPaint(2, new Color(0, 170, 0));
        renderer.setSeriesPaint(3, Color.BLACK);
        renderer.setSeriesPaint(4, Color.DARK_GRAY);
        renderer.setSeriesPaint(5, Color.GRAY);
        renderer.setSeriesPaint(6, Color.LIGHT_GRAY);
        renderer.setSeriesPaint(7, Color.MAGENTA);
        renderer.setSeriesPaint(8, Color.ORANGE);   
        //Tooltips
        for (int i = 0; i < 10; i++) {
            renderer.setSeriesToolTipGenerator(i, new StandardXYToolTipGenerator("{1}; {2}", NumberFormat.getNumberInstance(), NumberFormat.getNumberInstance()));
        }
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, renderer);
        plot.setBackgroundPaint(Color.white);
        plot.setDomainGridlinePaint(Color.darkGray);
        plot.setRangeGridlinePaint(Color.darkGray);
        plot.getRangeAxis().setAutoRangeMinimumSize(0.2);          
        plot.setAxisOffset(new RectangleInsets(4, 4, 4, 4));
        return plot;
    }

     /**
     * Create chart.
     * @param data data
     * @param index index of chart
     * @return chart
     */
    public static JFreeChart createChart(final DataList data, final int index) {
        JFreeChart chart = null;
        XYPlot xyplot = choisePlot(data, index);
        chart = new JFreeChart("", xyplot);
        chart.setBackgroundPaint(Color.white);
        chart.getLegend().setPosition(RectangleEdge.RIGHT);
        chart.getLegend().setBorder(0, 0, 0, 0);
        JFreeChart chartTemp = createChart(data.createDatasetCCD(), null, null, true);
        LegendItemSource[] lis = chartTemp.getLegend().getSources();
        chart.getLegend().setSources(lis);
        if (data.isShowLegend()) {
            chart.getLegend().setVisible(true);
            chart.setPadding(new RectangleInsets(10, 0, 0, 5));
        } else {
            chart.getLegend().setVisible(false);
            chart.setPadding(new RectangleInsets(10, 0, 0, 38));
        }
        return chart;
    }

    /**
     * Choise plot.
     * @param data data
     * @param index index of chart
     * @return plot
     */
    public static XYPlot choisePlot(final DataList data, final int index) {
        XYPlot plot = null;
        String s = "K, МПам1/2";
        AttributedString ss = new AttributedString(s);
        ss.addAttributes(new Font("Tahoma", Font.BOLD, 13).getAttributes(), 0, s.length());
        ss.addAttribute(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUPER, 7, 10);
        switch (index) {
            case 0:
                plot = createPlot(data.createDatasetCGR(), "K, МПа√м", "da/dt, мм/год", false);
                ((NumberAxis) plot.getDomainAxis()).setAttributedLabel(ss);
                break;
            case 1:
                plot = createPlot(data.createDatasetSIF(), "a, мм", "К, МПа√м", false);
                ((NumberAxis) plot.getRangeAxis()).setAttributedLabel(ss);
                break;
            default:
                plot = createPlot(data.createDatasetCCD(), "t, год", "a, мм", false);
                break;
        }
        return plot;
    }

    /**
     * Create or refresh chart dialog.
     * @param currentDialog current chart dialog
     * @param dataset data
     * @param dTitle title of chart dialog
     * @param xLabel name of axis x
     * @param yLabel name of axis y
     * @param isStepPlot step chart
     * @param isShowLegend show legend
     * @return new chart dialog
     */
    private DlgChart createOrRefresh(final DlgChart currentDialog,
            final XYSeriesCollection dataset, final String dTitle,
            final String xLabel, final String yLabel, final boolean isStepPlot,
            final boolean isShowLegend) {
        DlgChart newDialog;
        if (currentDialog == null) {
            newDialog = new DlgChart(dataset, dTitle, xLabel, yLabel, isStepPlot);
            newDialog.setLocationRelativeTo(parent);
        } else {
            newDialog = currentDialog;
            newDialog.refresh(dataset, isStepPlot);
        }     
        if (isShowLegend) {
            newDialog.getChart().getLegend().setVisible(true);
            newDialog.getChart().setPadding(new RectangleInsets(0, 0, 0, 5));
        } else {
            newDialog.getChart().getLegend().setVisible(false);
            newDialog.getChart().setPadding(new RectangleInsets(0, 0, 0, 38));
        }
        return newDialog;
    }

    /**
     * Create or refresh CGR chart dialog.
     */
    public void showChartCGR() {
        dlgChartCGR = createOrRefresh(dlgChartCGR, data.createDatasetCGR(),
                "Зависимость скорости роста трещины от коэффициента интенсивности напряжений", "K, МПа√м", "da/dt, мм/год", false, data.isShowLegend());
        dlgChartCGR.showChart();
    }

    /**
     * Create or refresh SIF chart dialog.
     */
    public void showChartSIF() {
        dlgChartSIF = createOrRefresh(dlgChartSIF, data.createDatasetSIF(),
                "Зависимость коэффициента интенсивности напряжений от глубины трещины", "a, мм", "К, МПа√м", false, data.isShowLegend());
        dlgChartSIF.showChart();
    }

    /**
     * Create or refresh CCD chart dialog.
     */
    public void showChartCCD() {
        dlgChartCCD = createOrRefresh(dlgChartCCD, data.createDatasetCCD(),
                "Зависимость глубины трещины от времени", "t, год", "a, мм", false, data.isShowLegend());
        dlgChartCCD.showChart();
    }
}
