package com.idansh.engine.world;

import com.idansh.engine.entity.Entity;
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
public class World implements Runnable {
    public final EntityManager entityManager;                                       // Contains all the entities (population) of the simulation
    private int id;                                                                 // The ID of the simulation, will be assigned on world run
    private final Map<TerminationRule.Type, TerminationRule> terminationRules;      // Rules on when to end the simulation
    private final Map<String, Rule> rulesMap;                                       // Rules that can activate during the simulation
    private ActiveEnvironmentVariables activeEnvironmentVariables;                  // Contains all the activated environment variables
    public final EnvironmentVariablesManager environmentVariablesManager;           // Contains all the environment variables factories
    private final Counter tickCounter;                                              // The current iteration of the simulation
    private final SimulationTime simulationTime;                                    // Holds the simulation's start and end times
    private SimulationResult simulationResult;
    private Integer threadCount;                                                    // The max number of threads that will be able to run simultaneously
    private boolean isRunning, isToStop, isToResume, isToPause;                     // Flags for the simulation process

    /**
     * Initialize the simulated world.
     */
    public World(int gridRows, int gridColumns) {
        this.terminationRules = new HashMap<>();
        this.rulesMap = new LinkedHashMap<>();
        this.environmentVariablesManager = new EnvironmentVariablesManager();
        this.activeEnvironmentVariables = null;
        this.entityManager = new EntityManager(gridRows, gridColumns);
        this.tickCounter = new Counter(0);
        this.simulationTime = new SimulationTime();
        this.simulationResult = null;
        this.threadCount = null;
        this.isRunning = this.isToPause = this.isToResume = this.isToStop = false;
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
                (name, rule) -> this.rulesMap.put(name, new Rule(rule, this))
        );

        this.environmentVariablesManager = new EnvironmentVariablesManager(world.environmentVariablesManager);
        this.activeEnvironmentVariables = null;
        this.tickCounter = new Counter(0);
        this.simulationTime = new SimulationTime();
        this.entityManager.initEntityPopulation();
        this.simulationResult = null;
        this.id = SimulationIdGenerator.getID();
        this.threadCount = world.threadCount;
        this.isRunning = this.isToPause = this.isToResume = this.isToStop = false;
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
    public void initEnvironmentVariables() {
        activeEnvironmentVariables = environmentVariablesManager.createActiveEnvironmentVariables();
    }


    /**
     * Adds a new Termination Rule to the simulated world.
     * There can only be one termination rule of each type - seconds or ticks.
     */
    public void addTerminationRule(TerminationRule terminationRule) {
        if(terminationRules.containsKey(terminationRule.getType()))
            throw new IllegalArgumentException("Received terminationRule's type - " + terminationRule.getType() + " already exists!");

        if(terminationRule.getType().equals(TerminationRule.Type.USER_DEFINED) && !terminationRules.isEmpty())
            throw new IllegalArgumentException("Cannot add user-defined termination rule with other types of termination rules!");

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
     * Saves the details of the simulation result.
     */
    @Override
    public void run() {
        this.isRunning = true;
        long timeToStop = -1;

        // Timer countdown for the termination rule SECONDS
        Countdown countdown = new Countdown();

        // If a termination rule of SECONDS was set, starts a timer.
        if (terminationRules.containsKey(TerminationRule.Type.SECONDS)) {
            timeToStop = terminationRules.get(TerminationRule.Type.SECONDS).getValue() * 1000L; // Get the amount of seconds and multiply by 1000 to get in milliseconds
        }

        // Check if the current tick has reached the termination rule tick defined, if one does not exist keeps going until reached the timer defined or the user decided to stop the simulation
        while ((!terminationRules.containsKey(TerminationRule.Type.TICKS)) || (terminationRules.containsKey(TerminationRule.Type.TICKS) && tickCounter.getCount() < terminationRules.get(TerminationRule.Type.TICKS).getValue())) {
            // Tell the entity manager the tick advanced
            entityManager.tickAdvance();

            // Checks if the timer exists and is expired, if so end simulation
            if (timeToStop != -1 && timeToStop < simulationTime.getElapsedTime()) {
                simulationResult = endSimulation("Timer Expired");
                return;
            }

            // Check if the simulation was ordered to stop, if so then stops without completing this tick
            if (isToStop) {
                simulationResult = endSimulation("Stopped By User");
                return;
            }

            // Check if the simulation was ordered to pause, if so then go into an infinite loop until ordered to resume
            if (isToPause) {
                try {
                    simulationTime.pauseElapsedTime();
                    // Check every 300 milliseconds if the simulation was ordered to resume running
                    do {
                        if (isToStop) {
                            simulationResult = endSimulation("Stopped By User");
                            return;
                        }

                        Thread.sleep(300);
                    } while (!isToResume);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                simulationTime.resumeElapsedTime();
            }

            // Increase the tick counter of each rule
            rulesMap.values().forEach(Rule::increaseTickCounter);

            // Try to invoke each rule of the simulation on each entity instance in the population
            for (Entity entity : entityManager.getPopulation()) {
                for (Rule rule : rulesMap.values()) {
                    rule.invoke(entity);
                }
            }

            // Try to reset the tick counter of each rule
            rulesMap.values().forEach(Rule::resetTickCounter);

            tickCounter.increaseCount();  // Increase the tick counter of the simulation
        }
        entityManager.checkPropertiesValueChange();         // Check if the properties' values have changed for each entity in the population
        entityManager.removeDeadEntitiesFromPopulation();   // Remove all previously killed entities from the population

        // Ticks reached, end simulation
        simulationResult = endSimulation("Ticks Reached");
    }


    /**
     * End a simulation with a String that defines why the simulation ended.
     * Only ends simulations on intended events, on errors the simulation will end according to the exception thrown.
     * @param endReason string that defines the reason of why the simulation ended, which Termination Rule was activated.
     * @return the simulation's result details.
     */
    private SimulationResult endSimulation(String endReason) {
        simulationTime.finish();
        return new SimulationResult(
                id,
                simulationTime,
                endReason,
                entityManager,
                getTickCount(),
                terminationRules.containsKey(TerminationRule.Type.TICKS) ? terminationRules.get(TerminationRule.Type.TICKS).getValue() : null
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

    public ActiveEnvironmentVariables getActiveEnvironmentVariables() {
        return activeEnvironmentVariables;
    }

    public SimulationResult getSimulationResult() {
        return simulationResult;
    }

    public boolean isSimulationFinished() {
        return simulationResult != null;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public int getThreadCount() {
        return threadCount;
    }


    /**
     * Stops the simulation at the next Simulation Tick.
     */
    public void stop() {
        if(!isRunning)
            return;

        isToStop = true;
        isRunning = false;
    }


    /**
     * Pauses the simulation at the next Simulation Tick.
     */
    public void pause() {
        if(!isRunning)
            return;

        isToPause = true;
        isToResume = false;
    }


    /**
     * Resumes the simulation from where it was last paused.
     * @throws RuntimeException in case the simulation wasn't paused before the resume call.
     */
    public void resume() {
        if(!isToPause || !isRunning)
            return;

        isToResume = true;
        isToPause = false;
    }
}

