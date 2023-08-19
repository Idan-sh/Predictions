package com.idansh.engine.manager;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.range.RangeDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.rule.TerminationRuleDTO;
import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.dto.simulation.SimulationEndTDO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.engine.helpers.Range;
import com.idansh.engine.manager.result.SimulationResult;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.rule.TerminationRule;
import com.idansh.engine.world.World;
import com.idansh.jaxb.unmarshal.reader.Reader;

import java.util.*;

/**
 * The UI will handle the engine through this class.
 * Some methods will return a DTO that contains data from the simulation (without the logic of the engine elements).
 */
public class EngineManager {
    private World currWorld;
    private final Map<Integer, SimulationResult> pastSimulations;

    public EngineManager() {
        currWorld = null;
        pastSimulations = new HashMap<>();
    }


    /**
     * @return returns to the UI a DTO that contains information on the current loaded simulated world.
     */
    public CurrentSimulationDTO getCurrentSimulationDetails() {
        CurrentSimulationDTO currentSimulationDTO = new CurrentSimulationDTO();

        // Add Entities:
        currWorld.entityManager.getEntityFactories().forEach(

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
                                        PropertyType.getTypeString(propertyFactory.getType()),
                                        rangeDTO,
                                        propertyFactory.isRandomGenerated(),
                                        null);
                                entityDTO.addPropertyDTOtoList(propertyDTO);
                            }
                    );
                    currentSimulationDTO.addEntityDTO(entityDTO);
                }
        );

        // Add Rules:
        currWorld.getRulesMap().forEach(
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

                    currentSimulationDTO.addRuleDTO(ruleDTO);
                }
        );

        // Add Termination Rules:
        currWorld.getTerminationRules().forEach(
                (terminationRuleType, terminationRule) -> {
                    TerminationRuleDTO terminationRuleDTO = new TerminationRuleDTO(
                            TerminationRule.Type.getTypeString(terminationRuleType),
                            terminationRule.getValue()
                    );

                    currentSimulationDTO.addTerminationRuleDTO(terminationRuleDTO);
                }
        );

        return currentSimulationDTO;
    }


    /**
     * Finds a simulation result from the past simulations and returns its information/details.
     * @param simulationId the ID of the simulation to get.
     * @return DTO containing its information/details.
     */
    public SimulationResultDTO getPastSimulationDetailsById(int simulationId) {
        SimulationResult simulationResult = pastSimulations.get(simulationId);
        return new SimulationResultDTO(simulationResult.getDateTime(), simulationResult.getDateTimeString(), simulationResult.getId());
    }


    /**
     * Adds a newly finished simulation to the past simulations collection.
     * @param simulationResult a simulation result that contains information/details of the last simulation ran.
     */
    private void addSimulationResult(SimulationResult simulationResult){
        pastSimulations.put(simulationResult.getId(), simulationResult);
    }


    /**
     * Loads a simulation for XML file.
     * @param path path to the XML file location in the machine.
     */
    public void loadSimulationFromFile(String path) {
        if (Reader.isValidPath(path)) {
            currWorld = Reader.readWorld(path);
        }
        else
            throw new IllegalArgumentException("path \"" + path + "\" is not a valid path!");
    }


    /**
     * Start running the current simulation that was loaded.
     * Loads user received input for environment variables.
     * @param environmentVariablesListDTO contains data of environment variables to update in the simulation.
     */
    public SimulationEndTDO runSimulation(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        updateEnvironmentVariablesFromInput(environmentVariablesListDTO);

        // Run the simulation
        SimulationResult simulationResult = currWorld.run();

        // Save the simulation result
        pastSimulations.put(simulationResult.getId(), simulationResult);

        return new SimulationEndTDO(simulationResult.getId(), simulationResult.getEndReason());
    }


    /**
     * Updates the values of received environment variables in the simulated world.
     * @param environmentVariablesListDTO DTO that contains data of multiple environment variables received from the user.
     */
    private void updateEnvironmentVariablesFromInput(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        environmentVariablesListDTO.getEnvironmentVariableInputDTOs().forEach(
                e -> {
                    PropertyFactory environmentVariable = currWorld.environmentVariablesManager.getEnvironmentVariable(e.getName());
                    environmentVariable.updateValue(e.getValue());
                }
        );
    }


    /**
     * Transfer all active environment variables from the engine to the UI.
     * @return EnvironmentVariablesSetDTO containing a list of all active environment variables,
     * with their names and values.
     */
    public EnvironmentVariablesListDTO getEnvironmentVariablesListDTO() {
        EnvironmentVariablesListDTO environmentVariablesListDTO = new EnvironmentVariablesListDTO();

        currWorld.getActiveEnvironmentVariables().getEnvironmentVariables().forEach(
                (name, envVar) -> {
                    Range range = envVar.getRange();
                    environmentVariablesListDTO.addEnvironmentVariableInput(
                            name, envVar.getValue(),
                            PropertyType.getTypeString(envVar.getType()),
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
                            simulationResult.getDateTime(),
                            simulationResult.getDateTimeString(),
                            simulationResult.getId());

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
                                                            PropertyType.getTypeString(propertyFactory.getType()),
                                                            rangeDTO,
                                                            propertyFactory.isRandomGenerated(),
                                                            propertyFactory.getValue())
                                            );
                                        }
                                );
                                simulationResultDTO.addEntityDTO(entityDTO);
                            }
                    );
                    // Add simulation result DTO to the return list
                    retPastSimulations.add(simulationResultDTO);
                }
        );

        return retPastSimulations;
    }


    /**
     * Checks whether of not a simulation is currently loaded into the program.
     * @return true if a simulation is loaded, false otherwise.
     */
    public boolean isSimulationLoaded() {
        return currWorld != null;
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
}