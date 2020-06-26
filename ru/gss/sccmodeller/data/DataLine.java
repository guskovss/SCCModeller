/*
 * Stress Corrosion Cracking Modeller
 */
package ru.gss.sccmodeller.data;

/**
 * Point.
 * @version 1.1.0 19.03.2020
 * @author Sergey Guskov
 */
public class DataLine {

    /**
     * Value x.
     */
    private Double x;
    /**
     * Value y.
     */
    private Double y;
    
    /**
     * Constructor.
     */
    public DataLine() {
    }

    /**
     * Constructor.
     * @param aX value x
     * @param aY value y
     */
    public DataLine(final Double aX, final Double aY) {
        x = aX;
        y = aY;
    }
    
    /**
     * Value x.
     * @return value x
     */
    public Double getX() {
        return x;
    }

    /**
     * Value x.
     * @param aX value x
     */
    public void setX(final Double aX) {
        x = aX;
    }

    /**
     * Value y.
     * @return value y
     */
    public Double getY() {
        return y;
    }

    /**
     * Value y.
     * @param aY value y
     */
    public void setY(final Double aY) {
        y = aY;
    }
}
