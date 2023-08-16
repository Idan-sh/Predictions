package com.idansh.engine.world;

import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.environment.EnvironmentVariablesManager;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.rule.Rule;
import com.idansh.engine.rule.TerminationRule;

import java.util.HashMap;
import java.util.Map;

/**
 * Main engine component that in charge of a single simulation,
 * and the various functions of the simulation.
 */
public class World {
    private final Map<TerminationRule.Type, TerminationRule> terminationRulesMap;  // Rules on when to end the simulation
    private final Map<String, Rule> rulesMap;                                    // Rules that can activate during the simulation
    private final EnvironmentVariablesManager environmentVariablesManager;      // Contains all the environment variables factories
    private ActiveEnvironmentVariables activeEnvironmentVariables;              // Contains all the activated environment variables
    public final EntityManager entityManager;                                   // Contains all the entities (population) of the simulation
    private int currTick;                                                       // The current iteration of the simulation


    /**
     * Initialize the simulated world.
     */
    public World() {
        this.terminationRulesMap = new HashMap<>();
        this.rulesMap = new HashMap<>();
        this.environmentVariablesManager = new EnvironmentVariablesManager();
        this.activeEnvironmentVariables = null;
        this.entityManager = new EntityManager();
        this.currTick = 0;
    }

    public ActiveEnvironmentVariables getActiveEnvironmentVariables() {
        return activeEnvironmentVariables;
    }

    /**
     * Adds a new environment variable factory to the simulated world,
     * which will be used to create an active environment variable.
     */
    public void addEnvironmentVariableFactory(PropertyFactory propertyFactory) {
        environmentVariablesManager.addEnvironmentVariableFactory(propertyFactory);
    }


    /**
     * Creates the environment variables for the simulation to use,
     * according to the environment variable factories defined.
     */
    public void InitEnvironmentVariables() {
        activeEnvironmentVariables = environmentVariablesManager.createActiveEnvironmentVariables();
    }


    /**
     * Adds a new Termination Rule to the simulated world.
     * There can only be one termination rule of each type - seconds or ticks.
     */
    public void addTerminationRule(TerminationRule terminationRule) {
        if(terminationRulesMap.containsKey(terminationRule.getType()))
            throw new IllegalArgumentException("Error: received terminationRule's type - " + terminationRule.getType() + " already exists!");

        terminationRulesMap.put(terminationRule.getType(), terminationRule);
    }


    /**
     * Adds a new Rule to the simulated world.
     * Rules have unique names, if the rule's name already exist, throws IllegalArgumentException.
     */
    public void addRule(Rule rule) {
        if(rulesMap.containsKey(rule.getName()))
            throw new IllegalArgumentException("Error: received Rule's name - " + rule.getName() + " already exists!");

        rulesMap.put(rule.getName(), rule);
    }
}

