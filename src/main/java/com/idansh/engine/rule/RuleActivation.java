package com.idansh.engine.rule;

import com.idansh.engine.helpers.RandomValue;

/**
 * Controls when a rule will be activated
 */
public class RuleActivation {
    private final int ticks;          // In every how many clock ticks will the rule try to activate
    private final double probability; // number between 0 (never happens) and 1 (always happens)
    private double generatedProbability;


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


    public RuleActivation(RuleActivation ruleActivation) {
        this.ticks = ruleActivation.getTicks();
        this.probability = ruleActivation.getProbability();
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }

    /**
     * Generate a random probability between 0 and 1 (included).
     */
    public void generateProbability() {
        generatedProbability = RandomValue.getRandomDouble();
    }

    /**
     * @return true if the probability of the activation is larger or equal to the generated probability,
     * false otherwise.
     */
    public boolean isProbabilityActivated() {
        return probability >= generatedProbability;
    }
}
