package com.idansh.engine.world;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.environment.variable.EnvironmentVariable;
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
    private final Map <String, EntityFactory> entityFactories;   // Each entity factory will define instructions on how to instantiate a single entity with a unique name
    private final List<Entity> population;                        // Population of all entities that exist in the simulated world, allows duplicates (e.g. multiple smokers entities)
    private final Map<String, TerminationRule> terminationRules;  // Rules on when to end the simulation

    // The static modifier may be removed if multiple simulations can co-exist (!!!)
    public static Map<String, EnvironmentVariable<?>> environmentVariables;    // Can be accessed from anywhere
    private int currTick;                     // The current iteration of the simulation

    static {
        environmentVariables = new HashMap<>();
    }

    /**
     * Initialize the simulated world.
     */
    public World() {
        this.entityFactories = new HashMap<>();
        this.population = new ArrayList<>();
        this.terminationRules = new HashMap<>();
        this.currTick = 0;
    }

    /**
     * Adds a new EntityFactory to the simulated world, from which entities will be created.
     */
    public void addEntityFactory(EntityFactory entityFactory) {
        if(entityFactories.containsKey(entityFactory.getName()))
            throw new IllegalArgumentException("Error: received entityFactory's name already exists!");

        entityFactories.put(entityFactory.getName(), entityFactory);
    }


    /**
     * Initializes the population of the world with entities using the entity factories.
     */
    public void initEntityPopulation() {
        // Run through all teh entity factories, on each factory create populationCount instances.
        entityFactories.forEach(
                (name , factory) -> {
                    for (int i = 0; i < factory.getPopulationCount(); i++) {
                        population.add(factory.createEntity());
                    }
                }
        );
    }


    /**
     * Adds a new environment variable to the simulated world.
     */
    public void addEnvironmentVariable(EnvironmentVariable<?> environmentVariable) {
        if(environmentVariables.containsKey(environmentVariable.getName()))
            throw new IllegalArgumentException("Error: received environmentVariable's name already exists!");

        environmentVariables.put(environmentVariable.getName(), environmentVariable);
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
