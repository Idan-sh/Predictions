package com.idansh.engine.objects;

import com.idansh.engine.rule.TerminationRule;

import java.util.Map;

/**
 * Main engine component that in charge of a single simulation,
 * and the various functions of the simulation.
 */
public class World {
    private Map<String, Entity> population;         // Population of entities, each got a unique name
    private Map<String, EnvironmentVariable<?>> environmentVariables;
    private Map<String, TerminationRule> terminationRules;
    private int currTick = 0;                       // The current iteration of the simulation
}
