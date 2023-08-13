package com.idansh.engine.helpers;

/**
 * A numeric range of the type Float or Integer.
 * Has a bottom value, and a top value.
 * bottom value must be lower than the top value (or equal to).
 */
public class Range {
    private double bottom, top;

    public Range(double from, double to) {
        if(from > to)
            throw new RuntimeException("Range bottom value is higher than top value.");

        this.bottom = from;
        this.top = to;
    }

    public double getBottom() {
        return bottom;
    }

    public double getTop() {
        return top;
    }
}
