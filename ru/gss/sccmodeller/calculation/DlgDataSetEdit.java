/*
 * Stress Corrosion Cracking Modeller
 */
package ru.gss.sccmodeller.calculation;

import javax.swing.text.DefaultFormatterFactory;
import org.jdesktop.application.Action;
import ru.gss.sccmodeller.commons.DlgDirEdit;
import ru.gss.sccmodeller.commons.NoLocaleNumberFormatter;
import ru.gss.sccmodeller.data.DataSet;

/**
 * Dialog for edit of model parameters.
 * @version 1.1.0 19.03.2020
 * @author Sergey Guskov
 */
public class DlgDataSetEdit extends DlgDirEdit < DataSet > {

    /**
     * Constructor.
     */
    public DlgDataSetEdit() {
        super();
        initComponents();
        jftfCurrentDensity.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfYieldStrength.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfThresholdSIF.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfWallThickness.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfInitDepth.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfStress.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfTimeBegin.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfTimeEnd.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfTimeStep.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(4)));
        jftfDepthBegin.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfDepthEnd.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfDepthStep.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(4)));
        jftfStressIFBegin.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfStressIFEnd.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(2)));
        jftfStressIFStep.setFormatterFactory(new DefaultFormatterFactory(new NoLocaleNumberFormatter(4)));
    }

    /**
     * Setter editing object.
     * @param aTempObj editing object
     */
    @Override
    public void setTempObj(final DataSet aTempObj) {
        putTempObj(aTempObj);
        jtfName.setText(getTempObj().getName());
        jtfFileName.setText(getTempObj().getFileName());
        jftfCurrentDensity.setValue(getTempObj().getCurrentDensity());
        jftfYieldStrength.setValue(getTempObj().getYieldStrength());
        jftfThresholdSIF.setValue(getTempObj().getThresholdSIF());
        jftfWallThickness.setValue(getTempObj().convertMeterToMillimeter(getTempObj().getWallThickness()));
        jftfInitDepth.setValue(getTempObj().convertMeterToMillimeter(getTempObj().getInitDepth()));
        jftfStress.setValue(getTempObj().getStress());
        jftfTimeBegin.setValue(getTempObj().convertSecondToYear(getTempObj().getTimeBegin()));
        jftfTimeEnd.setValue(getTempObj().convertSecondToYear(getTempObj().getTimeEnd()));
        jftfTimeStep.setValue(getTempObj().convertSecondToYear(getTempObj().getTimeStep()));
        jftfDepthBegin.setValue(getTempObj().convertMeterToMillimeter(getTempObj().getDepthBegin()));
        jftfDepthEnd.setValue(getTempObj().convertMeterToMillimeter(getTempObj().getDepthEnd()));
        jftfDepthStep.setValue(getTempObj().convertMeterToMillimeter(getTempObj().getDepthStep()));
        jftfStressIFBegin.setValue(getTempObj().getStressIFBegin());
        jftfStressIFEnd.setValue(getTempObj().getStressIFEnd());
        jftfStressIFStep.setValue(getTempObj().getStressIFStep());
        getRootPane().setDefaultButton(jbtnOk);
    }

    /**
     * Init new object.
     * @return new object
     */
    @Override
    public DataSet createTempObj() {
        return new DataSet();
    }

    /**
     * Action for Cancel button.
     */
    @Action
    public void acCancel() {
        setChangeObj(false);
    }

    /**
     * Action for OK button.
     */
    @Action
    public void acOk() {
        if (checkFormattedTextFieldNullNoSupposed(jftfCurrentDensity, 1e-5, 1e4)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfYieldStrength, 1e-5, 1e4)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfThresholdSIF, 1e-5, 1e4)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfWallThickness, 1.0, 1e2)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfInitDepth, 1e-3, 1e1)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfStress, 0.0, 1e3)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfTimeBegin, 0.0, 1e4)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfTimeEnd, 0.0, 1e4)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfTimeStep, 1e-5, 1e3)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfDepthBegin, 0.0, 1e2)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfDepthEnd, 0.0, 1e2)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfDepthStep, 1e-3, 1e1)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfStressIFBegin, 0.0, 1e4)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfStressIFEnd, 0.0, 1e4)) {
            return;
        }
        if (checkFormattedTextFieldNullNoSupposed(jftfStressIFStep, 1e-5, 1e4)) {
            return;
        }
        getTempObj().setName(jtfName.getText().trim());
        getTempObj().setCurrentDensity(getDoubleFromFormattedTextField(jftfCurrentDensity));
        getTempObj().setYieldStrength(getDoubleFromFormattedTextField(jftfYieldStrength));
        getTempObj().setThresholdSIF(getDoubleFromFormattedTextField(jftfThresholdSIF));
        getTempObj().setWallThickness(getTempObj().convertMillimeterToMeter(getDoubleFromFormattedTextField(jftfWallThickness)));
        getTempObj().setInitDepth(getTempObj().convertMillimeterToMeter(getDoubleFromFormattedTextField(jftfInitDepth)));
        getTempObj().setStress(getDoubleFromFormattedTextField(jftfStress));
        getTempObj().setTimeBegin(getTempObj().convertYearToSecond(getDoubleFromFormattedTextField(jftfTimeBegin)));
        getTempObj().setTimeEnd(getTempObj().convertYearToSecond(getDoubleFromFormattedTextField(jftfTimeEnd)));
        getTempObj().setTimeStep(getTempObj().convertYearToSecond(getDoubleFromFormattedTextField(jftfTimeStep)));
        getTempObj().setDepthBegin(getTempObj().convertMillimeterToMeter(getDoubleFromFormattedTextField(jftfDepthBegin)));
        getTempObj().setDepthEnd(getTempObj().convertMillimeterToMeter(getDoubleFromFormattedTextField(jftfDepthEnd)));
        getTempObj().setDepthStep(getTempObj().convertMillimeterToMeter(getDoubleFromFormattedTextField(jftfDepthStep)));
        getTempObj().setStressIFBegin(getDoubleFromFormattedTextField(jftfStressIFBegin));
        getTempObj().setStressIFEnd(getDoubleFromFormattedTextField(jftfStressIFEnd));
        getTempObj().setStressIFStep(getDoubleFromFormattedTextField(jftfStressIFStep));
        setChangeObj(true);
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

        jbtnOk = new javax.swing.JButton();
        jbtnCancel = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jtfName = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jtfFileName = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlbCurrentDensity = new javax.swing.JLabel();
        jftfCurrentDensity = new javax.swing.JFormattedTextField();
        jlbYieldStrength = new javax.swing.JLabel();
        jftfYieldStrength = new javax.swing.JFormattedTextField();
        jlbThresholdSIF = new javax.swing.JLabel();
        jftfThresholdSIF = new javax.swing.JFormattedTextField();
        jlbWallThickness = new javax.swing.JLabel();
        jftfWallThickness = new javax.swing.JFormattedTextField();
        jlbStress = new javax.swing.JLabel();
        jftfStress = new javax.swing.JFormattedTextField();
        jlbInitDepth = new javax.swing.JLabel();
        jftfInitDepth = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jlbTimeBegin = new javax.swing.JLabel();
        jlbTimeEnd = new javax.swing.JLabel();
        jlbTimeStep = new javax.swing.JLabel();
        jftfTimeBegin = new javax.swing.JFormattedTextField();
        jftfTimeEnd = new javax.swing.JFormattedTextField();
        jftfTimeStep = new javax.swing.JFormattedTextField();
        jPanel4 = new javax.swing.JPanel();
        jlbDepthBegin = new javax.swing.JLabel();
        jlbDepthEnd = new javax.swing.JLabel();
        jlbDepthStep = new javax.swing.JLabel();
        jftfDepthBegin = new javax.swing.JFormattedTextField();
        jftfDepthEnd = new javax.swing.JFormattedTextField();
        jftfDepthStep = new javax.swing.JFormattedTextField();
        jPanel7 = new javax.swing.JPanel();
        jlbStressIFBegin = new javax.swing.JLabel();
        jlbStressIFEnd = new javax.swing.JLabel();
        jlbStressIFStep = new javax.swing.JLabel();
        jftfStressIFBegin = new javax.swing.JFormattedTextField();
        jftfStressIFEnd = new javax.swing.JFormattedTextField();
        jftfStressIFStep = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(ru.gss.sccmodeller.SCCModellerApp.class).getContext().getResourceMap(DlgDataSetEdit.class);
        setTitle(resourceMap.getString("Form.title")); // NOI18N
        setModal(true);
        setName("Form"); // NOI18N
        setResizable(false);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(ru.gss.sccmodeller.SCCModellerApp.class).getContext().getActionMap(DlgDataSetEdit.class, this);
        jbtnOk.setAction(actionMap.get("acOk")); // NOI18N
        jbtnOk.setName("jbtnOk"); // NOI18N

        jbtnCancel.setAction(actionMap.get("acCancel")); // NOI18N
        jbtnCancel.setName("jbtnCancel"); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jtfName.setName("jtfName"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfName, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jtfName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jtfFileName.setBackground(resourceMap.getColor("jtfFileName.background")); // NOI18N
        jtfFileName.setEditable(false);
        jtfFileName.setName("jtfFileName"); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jtfFileName, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jtfFileName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel5.border.title"))); // NOI18N
        jPanel5.setName("jPanel5"); // NOI18N

        jlbCurrentDensity.setText(resourceMap.getString("jlbCurrentDensity.text")); // NOI18N
        jlbCurrentDensity.setName("jlbCurrentDensity"); // NOI18N

        jftfCurrentDensity.setName("jftfCurrentDensity"); // NOI18N

        jlbYieldStrength.setText(resourceMap.getString("jlbYieldStrength.text")); // NOI18N
        jlbYieldStrength.setName("jlbYieldStrength"); // NOI18N

        jftfYieldStrength.setName("jftfYieldStrength"); // NOI18N

        jlbThresholdSIF.setText(resourceMap.getString("jlbThresholdSIF.text")); // NOI18N
        jlbThresholdSIF.setName("jlbThresholdSIF"); // NOI18N

        jftfThresholdSIF.setName("jftfThresholdSIF"); // NOI18N

        jlbWallThickness.setText(resourceMap.getString("jlbWallThickness.text")); // NOI18N
        jlbWallThickness.setName("jlbWallThickness"); // NOI18N

        jftfWallThickness.setName("jftfWallThickness"); // NOI18N

        jlbStress.setText(resourceMap.getString("jlbStress.text")); // NOI18N
        jlbStress.setName("jlbStress"); // NOI18N

        jftfStress.setName("jftfStress"); // NOI18N

        jlbInitDepth.setText(resourceMap.getString("jlbInitDepth.text")); // NOI18N
        jlbInitDepth.setName("jlbInitDepth"); // NOI18N

        jftfInitDepth.setName("jftfInitDepth"); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbYieldStrength)
                    .addComponent(jlbThresholdSIF)
                    .addComponent(jlbWallThickness)
                    .addComponent(jlbCurrentDensity)
                    .addComponent(jlbInitDepth)
                    .addComponent(jlbStress))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 88, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jftfInitDepth)
                    .addComponent(jftfStress, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jftfWallThickness)
                    .addComponent(jftfThresholdSIF)
                    .addComponent(jftfYieldStrength)
                    .addComponent(jftfCurrentDensity, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbCurrentDensity)
                    .addComponent(jftfCurrentDensity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbYieldStrength)
                    .addComponent(jftfYieldStrength, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbThresholdSIF)
                    .addComponent(jftfThresholdSIF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbWallThickness)
                    .addComponent(jftfWallThickness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbInitDepth)
                    .addComponent(jftfInitDepth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbStress)
                    .addComponent(jftfStress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel6.border.title"))); // NOI18N
        jPanel6.setName("jPanel6"); // NOI18N

        jTabbedPane1.setName("jTabbedPane1"); // NOI18N

        jPanel3.setName("jPanel3"); // NOI18N

        jlbTimeBegin.setText(resourceMap.getString("jlbTimeBegin.text")); // NOI18N
        jlbTimeBegin.setName("jlbTimeBegin"); // NOI18N

        jlbTimeEnd.setText(resourceMap.getString("jlbTimeEnd.text")); // NOI18N
        jlbTimeEnd.setName("jlbTimeEnd"); // NOI18N

        jlbTimeStep.setText(resourceMap.getString("jlbTimeStep.text")); // NOI18N
        jlbTimeStep.setName("jlbTimeStep"); // NOI18N

        jftfTimeBegin.setName("jftfTimeBegin"); // NOI18N

        jftfTimeEnd.setName("jftfTimeEnd"); // NOI18N

        jftfTimeStep.setName("jftfTimeStep"); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbTimeBegin)
                    .addComponent(jlbTimeStep)
                    .addComponent(jlbTimeEnd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 158, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jftfTimeStep)
                    .addComponent(jftfTimeEnd)
                    .addComponent(jftfTimeBegin, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jftfTimeBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jftfTimeEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jftfTimeStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jlbTimeBegin)
                        .addGap(13, 13, 13)
                        .addComponent(jlbTimeEnd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbTimeStep)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), jPanel3); // NOI18N

        jPanel4.setName("jPanel4"); // NOI18N

        jlbDepthBegin.setText(resourceMap.getString("jlbDepthBegin.text")); // NOI18N
        jlbDepthBegin.setName("jlbDepthBegin"); // NOI18N

        jlbDepthEnd.setText(resourceMap.getString("jlbDepthEnd.text")); // NOI18N
        jlbDepthEnd.setName("jlbDepthEnd"); // NOI18N

        jlbDepthStep.setText(resourceMap.getString("jlbDepthStep.text")); // NOI18N
        jlbDepthStep.setName("jlbDepthStep"); // NOI18N

        jftfDepthBegin.setName("jftfDepthBegin"); // NOI18N

        jftfDepthEnd.setName("jftfDepthEnd"); // NOI18N

        jftfDepthStep.setName("jftfDepthStep"); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbDepthBegin)
                    .addComponent(jlbDepthStep)
                    .addComponent(jlbDepthEnd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jftfDepthStep)
                    .addComponent(jftfDepthEnd)
                    .addComponent(jftfDepthBegin, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jftfDepthBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jftfDepthEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jftfDepthStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jlbDepthBegin)
                        .addGap(13, 13, 13)
                        .addComponent(jlbDepthEnd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbDepthStep)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel4.TabConstraints.tabTitle"), jPanel4); // NOI18N

        jPanel7.setName("jPanel7"); // NOI18N

        jlbStressIFBegin.setText(resourceMap.getString("jlbStressIFBegin.text")); // NOI18N
        jlbStressIFBegin.setName("jlbStressIFBegin"); // NOI18N

        jlbStressIFEnd.setText(resourceMap.getString("jlbStressIFEnd.text")); // NOI18N
        jlbStressIFEnd.setName("jlbStressIFEnd"); // NOI18N

        jlbStressIFStep.setText(resourceMap.getString("jlbStressIFStep.text")); // NOI18N
        jlbStressIFStep.setName("jlbStressIFStep"); // NOI18N

        jftfStressIFBegin.setName("jftfStressIFBegin"); // NOI18N

        jftfStressIFEnd.setName("jftfStressIFEnd"); // NOI18N

        jftfStressIFStep.setName("jftfStressIFStep"); // NOI18N

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jlbStressIFBegin)
                    .addComponent(jlbStressIFStep)
                    .addComponent(jlbStressIFEnd))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 141, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jftfStressIFStep)
                    .addComponent(jftfStressIFEnd)
                    .addComponent(jftfStressIFBegin, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jftfStressIFBegin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jftfStressIFEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jftfStressIFStep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jlbStressIFBegin)
                        .addGap(13, 13, 13)
                        .addComponent(jlbStressIFEnd)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jlbStressIFStep)))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab(resourceMap.getString("jPanel7.TabConstraints.tabTitle"), jPanel7); // NOI18N

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jbtnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jbtnOk)
                    .addComponent(jbtnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton jbtnCancel;
    private javax.swing.JButton jbtnOk;
    private javax.swing.JFormattedTextField jftfCurrentDensity;
    private javax.swing.JFormattedTextField jftfDepthBegin;
    private javax.swing.JFormattedTextField jftfDepthEnd;
    private javax.swing.JFormattedTextField jftfDepthStep;
    private javax.swing.JFormattedTextField jftfInitDepth;
    private javax.swing.JFormattedTextField jftfStress;
    private javax.swing.JFormattedTextField jftfStressIFBegin;
    private javax.swing.JFormattedTextField jftfStressIFEnd;
    private javax.swing.JFormattedTextField jftfStressIFStep;
    private javax.swing.JFormattedTextField jftfThresholdSIF;
    private javax.swing.JFormattedTextField jftfTimeBegin;
    private javax.swing.JFormattedTextField jftfTimeEnd;
    private javax.swing.JFormattedTextField jftfTimeStep;
    private javax.swing.JFormattedTextField jftfWallThickness;
    private javax.swing.JFormattedTextField jftfYieldStrength;
    private javax.swing.JLabel jlbCurrentDensity;
    private javax.swing.JLabel jlbDepthBegin;
    private javax.swing.JLabel jlbDepthEnd;
    private javax.swing.JLabel jlbDepthStep;
    private javax.swing.JLabel jlbInitDepth;
    private javax.swing.JLabel jlbStress;
    private javax.swing.JLabel jlbStressIFBegin;
    private javax.swing.JLabel jlbStressIFEnd;
    private javax.swing.JLabel jlbStressIFStep;
    private javax.swing.JLabel jlbThresholdSIF;
    private javax.swing.JLabel jlbTimeBegin;
    private javax.swing.JLabel jlbTimeEnd;
    private javax.swing.JLabel jlbTimeStep;
    private javax.swing.JLabel jlbWallThickness;
    private javax.swing.JLabel jlbYieldStrength;
    private javax.swing.JTextField jtfFileName;
    private javax.swing.JTextField jtfName;
    // End of variables declaration//GEN-END:variables
    //CHECKSTYLE:ON
}
