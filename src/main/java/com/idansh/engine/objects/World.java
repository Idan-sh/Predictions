package com.idansh.engine.objects;

import com.idansh.engine.rule.TerminationRule;

import java.util.List;

/**
 * Main engine component that in charge of a single simulation,
 * and the various functions of the simulation.
 */
public class World {
    private List<Entity> population;
    private List<EnvironmentVariable<?>> environmentVariables;
    private List<TerminationRule> terminationRules;
    private int currTick = 0;
}
