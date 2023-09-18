package com.idansh.engine.manager;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.range.RangeDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.rule.TerminationRuleDTO;
import com.idansh.dto.simulation.LoadedSimulationDTO;
import com.idansh.dto.simulation.RunningSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.helpers.Range;
import com.idansh.engine.manager.result.SimulationResult;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.rule.TerminationRule;
import com.idansh.engine.world.World;
import com.idansh.engine.jaxb.unmarshal.reader.Reader;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The UI will handle the engine through this class.
 * Some methods will return a DTO that contains data from the simulation (without the logic of the engine elements).
 */
public class EngineManager {
    private World loadedWorld;                              // The currently loaded world. This world will not run but only be used to create instances for running
    private ExecutorService threadPool;               // Thread management for simulation runs
    private final Map<Integer, World> simulationsPool;      // Simulated worlds map: currently running simulations and finished simulations. Key = ID of the simulation, Value = simulated world

    public EngineManager() {
        loadedWorld = null;
        simulationsPool = new HashMap<>();
        threadPool = null;
    }

    /**
     * Create and add a new Thread Pool into the engine.
     * @param threadCount max number of threads that can run simultaneously.
     */
    private void createThreadPool(int threadCount) {
        threadPool = Executors.newFixedThreadPool(threadCount);
    }

    /**
     * @return returns to the UI a DTO that contains information on the current loaded simulated world.
     */
    public LoadedSimulationDTO getLoadedSimulationDetails() {
        LoadedSimulationDTO loadedSimulationDTO =
                new LoadedSimulationDTO(getEnvironmentVariablesListDTO(loadedWorld));

        // Add Entities:
        loadedWorld.entityManager.getEntityFactories().forEach(
                (entityFactoryName, entityFactory) -> {
                    // Create entity DTO
                    EntityDTO entityDTO = new EntityDTO(
                            entityFactoryName,
                            -1,
                            -1
                    );

                    // Create properties for the entity DTO
                    entityFactory.getPropertiesToAssign().forEach(
                            (propertyFactoryName, propertyFactory) -> {
                                Range range = propertyFactory.getRange();
                                RangeDTO rangeDTO;

                                // Check if range exists, if so then create DTO from it
                                if(range != null)
                                    rangeDTO = new RangeDTO(range.getBottom(), range.getTop());
                                else
                                    rangeDTO = null;

                                PropertyDTO propertyDTO = new PropertyDTO(
                                        propertyFactoryName,
                                        propertyFactory.getType().getTypeString(),
                                        rangeDTO,
                                        propertyFactory.isRandomGenerated(),
                                        propertyFactory.isRandomGenerated() ? null : propertyFactory.createProperty().getValue()    // If the value is not random, then get the fixed initial value
                                        );
                                entityDTO.addPropertyDTOtoList(propertyDTO);
                            }
                    );
                    loadedSimulationDTO.addEntityDTO(entityDTO);
                }
        );

        // Add Rules:
        loadedWorld.getRulesMap().forEach(
                (ruleName, rule) -> {
                    // Create rule DTO
                    RuleDTO ruleDTO = new RuleDTO(
                            ruleName,
                            rule.getActivation().getTicks(),
                            rule.getActivation().getProbability(),
                            rule.getActionsList().size()
                    );

                    // Add actions' names to the DTO
                    rule.getActionsList().forEach(
                            a -> ruleDTO.addActionName(a.getActionTypeString())
                    );

                    loadedSimulationDTO.addRuleDTO(ruleDTO);
                }
        );

        // Add Termination Rules:
        loadedWorld.getTerminationRules().forEach(
                (terminationRuleType, terminationRule) -> {
                    TerminationRuleDTO terminationRuleDTO = new TerminationRuleDTO(
                            TerminationRule.Type.getTypeString(terminationRuleType),
                            terminationRule.getValue()
                    );

                    loadedSimulationDTO.addTerminationRuleDTO(terminationRuleDTO);
                }
        );

        return loadedSimulationDTO;
    }


    /**
     * Loads a simulation for XML file.
     * @param file XML file with world data.
     */
    public void loadSimulationFromFile(File file) {
            loadedWorld = Reader.readWorld(file);

            // If one does not already exist, create a thread pool with the received thread count
            if(threadPool == null) {
                createThreadPool(loadedWorld.getThreadCount());
            }
    }


    /**
     * Create a runnable World object and add it to the thread pool,
     * load user received input of environment variables into the runnable World,
     * and execute the thread.
     * This thread will run sometime in the future, according to the JVM thread pool choice.
     * @param environmentVariablesListDTO contains data of environment variables to update in the simulation.
     * @return ID of the simulation created.
     */
    public int createAndPutSimulation(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        World runnableWorld = new World(loadedWorld);

        updateEnvironmentVariablesFromInput(runnableWorld, environmentVariablesListDTO);

        // Add the simulation world to the simulations pool
        simulationsPool.put(runnableWorld.getId(), runnableWorld);

        // Run the simulation thread at some time in the future
        threadPool.execute(runnableWorld);

        return runnableWorld.getId();
    }


    /**
     * Updates the values of received environment variables of a simulated world.
     * @param runningWorldInstance a World object that is initialized for a simulation run.
     * @param environmentVariablesListDTO DTO that contains data of multiple environment variables received from the user.
     */
    private void updateEnvironmentVariablesFromInput(World runningWorldInstance, EnvironmentVariablesListDTO environmentVariablesListDTO) {
        environmentVariablesListDTO.getEnvironmentVariableInputDTOs().forEach(
                e -> {
                    PropertyFactory environmentVariable = runningWorldInstance.environmentVariablesManager.getEnvironmentVariable(e.getName());
                    environmentVariable.updateValue(e.getValue());
                }
        );

        runningWorldInstance.initEnvironmentVariables();
    }


    /**
     * Transfer all active environment variables from the engine to the UI.
     * @return EnvironmentVariablesSetDTO containing a list of all active environment variables,
     * with their names and values.
     */
    public EnvironmentVariablesListDTO getEnvironmentVariablesListDTO(World world) {
        EnvironmentVariablesListDTO environmentVariablesListDTO = new EnvironmentVariablesListDTO();

        world.getActiveEnvironmentVariables().getEnvironmentVariables().forEach(
                (name, envVar) -> {
                    Range range = envVar.getRange();
                    environmentVariablesListDTO.addEnvironmentVariableInput(
                            name, envVar.getValue(),
                            envVar.getType().getTypeString(),
                            range == null ? null : new RangeDTO(range.getBottom(), range.getTop()));
                }
        );
        return environmentVariablesListDTO;
    }


    /**
     * Get a simulation execution, past and present.
     * Past simulations will be in the form of SimulationResultDTO,
     * while currently running simulations will be in the form of RunningSimulationDTO.
     * @param chosenExecutionID ID of the simulation execution to get.
     * @return a DTO in an Object form, that contains information on a currently running simulation execution,
     * or a finished simulation execution.
     */
    public Object getSimulationExecutionDTO(int chosenExecutionID) {
        Object retExecution;

        World world = simulationsPool.get(chosenExecutionID);

        if(world == null) {
            throw new IllegalArgumentException("cannot find simulation execution with ID " + chosenExecutionID + " in the running/finished simulation executions pool.");
        }

        if (world.isSimulationFinished()) {
            retExecution = getSimulationResultDTO(world);
        } else {
            retExecution = getRunningSimulationDTO(world);
        }

        return retExecution;
    }


    /**
     * Given a finished simulation world, get the result as a DTO object.
     * @param world instance of a simulated world that has finished working.
     * @return DTO that contains information about the simulation and its result.
     */
    private SimulationResultDTO getSimulationResultDTO(World world) {
        SimulationResult simulationResult = world.getSimulationResult();

        // Create simulation result DTO
        SimulationResultDTO simulationResultDTO = new SimulationResultDTO(
                simulationResult.getId(),
                simulationResult.getSimulationTime(),
                simulationResult.getCompletedTicks(),
                simulationResult.getMaxTicks(),
                simulationResult.getEndReason()
        );

        simulationResult.getEntityManager().getEntityFactories().forEach(
                (entityName, entityFactory) -> {
                    EntityDTO entityDTO = new EntityDTO(
                            entityName,
                            entityFactory.getPopulationCount(),
                            entityFactory.getInitPopulation());

                    // Add properties DTOs to the entity DTO
                    entityFactory.getPropertiesToAssign().forEach(
                            (propertyFactoryName, propertyFactory) -> {
                                Range range = propertyFactory.getRange();
                                RangeDTO rangeDTO = null;

                                if (range != null)
                                    rangeDTO = new RangeDTO(range.getBottom(), range.getTop());

                                entityDTO.addPropertyDTOtoList(
                                        new PropertyDTO(
                                                propertyFactoryName,
                                                propertyFactory.getType().getTypeString(),
                                                rangeDTO,
                                                propertyFactory.isRandomGenerated(),
                                                propertyFactory.getValue())
                                );
                            }
                    );
                    // Add the created Entity DTO to the Simulation Result DTO
                    simulationResultDTO.addEntityDTO(entityDTO);
                }
        );

        return simulationResultDTO;
    }


    /**
     * Get a list of all currently running simulations.
     * @return a DTO List of running simulations.
     */
    private RunningSimulationDTO getRunningSimulationDTO(World world) {
        Integer maxTicks = null;
        if(world.getTerminationRules().containsKey(TerminationRule.Type.TICKS))
            maxTicks = world.getTerminationRules().get(TerminationRule.Type.TICKS).getValue();

        RunningSimulationDTO runningSimulationDTO =
                new RunningSimulationDTO(
                        world.getId(),
                        getEnvironmentVariablesListDTO(world),
                        world.getSimulationTime(),
                        world.getTickCount(),
                        maxTicks
                );

        // Add Entities:
        world.entityManager.getEntityFactories().forEach(
                (entityFactoryName, entityFactory) -> {
                    // Create entity DTO
                    EntityDTO entityDTO = new EntityDTO(
                            entityFactoryName,
                            entityFactory.getPopulationCount(),
                            entityFactory.getInitPopulation());

                    // Create properties for the entity DTO
                    entityFactory.getPropertiesToAssign().forEach(
                            (propertyFactoryName, propertyFactory) -> {
                                Range range = propertyFactory.getRange();
                                RangeDTO rangeDTO;

                                // Check if range exists, if so then create DTO from it
                                if (range != null)
                                    rangeDTO = new RangeDTO(range.getBottom(), range.getTop());
                                else
                                    rangeDTO = null;

                                PropertyDTO propertyDTO = new PropertyDTO(
                                        propertyFactoryName,
                                        propertyFactory.getType().getTypeString(),
                                        rangeDTO,
                                        propertyFactory.isRandomGenerated(),
                                        propertyFactory.isRandomGenerated() ? null : propertyFactory.createProperty().getValue()    // If the value is not random, then get the fixed initial value
                                );
                                entityDTO.addPropertyDTOtoList(propertyDTO);
                            }
                    );
                    runningSimulationDTO.addEntityDTO(entityDTO);
                }
        );

        // Add Rules:
        world.getRulesMap().forEach(
                (ruleName, rule) -> {
                    // Create rule DTO
                    RuleDTO ruleDTO = new RuleDTO(
                            ruleName,
                            rule.getActivation().getTicks(),
                            rule.getActivation().getProbability(),
                            rule.getActionsList().size()
                    );

                    // Add actions' names to the DTO
                    rule.getActionsList().forEach(
                            a -> ruleDTO.addActionName(a.getActionTypeString())
                    );

                    runningSimulationDTO.addRuleDTO(ruleDTO);
                }
        );

        // Add Termination Rules:
        world.getTerminationRules().forEach(
                (terminationRuleType, terminationRule) -> {
                    TerminationRuleDTO terminationRuleDTO = new TerminationRuleDTO(
                            TerminationRule.Type.getTypeString(terminationRuleType),
                            terminationRule.getValue()
                    );

                    runningSimulationDTO.addTerminationRuleDTO(terminationRuleDTO);
                }
        );

        return runningSimulationDTO;
    }


    /**
     * Checks whether of not a simulation is currently loaded into the program.
     * @return true if a simulation is loaded, false otherwise.
     */
    public boolean isSimulationLoaded() {
        return loadedWorld != null;
    }


    /**
     * Gets a map of:
     * 1. key: the property's value
     * 2. value: the amount of entities in the population with this value
     * @param entityName Name of the entity in which the property is defined.
     * @param propertyName Name of the property to get its values.
     * @param simulationResultID ID of the simulation result that contains the info.
     */
    public Map<Object, Integer> getPropertyValues(int simulationResultID, String entityName, String propertyName) {
        Map<Object, Integer> retValuesMap = new LinkedHashMap<>();

        for (Entity entity : simulationsPool.get(simulationResultID).entityManager.getPopulation()) {
            if (entity.getName().equals(entityName)) {
                Object entityValue = entity.getPropertyByName(propertyName).getValue();

                if (retValuesMap.containsKey(entityValue)) {
                    int oldVal = retValuesMap.get(entityValue);
                    retValuesMap.put(entityValue, oldVal + 1);
                } else
                    retValuesMap.put(entityValue, 1);
            }
        }

        return retValuesMap;
    }


    /**
     * Sets an initial amount of instances in the population of the entity with the given name.
     */
    public void setEntityAmount(String entityName, int amount) {
        loadedWorld.entityManager.getEntityFactory(entityName).setInitPopulation(amount);
    }
}