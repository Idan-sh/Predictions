package com.idansh.engine.world;

import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.environment.EnvironmentVariablesManager;
import com.idansh.engine.helpers.Countdown;
import com.idansh.engine.helpers.Counter;
import com.idansh.engine.manager.result.SimulationResult;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.rule.Rule;
import com.idansh.engine.rule.TerminationRule;

import java.util.*;

/**
 * Main engine component that in charge of a single simulation,
 * and the various functions of the simulation.
 */
public class World {
    public final EntityManager entityManager;                                       // Contains all the entities (population) of the simulation
    private final Map<TerminationRule.Type, TerminationRule> terminationRules;      // Rules on when to end the simulation
    private final Map<String, Rule> rulesMap;                                       // Rules that can activate during the simulation
    private ActiveEnvironmentVariables activeEnvironmentVariables;                  // Contains all the activated environment variables
    public final EnvironmentVariablesManager environmentVariablesManager;           // Contains all the environment variables factories
    private final Counter tickCounter;                                              // The current iteration of the simulation
    private final Timer timer;                                                      // Timer for the termination rule SECONDS


    /**
     * Initialize the simulated world.
     */
    public World() {
        this.terminationRules = new HashMap<>();
        this.rulesMap = new LinkedHashMap<>();
        this.environmentVariablesManager = new EnvironmentVariablesManager();
        this.activeEnvironmentVariables = null;
        this.entityManager = new EntityManager();
        this.tickCounter = new Counter(0);
        this.timer = new Timer();
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
        if(terminationRules.containsKey(terminationRule.getType()))
            throw new IllegalArgumentException("received terminationRule's type - " + terminationRule.getType() + " already exists!");

        terminationRules.put(terminationRule.getType(), terminationRule);
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

    public SimulationResult run() {
        // Timer countdown for the termination rule SECONDS
        Countdown countdown = new Countdown();

        // If a termination rule of SECONDS was set, starts a timer.
        if(terminationRules.containsKey(TerminationRule.Type.SECONDS))
            timer.schedule(countdown, terminationRules.get(TerminationRule.Type.SECONDS).getValue() * 1000L); // Get the amount of seconds and multiply by 1000 to get in milliseconds

        // Check if the current tick has reached the termination rule tick defined, if one does not exist keeps going until reached the timer defined
        while((!terminationRules.containsKey(TerminationRule.Type.TICKS)) || (terminationRules.containsKey(TerminationRule.Type.TICKS) && tickCounter.getCount() < terminationRules.get(TerminationRule.Type.TICKS).getValue())) {
            tickCounter.increaseCount();

            // Checks if the timer expired
            if(countdown.isFinished()) {
                return new SimulationResult("Timer Expired", entityManager);
            }

            // Invoke all rules
            rulesMap.forEach((ruleName, rule) -> rule.invoke());
        }

        return new SimulationResult("Ticks Reached", entityManager);
    }

    public Map<TerminationRule.Type, TerminationRule> getTerminationRules() {
        return terminationRules;
    }

    public Map<String, Rule> getRulesMap() {
        return rulesMap;
    }
}

