/*
 * Stress Corrosion Cracking Modeller
 */
package ru.gss.sccmodeller.chart;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.jdesktop.application.Action;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import ru.gss.sccmodeller.commons.DlgParent;
import ru.gss.sccmodeller.commons.FileChooserFactory;

/**
 * Chart dialog.
 * @version 1.1.0 12.03.2020
 * @author Sergey Guskov
 */
public class DlgChart extends DlgParent {

    /**
     * Chart.
     */
    private JFreeChart chart;
    /**
     * Chart panel.
     */
    private ChartPanel chartPanel;
    /**
     * Title of dialog.
     */
    private String chartTitle = "Диаграмма";
    /**
     * Label of axis x.
     */
    private String chartLabelX = "x";
    /**
     * Label of axis y.
     */
    private String chartLabelY = "y";
    /**
     * Width of chart.
     */
    private int saveWidth = 1200;
    /**
     * Heigth of chart.
     */
    private int saveHeigth = 320;

    /**
     * Constructor.
     * @param dataset dataset for chart
     */
    public DlgChart(final XYDataset dataset) {
        super();
        initComponents();
        setTitle(chartTitle);
        chart = ChartMaker.createChart(dataset, chartLabelX, chartLabelY, false);
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);
        jpChart.add(chartPanel);
        chartPanel.setPopupMenu(jpmChart);
    }

    /**
     * Constructor.
     * @param dataset dataset for chart
     * @param dTitle title of dialog
     * @param xLabel label of axis x
     * @param yLabel label of axis у
     * @param isStepPlot step chart
     */
    public DlgChart(final XYDataset dataset, final String dTitle, final String xLabel, final String yLabel, final boolean isStepPlot) {
        super();
        initComponents();
        chartTitle = dTitle;
        chartLabelX = xLabel;
        chartLabelY = yLabel;
        setTitle(chartTitle);
        chart = ChartMaker.createChart(dataset, chartLabelX, chartLabelY, isStepPlot);
        chartPanel = new ChartPanel(chart);
        chartPanel.setMouseWheelEnabled(true);
        jpChart.add(chartPanel);
        chartPanel.setPopupMenu(jpmChart);
    }

    /**
     * Save chart.
     */
    @Action
    public void acSaveAs() {
        JFileChooser chooser = FileChooserFactory.getChooser(5);
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                //ChartUtilities.saveChartAsPNG(f, chart, chartPanel.getWidth(), chartPanel.getHeight());
                ChartUtilities.saveChartAsPNG(f, chart, saveWidth, saveHeigth);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, ex, "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Parameters of chart.
     */
    @Action
    public void acParameter() {
        DlgParameterChartEdit d = new DlgParameterChartEdit();
        d.setTempObj(chart);
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }

    /**
     * Refresh chart.
     * @param dataset dataset for chart
     * @param isStepPlot step chart
     */
    protected void refresh(final XYDataset dataset, final boolean isStepPlot) {
        chart = ChartMaker.createChart(dataset, chartLabelX, chartLabelY, isStepPlot);
        chartPanel.setChart(chart);
    }

    /**
     * Chart.
     * @return chart
     */
    public JFreeChart getChart() {
        return chart;
    }

    /**
     * Move dialog to front.
     */
    public void showChart() {
        setVisible(true);
        toFront();
    }

    //CHECKSTYLE:OFF
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpmChart = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jpChart = new javax.swing.JPanel();

        jpmChart.setName("jpmChart"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ru.gss.sccmodeller.SCCModellerApp.class).getContext().getActionMap(DlgChart.class, this);
        jMenuItem1.setAction(actionMap.get("acSaveAs")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jpmChart.add(jMenuItem1);

        jSeparator1.setName("jSeparator1"); // NOI18N
        jpmChart.add(jSeparator1);

        jMenuItem2.setAction(actionMap.get("acParameter")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jpmChart.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.sccmodeller.SCCModellerApp.class).getContext().getResourceMap(DlgChart.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(500, 250));
        setName("Form"); // NOI18N

        jpChart.setName("jpChart"); // NOI18N
        jpChart.setLayout(new javax.swing.BoxLayout(jpChart, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpChart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 898, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpChart, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 310, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JPanel jpChart;
    private javax.swing.JPopupMenu jpmChart;
    // End of variables declaration//GEN-END:variables
    //CHECKSTYLE:ON
}
