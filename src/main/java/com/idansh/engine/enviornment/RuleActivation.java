package com.idansh.engine.enviornment;

/**
 * Controls when a rule will be activated
 */
public class RuleActivation {
    private int ticks;          // In every how many clock ticks will the rule try to activate
    private double probability = 1; // Decimal number between 0 (never happens) and 1 (always happens)

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
