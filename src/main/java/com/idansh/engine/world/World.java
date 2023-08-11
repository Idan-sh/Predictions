package com.idansh.engine.world;

import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.environment.EnvironmentVariablesManager;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.rule.TerminationRule;

import java.util.HashMap;
import java.util.Map;

/**
 * Main engine component that in charge of a single simulation,
 * and the various functions of the simulation.
 */
public class World {
    private final Map<String, TerminationRule> terminationRules;            // Rules on when to end the simulation
    private final EnvironmentVariablesManager environmentVariablesManager;  // Contains all the environment variables factories
    private ActiveEnvironmentVariables activeEnvironmentVariables;          // Contains all the activated environment variables
    private final EntityManager entityManager;                              // Contains all the entities (population) of the simulation
    private int currTick;                                                   // The current iteration of the simulation


    /**
     * Initialize the simulated world.
     */
    public World() {
        this.terminationRules = new HashMap<>();
        this.environmentVariablesManager = new EnvironmentVariablesManager();
        this.activeEnvironmentVariables = null;
        this.entityManager = new EntityManager();
        this.currTick = 0;
    }


    /**
     * Adds a new environment variable factory to the simulated world,
     * which will be used to create an active environment variable.
     */
    public void addEnvironmentVariableFactory(PropertyFactory propertyFactory) {
        environmentVariablesManager.addEnvironmentVariableFactory(propertyFactory);
    }


    /**
     * Adds a new environment variable to the simulated world.
     */
    public void addTerminationRule(TerminationRule terminationRule) {
        if(terminationRules.containsKey(terminationRule.getName()))
            throw new IllegalArgumentException("Error: received terminationRule's name already exists!");

        terminationRules.put(terminationRule.getName(), terminationRule);
    }


    /**
     * Starts the simulation using the defined initial properties.
     */
    public void startSimulation() {
        activeEnvironmentVariables = environmentVariablesManager.createActiveEnvironmentVariables();    // Create the environment variables for the simulation to use

        // todo- continue with the simulation iterations...
    }
}

