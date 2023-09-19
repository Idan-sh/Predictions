package com.idansh.engine.helpers;

/**
 * Calculates the consistency of a given value.
 * Each time a value was changed, add the number of ticks that passed since
 * the last change into the consistency object.
 * After finished adding, call for getConsistency() to get the result.
 */
public class Consistency {
    private Integer sum;
    private Integer nofItems;


    public Consistency() {
        sum = 0;
        nofItems = 0;
    }

    public void addItem(int value) {
        sum += value;
        nofItems++;
    }

    public float getConsistency() {
        if (nofItems == 0)
            return 0;

        return sum.floatValue() / nofItems.floatValue();
    }
}
