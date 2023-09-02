package com.idansh.engine.world;

import com.idansh.engine.entity.EntityManager;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.environment.EnvironmentVariablesManager;
import com.idansh.engine.helpers.Countdown;
import com.idansh.engine.helpers.Counter;
import com.idansh.engine.helpers.SimulationIdGenerator;
import com.idansh.engine.helpers.SimulationTime;
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
    private final SimulationTime simulationTime;                                    // Holds the simulation's start and end times
    private int id;                                                                 // The ID of the simulation, will be assigned on world run


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
        this.simulationTime = new SimulationTime();
    }

    /**
     * Deep copies a world, setting it up from another run.
     * Only copy world that not previously ran.
     * @apiNote Does not copy the ID or the Active Environment Variables, creates them when the world is activated.
     */
    public World(World world) {
        // Check if the world ran, or if it's a newly created one
        if(world.getTickCount() != 0)
            throw new IllegalArgumentException("Cannot copy world that ran! Please only copy a world object that was only initialized...");

        this.entityManager = new EntityManager(world.entityManager);

        this.terminationRules = new HashMap<>();
        world.getTerminationRules().forEach(
                (type, terminationRule) -> this.terminationRules.put(type, new TerminationRule(type, terminationRule.getValue()))
        );

        this.rulesMap = new HashMap<>();
        world.getRulesMap().forEach(
                (name, rule) -> this.rulesMap.put(name, new Rule(rule))
        );

        this.environmentVariablesManager = new EnvironmentVariablesManager(world.environmentVariablesManager);
        this.tickCounter = new Counter(0);
        this.timer = new Timer();
        this.simulationTime = new SimulationTime();
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
            throw new IllegalArgumentException("received Rule's name - " + rule.getName() + " already exists!");

        rulesMap.put(rule.getName(), rule);
    }

    /**
     * Start running the simulated world.
     * @param runningSimulations Map of all currently running simulations. Will add the world when it runs to the map,
     *                           and will remove itself after the running process is finished.
     * @return The details on the simulation result.
     */
    public SimulationResult run(Map<Integer, World> runningSimulations) {
        // Add the running world to the current running simulations
        runningSimulations.put(this.getId(), this);

        // Timer countdown for the termination rule SECONDS
        Countdown countdown = new Countdown();
        id = SimulationIdGenerator.getID();

        // If a termination rule of SECONDS was set, starts a timer.
        if(terminationRules.containsKey(TerminationRule.Type.SECONDS))
            timer.schedule(countdown, terminationRules.get(TerminationRule.Type.SECONDS).getValue() * 1000L); // Get the amount of seconds and multiply by 1000 to get in milliseconds

        // Check if the current tick has reached the termination rule tick defined, if one does not exist keeps going until reached the timer defined
        while((!terminationRules.containsKey(TerminationRule.Type.TICKS)) || (terminationRules.containsKey(TerminationRule.Type.TICKS) && tickCounter.getCount() < terminationRules.get(TerminationRule.Type.TICKS).getValue())) {
            // Checks if the timer expired
            if(countdown.isFinished()) {
                // remove the world from the current running simulations
                runningSimulations.remove(this.getId());

                simulationTime.setEndTimes();
                return new SimulationResult(
                        id,
                        simulationTime,
                        "Timer Expired",
                        entityManager,
                        getTickCount(),
                        terminationRules.get(TerminationRule.Type.TICKS).getValue()
                );
            }

            // Run on every entity in the population and check if a rule can be invoked on it
            entityManager.getPopulation().forEach(
                    entity -> {
                        // Invoke all rules
                        rulesMap.forEach((ruleName, rule) -> rule.invoke(entity));
                    }
            );

            tickCounter.increaseCount();
        }

        // remove the world from the current running simulations
        runningSimulations.remove(this.getId());

        simulationTime.setEndTimes();
        return new SimulationResult(
                id,
                simulationTime,
                "Ticks Reached",
                entityManager,
                getTickCount(),
                terminationRules.get(TerminationRule.Type.TICKS).getValue()
        );
    }

    public Map<TerminationRule.Type, TerminationRule> getTerminationRules() {
        return terminationRules;
    }

    public Map<String, Rule> getRulesMap() {
        return rulesMap;
    }

    public int getId() {
        return id;
    }

    public int getTickCount() {
        return tickCounter.getCount();
    }

    public SimulationTime getSimulationTime() {
        return simulationTime;
    }
}

