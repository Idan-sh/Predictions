package com.idansh.engine.objects;

import com.idansh.engine.rule.TerminationRule;

import java.util.List;
import java.util.Map;

/**
 * Main engine component that in charge of a single simulation,
 * and the various functions of the simulation.
 */
public class World {
    private Map <String, EntityFactory> entityFactoryMap;   // Each entity factory will define a type of entity with a unique name, that can be instantiated over multiple instances
    private List<Entity> population;                        // Population of entities, allows duplicates (e.g. multiple smokers entities)
    private Map<String, TerminationRule> terminationRules;

    // The static modifier may be removed if multiple simulations can co-exist (!!!)
    public static Map<String, EnvironmentVariable<?>> environmentVariables;    // Can be accessed from anywhere

    private int currTick = 0;                       // The current iteration of the simulation
}
