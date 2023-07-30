package com.idansh.engine;

/**
 * Determines a single term/rule that when met will end the simulation.
 */
public class TerminationRule {
    /**
     * Sets the type of termination rule to be:
     * 0. TICKS -       maximum clock tick to reach
     * 1. SECONDS -     number of seconds for a timer to be set by
     */
    public enum Type {
        TICKS, SECONDS;
    }

    int value;  // nof ticks/seconds
    Type type;  // the type of the termination rule

    public TerminationRule(int value, Type type) {
        this.value = value;
        this.type = type;
    }

}
