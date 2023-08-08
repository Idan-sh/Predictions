package com.idansh.engine.world;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.EntityFactory;
import com.idansh.engine.environment.variable.EnvironmentVariable;
import com.idansh.engine.rule.TerminationRule;

import java.util.List;
import java.util.Map;

/**
 * Main engine component that in charge of a single simulation,
 * and the various functions of the simulation.
 */
public class World {
    private Map <String, EntityFactory> entityFactoryMap;   // Each entity factory will define instructions on how to instantiate a single entity with a unique name
    private List<Entity> population;                        // Population of all entities that exist in the simulated world, allows duplicates (e.g. multiple smokers entities)
    private Map<String, TerminationRule> terminationRules;  // Rules on when to end the simulation

    // The static modifier may be removed if multiple simulations can co-exist (!!!)
    public static Map<String, EnvironmentVariable<?>> environmentVariables;    // Can be accessed from anywhere

    private int currTick = 0;                       // The current iteration of the simulation
}
