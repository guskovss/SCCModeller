/*
 * Stress Corrosion Cracking Modeller
 */
package ru.gss.sccmodeller.data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JTextArea;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * List of model parameter set.
 * @version 1.1.0 17.03.2020
 * @author Sergey Guskov
 */
public class DataList {

    /**
     * List of parameter set.
     */
    private ArrayList<DataSet> dataSet;
    /**
     * Index of current parameter set.
     */
    private int dataSetIndex;
    /**
     * Show legend.
     */
    private boolean showLegend;

    /**
     * Constructor.
     */
    public DataList() {
        dataSet = new ArrayList<DataSet>();
        dataSetIndex = -1;
        showLegend = false;
    }

    /**
     * Save text area to file.
     * @param file file
     * @param jta text area
     * @throws java.io.IOException exception
     */
    public void saveTextAreaToFile(final File file, final JTextArea jta) throws IOException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new FileOutputStream(file), true);
            out.print(jta.getText());
        } finally {
            out.close();
        }
    }

    /**
     * Count of parameter set.
     * @return count of parameter set
     */
    public int getDataSetCount() {
        return dataSet.size();
    }

    /**
     * Maximum count of parameter set.
     * @return maximum count of parameter set
     */
    public int getDataSetCountMax() {
        return 9;
    }

    /**
     * Create dataset for CGR chart.
     * @return dataset
     */
    public XYSeriesCollection createDatasetCGR() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        int k = 1;
        for (int i = 0; i < dataSet.size(); i++) {
            XYSeries series = new XYSeries(k);
            for (int j = 0; j < dataSet.get(i).getDataCGR().size(); j++) {
                series.add(dataSet.get(i).getDataCGR().get(j).getX(), dataSet.get(i).getDataCGR().get(j).getY());
            }
            dataset.addSeries(series);
            k++;
        }
        return dataset;
    }

    /**
     * Create dataset for SIF chart.
     * @return dataset
     */
    public XYSeriesCollection createDatasetSIF() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        int k = 1;
        for (int i = 0; i < dataSet.size(); i++) {
            XYSeries series = new XYSeries(k);
            for (int j = 0; j < dataSet.get(i).getDataSIF().size(); j++) {
                series.add(dataSet.get(i).getDataSIF().get(j).getX(), dataSet.get(i).getDataSIF().get(j).getY());
            }
            dataset.addSeries(series);
            k++;
        }
        return dataset;
    }

    /**
     * Create dataset for CCD chart.
     * @return dataset
     */
    public XYSeriesCollection createDatasetCCD() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        int k = 1;
        for (int i = 0; i < dataSet.size(); i++) {
            XYSeries series = new XYSeries(k);
            for (int j = 0; j < dataSet.get(i).getDataCCD().size(); j++) {
                series.add(dataSet.get(i).getDataCCD().get(j).getX(), dataSet.get(i).getDataCCD().get(j).getY());
            }
            dataset.addSeries(series);
            k++;
        }
        return dataset;
    }

    /**
     * List of parameter set.
     * @return list of parameter set
     */
    public ArrayList<DataSet> getDataSet() {
        return dataSet;
    }

    /**
     * Current parameter set.
     * @return current parameter set
     */
    public DataSet getCurrentDataSet() {
        return dataSet.get(dataSetIndex);
    }

    /**
     * Index of current parameter set.
     * @return index of current parameter set
     */
    public int getDataSetIndex() {
        return dataSetIndex;
    }

    /**
     * Index of current parameter set.
     * @param aDataSetIndex index of current parameter set
     */
    public void setDataSetIndex(final int aDataSetIndex) {
        dataSetIndex = aDataSetIndex;
    }

     /**
     * Show legend.
     * @return show legend
     */
    public boolean isShowLegend() {
        return showLegend;
    }

    /**
     * Show legend.
     * @param aShowLegend show legend
     */
    public void setShowLegend(final boolean aShowLegend) {
        showLegend = aShowLegend;
    }
}
