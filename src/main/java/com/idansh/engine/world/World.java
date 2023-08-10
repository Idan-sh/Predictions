package com.idansh.engine.world;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.environment.EnvironmentVariablesManager;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.rule.TerminationRule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main engine component that in charge of a single simulation,
 * and the various functions of the simulation.
 */
public class World {
    private final Map<String, TerminationRule> terminationRules;  // Rules on when to end the simulation
    private final EnvironmentVariablesManager environmentVariablesManager;    // Contains all the environment variables of the simulation
    private final EntityManager entityManager;
    private int currTick;                     // The current iteration of the simulation


    /**
     * Initialize the simulated world.
     */
    public World() {
        this.terminationRules = new HashMap<>();
        this.environmentVariablesManager = new EnvironmentVariablesManager();
        this.entityManager = new EntityManager();
        this.currTick = 0;
    }



    /**
     * Adds a new environment variable to the simulated world.
     */
    public void addEnvironmentVariable(Property property) {
        environmentVariablesManager.addEnvironmentVariable(property);
    }


    /**
     * Adds a new environment variable to the simulated world.
     */
    public void addTerminationRule(TerminationRule terminationRule) {
        if(environmentVariables.containsKey(terminationRule.getName()))
            throw new IllegalArgumentException("Error: received terminationRule's name already exists!");

        terminationRules.put(terminationRule.getName(), terminationRule);
    }


}

