/*
 * Stress Corrosion Cracking Modeller
 */
package ru.gss.sccmodeller.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Model parameters.
 * @version 1.1.0 20.03.2020
 * @author Sergey Guskov
 */
public class DataSet {

    /**
     * Name of parameter set.
     */
    private String name;
    /**
     * Name of file.
     */
    private String fileName;
    /**
     * Anodic current density, A/м2.
     */
    private double currentDensity;
    /**
     * Yield strength, MPa.
     */
    private double yieldStrength;
    /**
     * Threshold stress intensity factor, MPam1/2.
     */
    private double thresholdSIF;
    /**
     * Wall thickness, m.
     */
    private double wallThickness;
    /**
     * Initial depth of crack, m.
     */
    private double initDepth;
    /**
     * Stress, MPa.
     */
    private double stress;
    /**
     * Begin of time interval, s.
     */
    private double timeBegin;
    /**
     * End of time interval, s.
     */
    private double timeEnd;
    /**
     * Step of time interval, s.
     */
    private double timeStep;
    /**
     * Begin of depth interval, m.
     */
    private double depthBegin;
    /**
     * End of depth interval, m.
     */
    private double depthEnd;
    /**
     * Step of depth interval, m.
     */
    private double depthStep;
    /**
     * Begin of stress intensity factor interval, MPam1/2.
     */
    private double stressIFBegin;
    /**
     * End of stress intensity factor interval, MPam1/2.
     */
    private double stressIFEnd;
    /**
     * Step of stress intensity factor interval, MPam1/2.
     */
    private double stressIFStep;
    /**
     * Dependence crack growth rate - stress intensity factor.
     */
    private ArrayList<DataLine> dataCGR;
    /**
     * Dependence stress intensity factor - corrosion crack depth.
     */
    private ArrayList<DataLine> dataSIF;
    /**
     * Dependence corrosion crack depth - time.
     */
    private ArrayList<DataLine> dataCCD;
    /**
     * Count of time substep for calculation.
     */
    private int timeSubStepCount;
    /**
     * Count of parse exeptions.
     */
    private int parseExceptionCount;

    /**
     * Constructor.
     */
    public DataSet() {
        name = "";
        fileName = "";
        currentDensity = 200;
        yieldStrength = 384;
        thresholdSIF = 20;
        wallThickness = 16e-3;
        initDepth = 1.5e-3;
        stress = 250;
        timeBegin = 0.0;
        timeEnd = convertYearToSecond(3);
        timeStep = convertYearToSecond(0.03);
        depthBegin = 0.0;
        depthEnd = 4e-3;
        depthStep = 1e-5;
        stressIFBegin = 0.0;
        stressIFEnd = 50.0;
        stressIFStep = 0.5;
        timeSubStepCount = 10000;
        parseExceptionCount = 0;
        dataCGR = new ArrayList<DataLine>();
        dataSIF = new ArrayList<DataLine>();
        dataCCD = new ArrayList<DataLine>();
    }

    /**
     * Parse integer value.
     * @param s string representation of integer value
     * @return integer value or null
     */
    private Integer parseInteger(final String s) {
        if (s.trim().isEmpty()) {
            return null;
        }
        if (s.equals("-")) {
            return null;
        }
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException ex) {
            parseExceptionCount++;
            return null;
        }
    }

    /**
     * Parse double value.
     * @param s string representation of double value
     * @return double value or null
     */
    private Double parseDouble(final String s) {
        if (s.trim().isEmpty()) {
            return null;
        }
        if (s.equals("-")) {
            return null;
        }
        try {
            String ss = s.replaceAll(",", ".");
            return Double.valueOf(ss);
        } catch (NumberFormatException ex) {
            parseExceptionCount++;
            return null;
        }
    }

    /**
     * Convert integer value to string.
     * @param value integer value
     * @return string representation of value
     */
    private String convertToString(final Integer value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }

    /**
     * Convert double value to string.
     * @param value double value
     * @return string representation of value
     */
    private String convertToString(final Double value) {
        if (value == null) {
            return "";
        }
        return String.format(Locale.US, "%.2f", value);
    }

    /**
     * Convert double value to string.
     * @param value double value
     * @param precision count of symbols after separator
     * @return string representation of value
     */
    private String convertToString(final Double value, final int precision) {
        if (value == null) {
            return " ";
        }
        return String.format(Locale.US, "%." + precision + "f", value);
    }

     /**
     * Load data from file.
     * @param file file
     * @throws java.io.IOException exception
     */
    public void loadDataFromFile(final File file) throws IOException {
        BufferedReader reader = null;
        try {
            //Read all lines from file
            reader = new BufferedReader(new FileReader(file));
            ArrayList<String> strings = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                strings.add(line);
            }
            dataCGR.clear();
            dataSIF.clear();
            dataCCD.clear();
            parseExceptionCount = 0;
            fileName = file.getAbsolutePath();
            //Parse data
            if (strings.size() > 15) {
                setName(strings.get(0).trim());
                setCurrentDensity(parseDouble(strings.get(1).trim()));
                setYieldStrength(parseDouble(strings.get(2).trim()));
                setThresholdSIF(parseDouble(strings.get(3).trim()));
                setWallThickness(convertMillimeterToMeter(parseDouble(strings.get(4).trim())));
                setInitDepth(convertMillimeterToMeter(parseDouble(strings.get(5).trim())));
                setStress(parseDouble(strings.get(6).trim()));
                setTimeBegin(convertYearToSecond(parseDouble(strings.get(7).trim())));
                setTimeEnd(convertYearToSecond(parseDouble(strings.get(8).trim())));
                setTimeStep(convertYearToSecond(parseDouble(strings.get(9).trim())));
                setDepthBegin(convertMillimeterToMeter(parseDouble(strings.get(10).trim())));
                setDepthEnd(convertMillimeterToMeter(parseDouble(strings.get(11).trim())));
                setDepthStep(convertMillimeterToMeter(parseDouble(strings.get(12).trim())));
                setStressIFBegin(parseDouble(strings.get(13).trim()));
                setStressIFEnd(parseDouble(strings.get(14).trim()));
                setStressIFStep(parseDouble(strings.get(15).trim()));
            }
        } finally {
            reader.close();
        }
    }

    /**
     * Save data to file.
     * @param file file
     * @throws java.io.IOException exception
     */
    public void saveDataToFile(final File file) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            //Save parameters
            writer.write(name);
            writer.newLine();
            writer.write(convertToString(currentDensity, 2));
            writer.newLine();
            writer.write(convertToString(yieldStrength, 2));
            writer.newLine();
            writer.write(convertToString(thresholdSIF, 2));
            writer.newLine(); 
            writer.write(convertToString(convertMeterToMillimeter(wallThickness), 2));
            writer.newLine();
            writer.write(convertToString(convertMeterToMillimeter(initDepth), 2));
            writer.newLine();
            writer.write(convertToString(stress, 2));
            writer.newLine();
            writer.write(convertToString(convertSecondToYear(timeBegin), 2));
            writer.newLine();
            writer.write(convertToString(convertSecondToYear(timeEnd), 2));
            writer.newLine();
            writer.write(convertToString(convertSecondToYear(timeStep), 4));
            writer.newLine();
            writer.write(convertToString(convertMeterToMillimeter(depthBegin), 2));
            writer.newLine();
            writer.write(convertToString(convertMeterToMillimeter(depthEnd), 2));
            writer.newLine();
            writer.write(convertToString(convertMeterToMillimeter(depthStep), 4));
            writer.newLine();
            writer.write(convertToString(stressIFBegin, 2));
            writer.newLine();
            writer.write(convertToString(stressIFEnd, 2));
            writer.newLine();
            writer.write(convertToString(stressIFStep, 4));
            writer.newLine();
            fileName = file.getAbsolutePath();
        } finally {
            writer.close();
        }
    }

    /**
     * Calculation crack growth rate.
     * @param k stress intensity factor, MPam1/2
     * @param k0 threshold stress intensity factor, MPam1/2
     * @param i0 anodic current density, A/м2
     * @param s0 yield strength, MPa
     * @return crack growth rate, m/s
     */
    private double crackGrowthRate(final double k, final double k0, final double i0, final double s0) {
        //Number of electrons exchanged in corrosion
        int z = 2;
        //Atomic mass of iron, kg/mol
        double m = 55.845e-3;
        //Faraday’s constant, C/mol
        double f = 96485;
        //Density of iron, kg/m3
        double ro = 7.847e3;
        //Incubation of repassivation, s
        double t0 = 1e-2;
        //Specific length, m
        double r0 = 1e-6;
        //Rupture ductility of passive film
        double e0 = 1e-3;
        //Rice’s coefficient
        double bt = 5.08;
        //Repassivation kinetic exponent
        double ns = 0.667;
        //Strain-hardening exponent
        double nb = 6;
        //Young’s modulus, MPa
        double e = 200e3;

        //Calculation
        double v = 0.0;
        if (k > k0) {
            double v1 = Math.log((k * k - k0 * k0) / (3 * Math.PI * r0 * s0 * s0));
            double v2 = Math.pow(v1, ((nb + 1) / (nb - 1)));
            double v3 = t0 * 2 * nb * bt * s0 * v2 / (e0 * r0 * (nb - 1) * e);
            double v4 = Math.pow(v3, ns);
            double v5 = m * i0 * v4 / (z * f * ro * (1 - ns));
            v = Math.pow(v5, (1 / (1 - ns)));
        }
        return v;
    }

    /**
     * Calculation stress intensity factor.
     * @param a corrosion crack depth, m
     * @param w wall thickness, m
     * @param s stress, MPa
     * @return stress intensity factor, MPam1/2
     */
    private double stressIntensityFactor(final double a, final double w, final double s) {
        double k1 = Math.sqrt(2 * w * Math.tan(Math.PI * a / 2 / w));
        double k2 = Math.cos(Math.PI * a / 2 / w);
        double k3 = 0.752 + 2.02 * a / w + 0.37 * Math.pow((1 - Math.sin(Math.PI * a / 2 / w)), 3);
        double k = s * k1 / k2 * k3;
        return k;
    }

    /**
     * Calculation dependence for CGR.
     */
    public void calculateCGR() {
        dataCGR.clear();
        double k = stressIFBegin;
        while (k <= stressIFEnd) {            
            dataCGR.add(new DataLine(k, crackGrowthRate(k, thresholdSIF, currentDensity, yieldStrength) * 31536e6));
            k = k + stressIFStep;
        }
    }

    /**
     * Calculation dependence for SIF.
     */
    public void calculateSIF() {
        dataSIF.clear();
        double a = depthBegin;
        while (a <= depthEnd) {
            if (a >= 0.9 * wallThickness) {
                break;
            }
            dataSIF.add(new DataLine(convertMeterToMillimeter(a), stressIntensityFactor(a, wallThickness, stress)));
            a = a + depthStep;
        }
    }

    /**
     * Calculation dependence for CCD.
     */
    public void calculateCCD() {
        dataCCD.clear();
        double v = 0;
        double k = 0;
        double a = initDepth;
        double t = timeBegin;
        double ts = timeStep / timeSubStepCount;
        int i = 0;
        while (t <= timeEnd) {
            if (a > 0.9 * wallThickness) {
                return;
            }
            if ((i % timeSubStepCount) == 0) {
                dataCCD.add(new DataLine(convertSecondToYear(t), convertMeterToMillimeter(a)));
            }
            k = stressIntensityFactor(a, wallThickness, stress);
            v = crackGrowthRate(k, thresholdSIF, currentDensity, yieldStrength);
            a = a + v * ts;
            t = t + ts;
            i++;
        }
    }

    /**
     * Name of parameter set.
     * @return name of parameter set
     */
    public String getName() {
        return name;
    }

    /**
     * Name of parameter set.
     * @param aName name of parameter set
     */
    public void setName(final String aName) {
        name = aName;
    }

    /**
     * Name of file.
     * @return name of file
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Name of file.
     * @param aFileName name of file
     */
    public void setFileName(final String aFileName) {
        fileName = aFileName;
    }

    /**
     * Anodic current density.
     * @return anodic current density
     */
    public double getCurrentDensity() {
        return currentDensity;
    }

    /**
     * Anodic current density.
     * @param aCurrentDensity anodic current density
     */
    public void setCurrentDensity(final double aCurrentDensity) {
        currentDensity = aCurrentDensity;
    }

    /**
     * Yield strength.
     * @return yield strength
     */
    public double getYieldStrength() {
        return yieldStrength;
    }

    /**
     * Yield strength.
     * @param aYieldStrength yield strength
     */
    public void setYieldStrength(final double aYieldStrength) {
        yieldStrength = aYieldStrength;
    }

   /**
     * Threshold stress intensity factor.
     * @return threshold stress intensity factor
     */
    public double getThresholdSIF() {
        return thresholdSIF;
    }

   /**
     * Threshold stress intensity factor.
     * @param aThresholdSIF threshold stress intensity factor
     */
    public void setThresholdSIF(final double aThresholdSIF) {
        thresholdSIF = aThresholdSIF;
    }

    /**
     * Wall thickness.
     * @return wall thickness
     */
    public double getWallThickness() {
        return wallThickness;
    }

    /**
     * Wall thickness.
     * @param aWallThickness wall thickness
     */
    public void setWallThickness(final double aWallThickness) {
        wallThickness = aWallThickness;
    }

    /**
     * Stress.
     * @return stress
     */
    public double getStress() {
        return stress;
    }

    /**
     * Stress.
     * @param aStress stress
     */
    public void setStress(final double aStress) {
        stress = aStress;
    }

    /**
     * Initial depth of crack.
     * @return initial depth of crack
     */
    public double getInitDepth() {
        return initDepth;
    }

    /**
     * Initial depth of crack.
     * @param aInitDepth initial depth of crack
     */
    public void setInitDepth(final double aInitDepth) {
        initDepth = aInitDepth;
    }

    /**
     * Begin of time interval.
     * @return begin of time interval
     */
    public double getTimeBegin() {
        return timeBegin;
    }

    /**
     * Begin of time interval.
     * @param aTimeBegin begin of time interval
     */
    public void setTimeBegin(final double aTimeBegin) {
        timeBegin = aTimeBegin;
    }

    /**
     * End of time interval.
     * @return end of time interval
     */
    public double getTimeEnd() {
        return timeEnd;
    }

    /**
     * End of time interval.
     * @param aTimeEnd end of time interval
     */
    public void setTimeEnd(final double aTimeEnd) {
        timeEnd = aTimeEnd;
    }

    /**
     * Step of time interval.
     * @return step of time interval
     */
    public double getTimeStep() {
        return timeStep;
    }

    /**
     * Step of time interval.
     * @param aTimeStep step of time interval
     */
    public void setTimeStep(final double aTimeStep) {
        timeStep = aTimeStep;
    }

    /**
     * Begin of depth interval.
     * @return begin of depth interval
     */
    public double getDepthBegin() {
        return depthBegin;
    }

    /**
     * Begin of depth interval.
     * @param aDepthBegin begin of depth interval
     */
    public void setDepthBegin(final double aDepthBegin) {
        depthBegin = aDepthBegin;
    }

    /**
     * End of depth interval.
     * @return end of depth interval
     */
    public double getDepthEnd() {
        return depthEnd;
    }

    /**
     * End of depth interval.
     * @param aDepthEnd end of depth interval
     */
    public void setDepthEnd(final double aDepthEnd) {
        depthEnd = aDepthEnd;
    }

    /**
     * Step of depth interval.
     * @return step of depth interval
     */
    public double getDepthStep() {
        return depthStep;
    }

    /**
     * Step of depth interval.
     * @param aDepthStep step of depth interval
     */
    public void setDepthStep(final double aDepthStep) {
        depthStep = aDepthStep;
    }

    /**
     * Begin of stress intensity factor interval.
     * @return begin of stress intensity factor interval
     */
    public double getStressIFBegin() {
        return stressIFBegin;
    }

    /**
     * Begin of stress intensity factor interval.
     * @param aStressIFBegin begin of stress intensity factor interval
     */
    public void setStressIFBegin(final double aStressIFBegin) {
        stressIFBegin = aStressIFBegin;
    }

    /**
     * End of stress intensity factor interval.
     * @return end of stress intensity factor interval
     */
    public double getStressIFEnd() {
        return stressIFEnd;
    }

    /**
     * End of stress intensity factor interval.
     * @param aStressIFEnd end of stress intensity factor interval
     */
    public void setStressIFEnd(final double aStressIFEnd) {
        stressIFEnd = aStressIFEnd;
    }

    /**
     * Step of stress intensity factor interval.
     * @return step of stress intensity factor interval
     */
    public double getStressIFStep() {
        return stressIFStep;
    }

    /**
     * Step of stress intensity factor interval.
     * @param aStressIFStep step of stress intensity factor interval
     */
    public void setStressIFStep(final double aStressIFStep) {
        stressIFStep = aStressIFStep;
    }

    /**
     * Count of parse exeptions.
     * @return count of parse exeptions
     */
    public int getParseExceptionCount() {
        return parseExceptionCount;
    }

    /**
     * Dependence for CGR.
     * @return dependence for CGR
     */
    public ArrayList<DataLine> getDataCGR() {
        return dataCGR;
    }

    /**
     * Dependence for SIF.
     * @return dependence for SIF
     */
    public ArrayList<DataLine> getDataSIF() {
        return dataSIF;
    }

    /**
     * Dependence for CCD.
     * @return dependence for CCD
     */
    public ArrayList<DataLine> getDataCCD() {
        return dataCCD;
    }
    
    /**
     * Convert value in second to value in year.
     * @param value value in second
     * @return value in year
     */
    public double convertSecondToYear(final double value) {
        return value / 31536e3;
    }
    
    /**
     * Convert value in year to value in second.
     * @param value value in year
     * @return value in second
     */
    public double convertYearToSecond(final double value) {
        return value * 31536e3;
    }
    
    /**
     * Convert value in meter to value in millimeter.
     * @param value value in meter
     * @return value in millimeter
     */
    public double convertMeterToMillimeter(final double value) {
        return value * 1e3;
    }
    
    /**
     * Convert value in millimeter to value in meter.
     * @param value value in millimeter
     * @return value in meter
     */
    public double convertMillimeterToMeter(final double value) {
        return value * 1e-3;
    }
}
