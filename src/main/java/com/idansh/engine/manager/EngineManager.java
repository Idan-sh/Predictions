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
import com.idansh.engine.helpers.Range;
import com.idansh.engine.manager.result.SimulationResult;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.rule.TerminationRule;
import com.idansh.engine.world.World;
import com.idansh.engine.jaxb.unmarshal.reader.Reader;

import java.io.File;
import java.util.*;

/**
 * The UI will handle the engine through this class.
 * Some methods will return a DTO that contains data from the simulation (without the logic of the engine elements).
 */
public class EngineManager {
    private World loadedWorld;  // The currently loaded world. This world will not run but only be used to create instances for running
    private final Map<Integer, World> runningSimulations;
    private final Map<Integer, SimulationResult> pastSimulations;

    public EngineManager() {
        loadedWorld = null;
        runningSimulations = new HashMap<>();
        pastSimulations = new HashMap<>();
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
                            rule.getActionsSet().size()
                    );

                    // Add actions' names to the DTO
                    rule.getActionsSet().forEach(
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
     * Adds a newly finished simulation to the past simulations' collection.
     * @param simulationResult a simulation result that contains information/details of the last simulation ran.
     */
    private void addSimulationResult(SimulationResult simulationResult){
        pastSimulations.put(simulationResult.getId(), simulationResult);
    }


    /**
     * Loads a simulation for XML file.
     * @param file XML file with world data.
     */
    public void loadSimulationFromFile(File file) {
            loadedWorld = Reader.readWorld(file);
    }


    /**
     * Start running the current simulation that was loaded.
     * Loads user received input for environment variables.
     * @param environmentVariablesListDTO contains data of environment variables to update in the simulation.
     * @return ID of the simulation which ended.
     */
    public int runSimulation(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        World runningWorldInstance = new World(loadedWorld);

        runningWorldInstance.entityManager.initEntityPopulation();
        updateEnvironmentVariablesFromInput(environmentVariablesListDTO);

        // Run the simulation
        SimulationResult simulationResult = runningWorldInstance.run(runningSimulations);

        // Save the simulation result
        addSimulationResult(simulationResult);

        return simulationResult.getId();
    }


    /**
     * Updates the values of received environment variables in the simulated world.
     * @param environmentVariablesListDTO DTO that contains data of multiple environment variables received from the user.
     */
    private void updateEnvironmentVariablesFromInput(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        environmentVariablesListDTO.getEnvironmentVariableInputDTOs().forEach(
                e -> {
                    PropertyFactory environmentVariable = loadedWorld.environmentVariablesManager.getEnvironmentVariable(e.getName());
                    environmentVariable.updateValue(e.getValue());
                }
        );
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
     * Get SimulationResultDTO list containing information on past simulations' results.
     */
    public List<SimulationResultDTO> getPastSimulationsResults() {
        List<SimulationResultDTO> retPastSimulations = new ArrayList<>();

        pastSimulations.forEach(
                (id, simulationResult) -> {
                    // Create simulation result DTO
                    SimulationResultDTO simulationResultDTO = new SimulationResultDTO(
                            simulationResult.getId(),
                            simulationResult.getSimulationTime(),
                            simulationResult.getCompletedTicks(),
                            simulationResult.getMaxTicks()
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

                                            if(range != null)
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
                    // Add simulation result DTO to the Past Simulations list
                    retPastSimulations.add(simulationResultDTO);
                }
        );

        return retPastSimulations;
    }


    /**
     * Get a list of all currently running simulations.
     * @return a DTO List of running simulations.
     */
    public List<RunningSimulationDTO> getRunningSimulations() {
        List<RunningSimulationDTO> runningSimulationsList = new ArrayList<>();

        runningSimulations.forEach(
                (id, world) -> {
                    RunningSimulationDTO runningSimulationDTO =
                            new RunningSimulationDTO(
                                    id,
                                    getEnvironmentVariablesListDTO(world),
                                    world.getSimulationTime(),
                                    world.getTickCount(),
                                    world.getTerminationRules().get(TerminationRule.Type.TICKS).getValue()
                                    );

                    // Add Entities:
                    world.entityManager.getEntityFactories().forEach(
                            (entityFactoryName, entityFactory) -> {
                                // Create entity DTO
                                EntityDTO entityDTO = new EntityDTO(
                                        entityFactoryName,
                                        entityFactory.getPopulationCount(),
                                        entityFactory.getPopulationCount());

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
                                        rule.getActionsSet().size()
                                );

                                // Add actions' names to the DTO
                                rule.getActionsSet().forEach(
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

                    runningSimulationsList.add(runningSimulationDTO);
                }
        );

        return runningSimulationsList;
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
     */
    public Map<Object, Integer> getPropertyValues(int simulationResultID, PropertyDTO propertyDTO) {
        Map<Object, Integer> retValuesMap = new LinkedHashMap<>();

        pastSimulations.get(simulationResultID).getEntityManager().getPopulation().forEach(
                entity -> {
                    Object entityValue = entity.getPropertyByName(propertyDTO.getName()).getValue();

                    if(retValuesMap.containsKey(entityValue)) {
                        int oldVal = retValuesMap.get(entityValue);
                        retValuesMap.put(entityValue, oldVal + 1);
                    }
                    else
                        retValuesMap.put(entityValue, 1);
                }
        );

        return retValuesMap;
    }


    /**
     * Sets an initial amount of instances in the population of the entity with the given name.
     */
    public void setEntityAmount(String entityName, int amount) {
        loadedWorld.entityManager.getEntityFactory(entityName).setInitPopulation(amount);
    }
}