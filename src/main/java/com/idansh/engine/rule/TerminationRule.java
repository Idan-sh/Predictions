package com.idansh.engine.rule;

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
    Type type;  // the type of the termination rule
    int value;  // nof ticks/seconds

    public TerminationRule(Type type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Type getType() {
        return type;
    }
}
