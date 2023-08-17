package com.idansh.engine.rule;

/**
 * Controls when a rule will be activated
 */
public class RuleActivation {
    private final int ticks;          // In every how many clock ticks will the rule try to activate
    private final double probability; // number between 0 (never happens) and 1 (always happens)


    /**
     * Adds default values to the rule activation.
     */
    public RuleActivation() {
        this.ticks = 1;
        this.probability = 1;
    }

    /**
     * Sets custom values to the rule activation.
     */
    public RuleActivation(int ticks, double probability) {
        this.ticks = ticks;
        this.probability = probability;
    }

    /**
     * Sets custom values to the rule activation ticks, and default value for the probability.
     */
    public RuleActivation(int ticks) {
        this.ticks = ticks;
        this.probability = 1;
    }

    /**
     * Sets custom values to the rule activation probability, and default value for the ticks.
     */
    public RuleActivation(double probability) {
        this.probability = probability;
        this.ticks = 1;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }
}
