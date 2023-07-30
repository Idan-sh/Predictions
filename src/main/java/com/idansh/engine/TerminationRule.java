package com.idansh.engine;

/**
 * Determines a single term/rule that when met will end the simulation.
 */
public class TerminationRule {
    public enum Type {
        TICKS, SECONDS;
    }

    int value;
    Type type;

    public TerminationRule(int value, Type type) {
        this.value = value;
        this.type = type;
    }

}
