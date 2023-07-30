package com.idansh.engine;

/**
 * A numeric range of the type Float or Integer.
 * Has a bottom value, and a top value.
 * bottom value must be lower than the top value (or equal to).
 */
public class Range {
    Float bottom, top;

    public Range(Float bottom, Float top) {
        if(bottom > top)
            throw new RuntimeException("Range bottom value is higher than top value.");

        this.bottom = bottom;
        this.top = top;
    }
}
