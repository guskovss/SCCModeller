/*
 * Stress Corrosion Cracking Modeller
 */
package ru.gss.sccmodeller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.Box;
import org.jdesktop.application.Action;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import ru.gss.sccmodeller.calculation.DataCCDTableModel;
import ru.gss.sccmodeller.chart.ChartMaker;
import ru.gss.sccmodeller.calculation.DlgDataSetEdit;
import ru.gss.sccmodeller.calculation.DataCGRTableModel;
import ru.gss.sccmodeller.calculation.DataSIFTableModel;
import ru.gss.sccmodeller.calculation.DataSetTableModel;
import ru.gss.sccmodeller.chart.DlgParameterChartEdit;
import ru.gss.sccmodeller.commons.FileChooserFactory;
import ru.gss.sccmodeller.data.DataList;
import ru.gss.sccmodeller.data.DataSet;

/**
 * The main frame of the application.
 * @version 1.1.0 20.03.2020
 * @author Sergey Guskov
 */
public class SCCModellerView extends FrameView {

    static {
        UIManager.put("JXTable.column.horizontalScroll", "Горизонтальная прокрутка");
        UIManager.put("JXTable.column.packAll", "Упаковка всех столбцов");
        UIManager.put("JXTable.column.packSelected", "Упаковка выбранного столбца");
    }

    /**
     * Constructor.
     * @param app application
     */
    public SCCModellerView(final SingleFrameApplication app) {
        super(app);
        initComponents();

        //Icon
        //org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.sccmodeller.SCCModellerApp.class).getContext().getResourceMap(SCCModellerView.class);
        //getFrame().setIconImage(resourceMap.getImageIcon("mainFrame.icon").getImage());

        //Translate
        UIManager.put("FileChooser.fileNameLabelText", "Имя файла:");
        UIManager.put("FileChooser.lookInLabelText", "Папка:");
        UIManager.put("FileChooser.saveInLabelText", "Папка:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Тип:");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Фильтр");
        UIManager.put("FileChooser.upFolderToolTipText", "Наверх");
        UIManager.put("FileChooser.homeFolderToolTipText", "Домой");
        UIManager.put("FileChooser.newFolderToolTipText", "Новая папка");
        UIManager.put("FileChooser.listViewButtonToolTipText", "Список");
        UIManager.put("FileChooser.detailsViewButtonToolTipText", "Таблица");
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.openButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.updateButtonText", "Обновить");
        UIManager.put("FileChooser.helpButtonText", "Справка");
        UIManager.put("FileChooser.saveButtonToolTipText", "Сохранить");
        UIManager.put("FileChooser.openButtonToolTipText", "Открыть");
        UIManager.put("FileChooser.cancelButtonToolTipText", "Отмена");
        UIManager.put("FileChooser.updateButtonToolTipText", "Обновить");
        UIManager.put("FileChooser.helpButtonToolTipText", "Справка");
        UIManager.put("FileChooser.openDialogTitleText", "Открыть");
        UIManager.put("FileChooser.saveDialogTitleText", "Сохранить как");
        UIManager.put("ProgressMonitor.progressText", "Загрузка...");
        UIManager.put("OptionPane.cancelButtonText", "Отмена");
        UIManager.put("OptionPane.yesButtonText", "Да");
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.messageDialogTitle", "Внимание");

        //Main objects
        data = new DataList();
        chartMaker = new ChartMaker(this.getComponent(), data);

        //Settings of data table
        tmDataSet = new DataSetTableModel(data);
        jtDataSet.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtDataSet.addHighlighter(HighlighterFactory.createSimpleStriping());
        jtDataSet.setModel(tmDataSet);
        jtDataSet.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                int k = jtDataSet.getSelectedRow();
                if (k > -1) {
                    setSelectDataSet(true);
                    data.setDataSetIndex(jtDataSet.convertRowIndexToModel(k));
                } else {
                    setSelectDataSet(false);
                    data.setDataSetIndex(-1);
                }
                refreshAllTable();
            }
        });
        jtDataSet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if ((e.getClickCount() == 2) && (e.getButton() == 1) && (jtDataSet.getSelectedRow() >= 0)) {
                    acEditDataSet();
                }
            }
        });

        //Settings of CGR table
        tmDataCGR = new DataCGRTableModel(data);
        jtDataCGR.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtDataCGR.addHighlighter(HighlighterFactory.createSimpleStriping());
        jtDataCGR.setModel(tmDataCGR);

        //Settings of SIF table
        tmDataSIF = new DataSIFTableModel(data);
        jtDataSIF.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtDataSIF.addHighlighter(HighlighterFactory.createSimpleStriping());
        jtDataSIF.setModel(tmDataSIF);

        //Settings of CCD table
        tmDataCCD = new DataCCDTableModel(data);
        jtDataCCD.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jtDataCCD.addHighlighter(HighlighterFactory.createSimpleStriping());
        jtDataCCD.setModel(tmDataCCD);

        cpDataCGR = new ChartPanel(ChartMaker.createChart(data, 0));
        cpDataCGR.setPopupMenu(jpmChart);
        cpDataCGR.setMouseWheelEnabled(true);
        jcDataCGR.add(cpDataCGR);

        cpDataSIF = new ChartPanel(ChartMaker.createChart(data, 1));
        cpDataSIF.setPopupMenu(jpmChart);
        cpDataSIF.setMouseWheelEnabled(true);
        jcDataSIF.add(cpDataSIF);

        cpDataCCD = new ChartPanel(ChartMaker.createChart(data, 2));
        cpDataCCD.setPopupMenu(jpmChart);
        cpDataCCD.setMouseWheelEnabled(true);
        jcDataCCD.add(cpDataCCD);

        jtbMain.add(Box.createHorizontalGlue(), 6);
    }

    /**
     * Save log to file.
     */
    @Action
    public void acSaveLogToFile() {
        JFileChooser chooser = FileChooserFactory.getChooser(3);
        if (chooser.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                data.saveTextAreaToFile(f, jtaLog);
                addToLog("Запись сообщений в файл " + f.getAbsolutePath());
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Text message.
     * @param s message
     */
    private void addToLog(final String s) {
        jtaLog.append(s + "\n");
    }

    /**
     * Existing data.
     */
    private boolean existData = false;

    /**
     * Existing data.
     * @return existing data
     */
    public boolean isExistData() {
        return existData;
    }

    /**
     * Existing data.
     * @param b existing data
     */
    public void setExistData(final boolean b) {
        boolean old = isExistData();
        existData = b;
        firePropertyChange("existData", old, isExistData());
    }

    /**
     * Select data set.
     */
    private boolean selectDataSet = false;

    /**
     * Select data set.
     * @return select data set.
     */
    public boolean isSelectDataSet() {
        return selectDataSet;
    }

    /**
     * Select data set.
     * @param b select data set
     */
    public void setSelectDataSet(final boolean b) {
        boolean old = isSelectDataSet();
        selectDataSet = b;
        firePropertyChange("selectDataSet", old, isSelectDataSet());
    }

    /**
     * Message of number parse exeptions.
     * @param n count of exeptions
     */
    private void showParseExceptionMessage(final int n) {
        JOptionPane.showMessageDialog(this.getFrame(),
                "Количество ошибок при распознавании чисел - " + n,
                "Внимание", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Message of data set count.
     */
    private void showDataSetCountMessage() {
        JOptionPane.showMessageDialog(this.getFrame(),
                "Максимальное количество одновременно открытых участков - " + data.getDataSetCountMax() +
                ".\nДля добавления нового участка необходимо удалить один из имеющихся.",
                "Внимание", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Message of exeption.
     * @param ex exeption
     */
    public void showErrorMessage(final Exception ex) {
        JOptionPane.showMessageDialog(
                SCCModellerApp.getApplication().getMainFrame(), ex,
                "Ошибка", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Chart save.
     */
    @Action
    public void acChartSaveAs() {
        JFileChooser ch = FileChooserFactory.getChooser(5);
        if (ch.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = ch.getSelectedFile();
            try {
                ChartUtilities.saveChartAsPNG(f, ((ChartPanel) jpmChart.getInvoker()).getChart(), 1200, 620);
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Chart parameters.
     */
    @Action
    public void acChartParameter() {
        DlgParameterChartEdit d = new DlgParameterChartEdit();
        d.setTempObj(((ChartPanel) jpmChart.getInvoker()).getChart());
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
    }

    /**
     * Refresh all table.
     */
    private void refreshAllTable() {
        tmDataCGR.fireTableDataChanged();
        tmDataSIF.fireTableDataChanged();
        tmDataCCD.fireTableDataChanged();
    }
    
    /**
     * Chart repaint.
     */
    @Action(enabledProperty = "existData")
    public void acPlot() {
        cpDataCGR.setChart(ChartMaker.createChart(data, 0));
        cpDataSIF.setChart(ChartMaker.createChart(data, 1));
        cpDataCCD.setChart(ChartMaker.createChart(data, 2));
    }

    /**
     * Show legend.
     */
    @Action(enabledProperty = "existData")
    public void acShowLegendButton() {
        data.setShowLegend(jbtnShowLegend.isSelected());
        acPlot();
    }

     /**
     * Show CGR chart dialog.
     */
    @Action(enabledProperty = "existData")
    public void acChartCGR() {
        chartMaker.showChartCGR();
    }

    /**
     * Show SIF chart dialog.
     */
    @Action(enabledProperty = "existData")
    public void acChartSIF() {
        chartMaker.showChartSIF();
    }

    /**
     * Show CCD chart dialog.
     */
    @Action(enabledProperty = "existData")
    public void acChartCCD() {
        chartMaker.showChartCCD();
    }

    /**
     * Load model parameters from file.
     */
    @Action
    public void acOpenDataSet() {
        if (data.getDataSetCount() == data.getDataSetCountMax()) {
            showDataSetCountMessage();
            return;
        }
        JFileChooser chooser = FileChooserFactory.getChooser(3);
        if (chooser.showOpenDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            DataSet sc = new DataSet();
            try {
                sc.loadDataFromFile(f);
                data.getDataSet().add(sc);
                int i = data.getDataSet().size() - 1;
                tmDataSet.fireTableRowsInserted(i, i);
                int j = jtDataSet.convertRowIndexToView(i);
                jtDataSet.setRowSelectionInterval(j, j);
                jtDataSet.scrollRectToVisible(jtDataSet.getCellRect(j, 0, true));
                addToLog("Открыт файл с данными " + f.getAbsolutePath());
            } catch (FileNotFoundException ex) {
                showErrorMessage(ex);
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
            if (sc.getParseExceptionCount() > 0) {
                showParseExceptionMessage(sc.getParseExceptionCount());
                return;
            }
            refreshAllTable();
            setExistData(true);
        }
    }

    /**
     * Save model parameters to file.
     */
    @Action(enabledProperty = "selectDataSet")
    public void acSaveDataSet() {
        JFileChooser chooser = FileChooserFactory.getChooser(3);
        if (chooser.showSaveDialog(this.getFrame()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            try {
                int i = jtDataSet.convertRowIndexToModel(jtDataSet.getSelectedRow());
                data.getDataSet().get(i).saveDataToFile(f);
                addToLog("Данные сохранены в файл " + f.getAbsolutePath());
            } catch (IOException ex) {
                showErrorMessage(ex);
            }
        }
    }

    /**
     * Add model parameters set.
     */
    @Action
    public void acAddDataSet() {
        if (data.getDataSetCount() == data.getDataSetCountMax()) {
            showDataSetCountMessage();
            return;
        }
        DlgDataSetEdit d = new DlgDataSetEdit();
        d.setTempObj(d.createTempObj());
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
        if (d.isChangeObj()) {
            data.getDataSet().add(d.getTempObj());
            int i = data.getDataSet().size() - 1;
            tmDataSet.fireTableRowsInserted(i, i);
            int j = jtDataSet.convertRowIndexToView(i);
            jtDataSet.setRowSelectionInterval(j, j);
            jtDataSet.scrollRectToVisible(jtDataSet.getCellRect(j, 0, true));
            setExistData(true);
        }
    }

    /**
     * Edit model parameters set.
     */
    @Action(enabledProperty = "selectDataSet")
    public void acEditDataSet() {
        int i = jtDataSet.convertRowIndexToModel(jtDataSet.getSelectedRow());
        DlgDataSetEdit d = new DlgDataSetEdit();
        d.setTempObj(data.getDataSet().get(i));
        d.setLocationRelativeTo(this.getFrame());
        d.setVisible(true);
        if (d.isChangeObj()) {
            tmDataSet.fireTableRowsUpdated(i, i);
            jtDataSet.getRowSorter().allRowsChanged();
        }
    }

    /**
     * Delete model parameters set.
     */
    @Action(enabledProperty = "selectDataSet")
    public void acDelDataSet() {
        int i = jtDataSet.convertRowIndexToModel(jtDataSet.getSelectedRow());
        data.getDataSet().remove(i);
        tmDataSet.fireTableRowsDeleted(i, i);
        setSelectDataSet(false);
        if (data.getDataSet().isEmpty()) {
            setExistData(false);
            jbtnShowLegend.setSelected(false);
            data.setShowLegend(false);
        }
        acPlot();
    }
    
    /**
     * Calculation crack growth rate.
     */
    @Action(enabledProperty = "selectDataSet")
    public void acCalculateCGR() {
        int i = jtDataSet.convertRowIndexToModel(jtDataSet.getSelectedRow());
        data.getDataSet().get(i).calculateCGR();
        tmDataCGR.fireTableDataChanged();
        cpDataCGR.setChart(ChartMaker.createChart(data, 0));
    }

    /**
     * Calculation stress intensity factor.
     */
    @Action(enabledProperty = "selectDataSet")
    public void acCalculateSIF() {
        int i = jtDataSet.convertRowIndexToModel(jtDataSet.getSelectedRow());
        data.getDataSet().get(i).calculateSIF();
        tmDataSIF.fireTableDataChanged();
        cpDataSIF.setChart(ChartMaker.createChart(data, 1));
    }

    /**
     * Calculation corrosion crack depth.
     */
    @Action(enabledProperty = "selectDataSet")
    public void acCalculateCCD() {
        int i = jtDataSet.convertRowIndexToModel(jtDataSet.getSelectedRow());
        data.getDataSet().get(i).calculateCCD();
        tmDataCCD.fireTableDataChanged();
        cpDataCCD.setChart(ChartMaker.createChart(data, 2));
    }

    /**
     * Action for About button.
     */
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            aboutBox = new SCCModellerAboutBox();
        }
        aboutBox.setLocationRelativeTo(this.getFrame());
        aboutBox.setVisible(true);
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

        mainPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel5 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jtbMain = new javax.swing.JToolBar();
        jbtnOpen = new javax.swing.JButton();
        jbtnSave = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jbtnAdd = new javax.swing.JButton();
        jbtnEdit = new javax.swing.JButton();
        jbtnDel = new javax.swing.JButton();
        jbtnRefresh = new javax.swing.JButton();
        jbtnShowLegend = new javax.swing.JToggleButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        jtDataSet = new org.jdesktop.swingx.JXTable();
        jPanel14 = new javax.swing.JPanel();
        jToolBar7 = new javax.swing.JToolBar();
        jbtnSaveLog = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaLog = new javax.swing.JTextArea();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel7 = new javax.swing.JPanel();
        jcDataCGR = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jtDataCGR = new org.jdesktop.swingx.JXTable();
        jToolBar10 = new javax.swing.JToolBar();
        jbtnCalculateCGR = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        jPanel8 = new javax.swing.JPanel();
        jcDataSIF = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jToolBar12 = new javax.swing.JToolBar();
        jbtnCalculateSIF = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        jtDataSIF = new org.jdesktop.swingx.JXTable();
        jPanel6 = new javax.swing.JPanel();
        jSplitPane5 = new javax.swing.JSplitPane();
        jPanel10 = new javax.swing.JPanel();
        jcDataCCD = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jToolBar14 = new javax.swing.JToolBar();
        jbtnCalculateCCD = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtDataCCD = new org.jdesktop.swingx.JXTable();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu jmFile = new javax.swing.JMenu();
        jmiOpenFile = new javax.swing.JMenuItem();
        jmiSaveFile = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        jmiAdd = new javax.swing.JMenuItem();
        jmiEdit = new javax.swing.JMenuItem();
        jmiDel = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        jmiExit = new javax.swing.JMenuItem();
        jmCalculation = new javax.swing.JMenu();
        jmiCalculationCGR = new javax.swing.JMenuItem();
        jmiCalculationSIF = new javax.swing.JMenuItem();
        jmiCalculationCCD = new javax.swing.JMenuItem();
        jmChart = new javax.swing.JMenu();
        jmiChartCGR = new javax.swing.JMenuItem();
        jmiChartSIF = new javax.swing.JMenuItem();
        jmiChartCCD = new javax.swing.JMenuItem();
        javax.swing.JMenu jmHelp = new javax.swing.JMenu();
        javax.swing.JMenuItem jmiAbout = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        statusMessageLabel = new javax.swing.JLabel();
        jpmChart = new javax.swing.JPopupMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        jMenuItem33 = new javax.swing.JMenuItem();

        mainPanel.setName("mainPanel"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setPreferredSize(new java.awt.Dimension(626, 210));
        jPanel3.setRequestFocusEnabled(false);

        jSplitPane2.setBorder(null);
        jSplitPane2.setDividerLocation(250);
        jSplitPane2.setDividerSize(3);
        jSplitPane2.setMinimumSize(new java.awt.Dimension(200, 226));
        jSplitPane2.setName("jSplitPane2"); // NOI18N

        jPanel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jPanel5.setMinimumSize(new java.awt.Dimension(250, 150));
        jPanel5.setName("jPanel5"); // NOI18N
        jPanel5.setPreferredSize(new java.awt.Dimension(250, 128));

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.setName("jSplitPane1"); // NOI18N

        jPanel4.setMinimumSize(new java.awt.Dimension(100, 150));
        jPanel4.setName("jPanel4"); // NOI18N

        jtbMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 5));
        jtbMain.setFloatable(false);
        jtbMain.setRollover(true);
        jtbMain.setMaximumSize(new java.awt.Dimension(314, 36));
        jtbMain.setMinimumSize(new java.awt.Dimension(228, 36));
        jtbMain.setName("jtbMain"); // NOI18N
        jtbMain.setPreferredSize(new java.awt.Dimension(100, 36));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ru.gss.sccmodeller.SCCModellerApp.class).getContext().getActionMap(SCCModellerView.class, this);
        jbtnOpen.setAction(actionMap.get("acOpenDataSet")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.sccmodeller.SCCModellerApp.class).getContext().getResourceMap(SCCModellerView.class);
        jbtnOpen.setIcon(resourceMap.getIcon("jbtnOpen.icon")); // NOI18N
        jbtnOpen.setDisabledIcon(resourceMap.getIcon("jbtnOpen.disabledIcon")); // NOI18N
        jbtnOpen.setFocusable(false);
        jbtnOpen.setHideActionText(true);
        jbtnOpen.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnOpen.setName("jbtnOpen"); // NOI18N
        jbtnOpen.setRolloverIcon(resourceMap.getIcon("jbtnOpen.rolloverIcon")); // NOI18N
        jbtnOpen.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbMain.add(jbtnOpen);

        jbtnSave.setAction(actionMap.get("acSaveDataSet")); // NOI18N
        jbtnSave.setIcon(resourceMap.getIcon("jbtnSave.icon")); // NOI18N
        jbtnSave.setDisabledIcon(resourceMap.getIcon("jbtnSave.disabledIcon")); // NOI18N
        jbtnSave.setFocusable(false);
        jbtnSave.setHideActionText(true);
        jbtnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnSave.setName("jbtnSave"); // NOI18N
        jbtnSave.setRolloverIcon(resourceMap.getIcon("jbtnSave.rolloverIcon")); // NOI18N
        jbtnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbMain.add(jbtnSave);

        jSeparator8.setName("jSeparator8"); // NOI18N
        jtbMain.add(jSeparator8);

        jbtnAdd.setAction(actionMap.get("acAddDataSet")); // NOI18N
        jbtnAdd.setIcon(resourceMap.getIcon("jbtnAdd.icon")); // NOI18N
        jbtnAdd.setFocusable(false);
        jbtnAdd.setHideActionText(true);
        jbtnAdd.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnAdd.setName("jbtnAdd"); // NOI18N
        jbtnAdd.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbMain.add(jbtnAdd);

        jbtnEdit.setAction(actionMap.get("acEditDataSet")); // NOI18N
        jbtnEdit.setIcon(resourceMap.getIcon("jbtnEdit.icon")); // NOI18N
        jbtnEdit.setDisabledIcon(resourceMap.getIcon("jbtnEdit.disabledIcon")); // NOI18N
        jbtnEdit.setFocusable(false);
        jbtnEdit.setHideActionText(true);
        jbtnEdit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnEdit.setName("jbtnEdit"); // NOI18N
        jbtnEdit.setRolloverIcon(resourceMap.getIcon("jbtnEdit.rolloverIcon")); // NOI18N
        jbtnEdit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbMain.add(jbtnEdit);

        jbtnDel.setAction(actionMap.get("acDelDataSet")); // NOI18N
        jbtnDel.setIcon(resourceMap.getIcon("jbtnDel.icon")); // NOI18N
        jbtnDel.setDisabledIcon(resourceMap.getIcon("jbtnDel.disabledIcon")); // NOI18N
        jbtnDel.setFocusable(false);
        jbtnDel.setHideActionText(true);
        jbtnDel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnDel.setName("jbtnDel"); // NOI18N
        jbtnDel.setRolloverIcon(resourceMap.getIcon("jbtnDel.rolloverIcon")); // NOI18N
        jbtnDel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbMain.add(jbtnDel);

        jbtnRefresh.setAction(actionMap.get("acPlot")); // NOI18N
        jbtnRefresh.setIcon(resourceMap.getIcon("jbtnRefresh.icon")); // NOI18N
        jbtnRefresh.setDisabledIcon(resourceMap.getIcon("jbtnRefresh.disabledIcon")); // NOI18N
        jbtnRefresh.setFocusable(false);
        jbtnRefresh.setHideActionText(true);
        jbtnRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnRefresh.setName("jbtnRefresh"); // NOI18N
        jbtnRefresh.setRolloverIcon(resourceMap.getIcon("jbtnRefresh.rolloverIcon")); // NOI18N
        jbtnRefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbMain.add(jbtnRefresh);

        jbtnShowLegend.setAction(actionMap.get("acShowLegendButton")); // NOI18N
        jbtnShowLegend.setIcon(resourceMap.getIcon("jbtnShowLegend.icon")); // NOI18N
        jbtnShowLegend.setDisabledIcon(resourceMap.getIcon("jbtnShowLegend.disabledIcon")); // NOI18N
        jbtnShowLegend.setFocusable(false);
        jbtnShowLegend.setHideActionText(true);
        jbtnShowLegend.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnShowLegend.setName("jbtnShowLegend"); // NOI18N
        jbtnShowLegend.setRolloverIcon(resourceMap.getIcon("jbtnShowLegend.rolloverIcon")); // NOI18N
        jbtnShowLegend.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbMain.add(jbtnShowLegend);

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        jtDataSet.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtDataSet.setColumnControlVisible(true);
        jtDataSet.setName("jtDataSet"); // NOI18N
        jtDataSet.setSortable(false);
        jScrollPane6.setViewportView(jtDataSet);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jtbMain, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jtbMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
        );

        jSplitPane1.setTopComponent(jPanel4);

        jPanel14.setMinimumSize(new java.awt.Dimension(100, 123));
        jPanel14.setName("jPanel14"); // NOI18N

        jToolBar7.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar7.setFloatable(false);
        jToolBar7.setRollover(true);
        jToolBar7.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar7.setMinimumSize(new java.awt.Dimension(228, 36));
        jToolBar7.setName("jToolBar7"); // NOI18N
        jToolBar7.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnSaveLog.setAction(actionMap.get("acSaveLogToFile")); // NOI18N
        jbtnSaveLog.setIcon(resourceMap.getIcon("jbtnSaveLog.icon")); // NOI18N
        jbtnSaveLog.setDisabledIcon(resourceMap.getIcon("jbtnSaveLog.disabledIcon")); // NOI18N
        jbtnSaveLog.setFocusable(false);
        jbtnSaveLog.setHideActionText(true);
        jbtnSaveLog.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnSaveLog.setName("jbtnSaveLog"); // NOI18N
        jbtnSaveLog.setRolloverIcon(resourceMap.getIcon("jbtnSaveLog.rolloverIcon")); // NOI18N
        jbtnSaveLog.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar7.add(jbtnSaveLog);

        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jtaLog.setColumns(20);
        jtaLog.setEditable(false);
        jtaLog.setFont(resourceMap.getFont("jtaLog.font")); // NOI18N
        jtaLog.setRows(5);
        jtaLog.setWrapStyleWord(true);
        jtaLog.setName("jtaLog"); // NOI18N
        jScrollPane1.setViewportView(jtaLog);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar7, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addComponent(jToolBar7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
        );

        jSplitPane1.setRightComponent(jPanel14);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );

        jSplitPane2.setLeftComponent(jPanel5);

        jTabbedPane2.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jTabbedPane2.setMinimumSize(new java.awt.Dimension(500, 350));
        jTabbedPane2.setName("jTabbedPane2"); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(390, 248));

        jSplitPane3.setBorder(null);
        jSplitPane3.setDividerLocation(330);
        jSplitPane3.setDividerSize(3);
        jSplitPane3.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane3.setResizeWeight(1.0);
        jSplitPane3.setName("jSplitPane3"); // NOI18N

        jPanel7.setMinimumSize(new java.awt.Dimension(0, 200));
        jPanel7.setName("jPanel7"); // NOI18N

        jcDataCGR.setMinimumSize(new java.awt.Dimension(500, 0));
        jcDataCGR.setName("jcDataCGR"); // NOI18N
        jcDataCGR.setLayout(new javax.swing.BoxLayout(jcDataCGR, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcDataCGR, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcDataCGR, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );

        jSplitPane3.setLeftComponent(jPanel7);

        jPanel11.setMinimumSize(new java.awt.Dimension(100, 120));
        jPanel11.setName("jPanel11"); // NOI18N

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        jtDataCGR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtDataCGR.setColumnControlVisible(true);
        jtDataCGR.setName("jtDataCGR"); // NOI18N
        jtDataCGR.setSortable(false);
        jScrollPane7.setViewportView(jtDataCGR);

        jToolBar10.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar10.setFloatable(false);
        jToolBar10.setRollover(true);
        jToolBar10.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar10.setMinimumSize(new java.awt.Dimension(250, 36));
        jToolBar10.setName("jToolBar10"); // NOI18N
        jToolBar10.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnCalculateCGR.setAction(actionMap.get("acCalculateCGR")); // NOI18N
        jbtnCalculateCGR.setIcon(resourceMap.getIcon("jbtnCalculateCGR.icon")); // NOI18N
        jbtnCalculateCGR.setDisabledIcon(resourceMap.getIcon("jbtnCalculateCGR.disabledIcon")); // NOI18N
        jbtnCalculateCGR.setFocusable(false);
        jbtnCalculateCGR.setHideActionText(true);
        jbtnCalculateCGR.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnCalculateCGR.setName("jbtnCalculateCGR"); // NOI18N
        jbtnCalculateCGR.setRolloverIcon(resourceMap.getIcon("jbtnCalculateCGR.rolloverIcon")); // NOI18N
        jbtnCalculateCGR.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar10.add(jbtnCalculateCGR);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar10, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addComponent(jToolBar10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
        );

        jSplitPane3.setRightComponent(jPanel11);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(390, 248));

        jSplitPane4.setBorder(null);
        jSplitPane4.setDividerLocation(330);
        jSplitPane4.setDividerSize(3);
        jSplitPane4.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane4.setResizeWeight(1.0);
        jSplitPane4.setName("jSplitPane4"); // NOI18N

        jPanel8.setMinimumSize(new java.awt.Dimension(0, 200));
        jPanel8.setName("jPanel8"); // NOI18N

        jcDataSIF.setMinimumSize(new java.awt.Dimension(500, 0));
        jcDataSIF.setName("jcDataSIF"); // NOI18N
        jcDataSIF.setLayout(new javax.swing.BoxLayout(jcDataSIF, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcDataSIF, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcDataSIF, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );

        jSplitPane4.setLeftComponent(jPanel8);

        jPanel12.setMinimumSize(new java.awt.Dimension(100, 120));
        jPanel12.setName("jPanel12"); // NOI18N

        jToolBar12.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar12.setFloatable(false);
        jToolBar12.setRollover(true);
        jToolBar12.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar12.setMinimumSize(new java.awt.Dimension(250, 36));
        jToolBar12.setName("jToolBar12"); // NOI18N
        jToolBar12.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnCalculateSIF.setAction(actionMap.get("acCalculateSIF")); // NOI18N
        jbtnCalculateSIF.setIcon(resourceMap.getIcon("jbtnCalculateSIF.icon")); // NOI18N
        jbtnCalculateSIF.setDisabledIcon(resourceMap.getIcon("jbtnCalculateSIF.disabledIcon")); // NOI18N
        jbtnCalculateSIF.setFocusable(false);
        jbtnCalculateSIF.setHideActionText(true);
        jbtnCalculateSIF.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnCalculateSIF.setName("jbtnCalculateSIF"); // NOI18N
        jbtnCalculateSIF.setRolloverIcon(resourceMap.getIcon("jbtnCalculateSIF.rolloverIcon")); // NOI18N
        jbtnCalculateSIF.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar12.add(jbtnCalculateSIF);

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        jtDataSIF.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtDataSIF.setColumnControlVisible(true);
        jtDataSIF.setName("jtDataSIF"); // NOI18N
        jtDataSIF.setSortable(false);
        jScrollPane5.setViewportView(jtDataSIF);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar12, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addComponent(jToolBar12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
        );

        jSplitPane4.setRightComponent(jPanel12);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setPreferredSize(new java.awt.Dimension(390, 248));

        jSplitPane5.setBorder(null);
        jSplitPane5.setDividerLocation(330);
        jSplitPane5.setDividerSize(3);
        jSplitPane5.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane5.setResizeWeight(1.0);
        jSplitPane5.setName("jSplitPane5"); // NOI18N

        jPanel10.setMinimumSize(new java.awt.Dimension(0, 200));
        jPanel10.setName("jPanel10"); // NOI18N

        jcDataCCD.setMinimumSize(new java.awt.Dimension(500, 0));
        jcDataCCD.setName("jcDataCCD"); // NOI18N
        jcDataCCD.setLayout(new javax.swing.BoxLayout(jcDataCCD, javax.swing.BoxLayout.LINE_AXIS));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcDataCCD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jcDataCCD, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
        );

        jSplitPane5.setLeftComponent(jPanel10);

        jPanel13.setMinimumSize(new java.awt.Dimension(100, 120));
        jPanel13.setName("jPanel13"); // NOI18N

        jToolBar14.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 0, 0));
        jToolBar14.setFloatable(false);
        jToolBar14.setRollover(true);
        jToolBar14.setMaximumSize(new java.awt.Dimension(314, 36));
        jToolBar14.setMinimumSize(new java.awt.Dimension(250, 36));
        jToolBar14.setName("jToolBar14"); // NOI18N
        jToolBar14.setPreferredSize(new java.awt.Dimension(100, 36));

        jbtnCalculateCCD.setAction(actionMap.get("acCalculateCCD")); // NOI18N
        jbtnCalculateCCD.setIcon(resourceMap.getIcon("jbtnCalculateCCD.icon")); // NOI18N
        jbtnCalculateCCD.setDisabledIcon(resourceMap.getIcon("jbtnCalculateCCD.disabledIcon")); // NOI18N
        jbtnCalculateCCD.setFocusable(false);
        jbtnCalculateCCD.setHideActionText(true);
        jbtnCalculateCCD.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jbtnCalculateCCD.setName("jbtnCalculateCCD"); // NOI18N
        jbtnCalculateCCD.setRolloverIcon(resourceMap.getIcon("jbtnCalculateCCD.rolloverIcon")); // NOI18N
        jbtnCalculateCCD.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar14.add(jbtnCalculateCCD);

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        jtDataCCD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jtDataCCD.setColumnControlVisible(true);
        jtDataCCD.setName("jtDataCCD"); // NOI18N
        jtDataCCD.setSortable(false);
        jScrollPane4.setViewportView(jtDataCCD);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar14, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jToolBar14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
        );

        jSplitPane5.setRightComponent(jPanel13);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 460, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab(resourceMap.getString("jPanel6.TabConstraints.tabTitle"), jPanel6); // NOI18N

        jSplitPane2.setRightComponent(jTabbedPane2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 772, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        jmFile.setText(resourceMap.getString("jmFile.text")); // NOI18N
        jmFile.setName("jmFile"); // NOI18N

        jmiOpenFile.setAction(actionMap.get("acOpenDataSet")); // NOI18N
        jmiOpenFile.setToolTipText(resourceMap.getString("jmiOpenFile.toolTipText")); // NOI18N
        jmiOpenFile.setName("jmiOpenFile"); // NOI18N
        jmFile.add(jmiOpenFile);

        jmiSaveFile.setAction(actionMap.get("acSaveDataSet")); // NOI18N
        jmiSaveFile.setToolTipText(resourceMap.getString("jmiSaveFile.toolTipText")); // NOI18N
        jmiSaveFile.setName("jmiSaveFile"); // NOI18N
        jmFile.add(jmiSaveFile);

        jSeparator10.setName("jSeparator10"); // NOI18N
        jmFile.add(jSeparator10);

        jmiAdd.setAction(actionMap.get("acAddDataSet")); // NOI18N
        jmiAdd.setText(resourceMap.getString("jmiAdd.text")); // NOI18N
        jmiAdd.setToolTipText(resourceMap.getString("jmiAdd.toolTipText")); // NOI18N
        jmiAdd.setName("jmiAdd"); // NOI18N
        jmFile.add(jmiAdd);

        jmiEdit.setAction(actionMap.get("acEditDataSet")); // NOI18N
        jmiEdit.setText(resourceMap.getString("jmiEdit.text")); // NOI18N
        jmiEdit.setToolTipText(resourceMap.getString("jmiEdit.toolTipText")); // NOI18N
        jmiEdit.setName("jmiEdit"); // NOI18N
        jmFile.add(jmiEdit);

        jmiDel.setAction(actionMap.get("acDelDataSet")); // NOI18N
        jmiDel.setText(resourceMap.getString("jmiDel.text")); // NOI18N
        jmiDel.setToolTipText(resourceMap.getString("jmiDel.toolTipText")); // NOI18N
        jmiDel.setName("jmiDel"); // NOI18N
        jmFile.add(jmiDel);

        jSeparator4.setName("jSeparator4"); // NOI18N
        jmFile.add(jSeparator4);

        jmiExit.setAction(actionMap.get("quit")); // NOI18N
        jmiExit.setToolTipText(resourceMap.getString("jmiExit.toolTipText")); // NOI18N
        jmiExit.setName("jmiExit"); // NOI18N
        jmFile.add(jmiExit);

        menuBar.add(jmFile);

        jmCalculation.setText(resourceMap.getString("jmCalculation.text")); // NOI18N
        jmCalculation.setName("jmCalculation"); // NOI18N

        jmiCalculationCGR.setAction(actionMap.get("acCalculateCGR")); // NOI18N
        jmiCalculationCGR.setText(resourceMap.getString("jmiCalculationCGR.text")); // NOI18N
        jmiCalculationCGR.setToolTipText(resourceMap.getString("jmiCalculationCGR.toolTipText")); // NOI18N
        jmiCalculationCGR.setName("jmiCalculationCGR"); // NOI18N
        jmCalculation.add(jmiCalculationCGR);

        jmiCalculationSIF.setAction(actionMap.get("acCalculateSIF")); // NOI18N
        jmiCalculationSIF.setToolTipText(resourceMap.getString("jmiCalculationSIF.toolTipText")); // NOI18N
        jmiCalculationSIF.setName("jmiCalculationSIF"); // NOI18N
        jmCalculation.add(jmiCalculationSIF);

        jmiCalculationCCD.setAction(actionMap.get("acCalculateCCD")); // NOI18N
        jmiCalculationCCD.setToolTipText(resourceMap.getString("jmiCalculationCCD.toolTipText")); // NOI18N
        jmiCalculationCCD.setName("jmiCalculationCCD"); // NOI18N
        jmCalculation.add(jmiCalculationCCD);

        menuBar.add(jmCalculation);

        jmChart.setText(resourceMap.getString("jmChart.text")); // NOI18N
        jmChart.setName("jmChart"); // NOI18N

        jmiChartCGR.setAction(actionMap.get("acChartCGR")); // NOI18N
        jmiChartCGR.setToolTipText(resourceMap.getString("jmiChartCGR.toolTipText")); // NOI18N
        jmiChartCGR.setName("jmiChartCGR"); // NOI18N
        jmChart.add(jmiChartCGR);

        jmiChartSIF.setAction(actionMap.get("acChartSIF")); // NOI18N
        jmiChartSIF.setText(resourceMap.getString("jmiChartSIF.text")); // NOI18N
        jmiChartSIF.setToolTipText(resourceMap.getString("jmiChartSIF.toolTipText")); // NOI18N
        jmiChartSIF.setName("jmiChartSIF"); // NOI18N
        jmChart.add(jmiChartSIF);

        jmiChartCCD.setAction(actionMap.get("acChartCCD")); // NOI18N
        jmiChartCCD.setText(resourceMap.getString("jmiChartCCD.text")); // NOI18N
        jmiChartCCD.setToolTipText(resourceMap.getString("jmiChartCCD.toolTipText")); // NOI18N
        jmiChartCCD.setName("jmiChartCCD"); // NOI18N
        jmChart.add(jmiChartCCD);

        menuBar.add(jmChart);

        jmHelp.setText(resourceMap.getString("jmHelp.text")); // NOI18N
        jmHelp.setName("jmHelp"); // NOI18N

        jmiAbout.setAction(actionMap.get("showAboutBox")); // NOI18N
        jmiAbout.setName("jmiAbout"); // NOI18N
        jmHelp.add(jmiAbout);

        menuBar.add(jmHelp);

        statusPanel.setMaximumSize(new java.awt.Dimension(32767, 20));
        statusPanel.setName("statusPanel"); // NOI18N

        statusMessageLabel.setMinimumSize(new java.awt.Dimension(20, 20));
        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusMessageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(485, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusMessageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
        );

        jpmChart.setName("jpmChart"); // NOI18N

        jMenuItem13.setAction(actionMap.get("acChartSaveAs")); // NOI18N
        jMenuItem13.setName("jMenuItem13"); // NOI18N
        jpmChart.add(jMenuItem13);

        jSeparator2.setName("jSeparator2"); // NOI18N
        jpmChart.add(jSeparator2);

        jMenuItem33.setAction(actionMap.get("acChartParameter")); // NOI18N
        jMenuItem33.setName("jMenuItem33"); // NOI18N
        jpmChart.add(jMenuItem33);

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JSplitPane jSplitPane4;
    private javax.swing.JSplitPane jSplitPane5;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JToolBar jToolBar10;
    private javax.swing.JToolBar jToolBar12;
    private javax.swing.JToolBar jToolBar14;
    private javax.swing.JToolBar jToolBar7;
    private javax.swing.JButton jbtnAdd;
    private javax.swing.JButton jbtnCalculateCCD;
    private javax.swing.JButton jbtnCalculateCGR;
    private javax.swing.JButton jbtnCalculateSIF;
    private javax.swing.JButton jbtnDel;
    private javax.swing.JButton jbtnEdit;
    private javax.swing.JButton jbtnOpen;
    private javax.swing.JButton jbtnRefresh;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JButton jbtnSaveLog;
    private javax.swing.JToggleButton jbtnShowLegend;
    private javax.swing.JPanel jcDataCCD;
    private javax.swing.JPanel jcDataCGR;
    private javax.swing.JPanel jcDataSIF;
    private javax.swing.JMenu jmCalculation;
    private javax.swing.JMenu jmChart;
    private javax.swing.JMenuItem jmiAdd;
    private javax.swing.JMenuItem jmiCalculationCCD;
    private javax.swing.JMenuItem jmiCalculationCGR;
    private javax.swing.JMenuItem jmiCalculationSIF;
    private javax.swing.JMenuItem jmiChartCCD;
    private javax.swing.JMenuItem jmiChartCGR;
    private javax.swing.JMenuItem jmiChartSIF;
    private javax.swing.JMenuItem jmiDel;
    private javax.swing.JMenuItem jmiEdit;
    private javax.swing.JMenuItem jmiExit;
    private javax.swing.JMenuItem jmiOpenFile;
    private javax.swing.JMenuItem jmiSaveFile;
    private javax.swing.JPopupMenu jpmChart;
    private org.jdesktop.swingx.JXTable jtDataCCD;
    private org.jdesktop.swingx.JXTable jtDataCGR;
    private org.jdesktop.swingx.JXTable jtDataSIF;
    private org.jdesktop.swingx.JXTable jtDataSet;
    private javax.swing.JTextArea jtaLog;
    private javax.swing.JToolBar jtbMain;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private JDialog aboutBox;
    private DataCGRTableModel tmDataCGR;
    private DataSIFTableModel tmDataSIF;
    private DataCCDTableModel tmDataCCD;
    private DataSetTableModel tmDataSet;
    private DataList data;
    private ChartMaker chartMaker;
    private ChartPanel cpDataCGR;
    private ChartPanel cpDataSIF;
    private ChartPanel cpDataCCD;
    //CHECKSTYLE:ON
}
