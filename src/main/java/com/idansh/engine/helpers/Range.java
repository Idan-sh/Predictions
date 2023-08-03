package com.idansh.engine.helpers;

/**
 * A numeric range of the type Float or Integer.
 * Has a bottom value, and a top value.
 * bottom value must be lower than the top value (or equal to).
 */
public class Range {
    private int bottom, top;

    public Range(int from, int to) {
        if(from > to)
            throw new RuntimeException("Range bottom value is higher than top value.");

        this.bottom = from;
        this.top = to;
    }

    public int getBottom() {
        return bottom;
    }

    public int getTop() {
        return top;
    }
}
