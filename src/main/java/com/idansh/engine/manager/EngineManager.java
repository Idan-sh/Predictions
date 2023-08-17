package com.idansh.engine.manager;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariablesSetDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.range.RangeDTO;
import com.idansh.dto.rule.RuleDTO;
import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.engine.manager.result.SimulationIdGenerator;
import com.idansh.engine.manager.result.SimulationResult;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.property.instance.PropertyType;
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
                                PropertyDTO propertyDTO = new PropertyDTO(
                                        propertyFactoryName,
                                        PropertyType.getTypeString(propertyFactory.getType()),
                                        new RangeDTO(
                                                propertyFactory.getRange().getBottom(),
                                                propertyFactory.getRange().getTop()),
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
                }
        );

        // Add Termination Rules:
        currWorld.getTerminationRulesMap().forEach(
                (terminationRuleName, terminationRule) -> {

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
        if (Reader.isValidPath(path))
            currWorld = Reader.readWorld(path);
        else
            throw new IllegalArgumentException("path \"" + path + "\" is not a valid path!");
    }


    /**
     * Start running the current simulation that was loaded.
     * Loads user received input for environment variables.
     * @param environmentVariablesSetDTO contains data of environment variables to update in the simulation.
     */
    public SimulationResultDTO runSimulation(EnvironmentVariablesSetDTO environmentVariablesSetDTO) {
        updateEnvironmentVariablesFromInput(environmentVariablesSetDTO);

        // Run the simulation
        SimulationResult simulationResult = currWorld.run();

        // Save the simulation result
        pastSimulations.put(simulationResult.getId(), simulationResult);

        // TODO : return to the UI the simulation result. digest simulationResult...
        return new SimulationResultDTO(simulationResult.getDateTime(), simulationResult.getDateTimeString(), simulationResult.getId());
    }


    /**
     * Updates the values of received environment variables in the simulated world.
     * @param environmentVariablesSetDTO DTO that contains data of multiple environment variables received from the user.
     */
    private void updateEnvironmentVariablesFromInput(EnvironmentVariablesSetDTO environmentVariablesSetDTO) {
        environmentVariablesSetDTO.getEnvironmentVariableInputDTOs().forEach(
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
    public EnvironmentVariablesSetDTO getEnvironmentVariablesSetDTO() {
        EnvironmentVariablesSetDTO environmentVariablesSetDTO = new EnvironmentVariablesSetDTO();

        currWorld.getActiveEnvironmentVariables().getEnvironmentVariables().forEach(
                (key, value) -> environmentVariablesSetDTO.addEnvironmentVariableInput(key, value.getValue())
        );
        return environmentVariablesSetDTO;
    }


    /**
     * Get SimulationResultDTOs containing information on past simulations' results.
     */
    public List<SimulationResultDTO> getPastSimulationsResults() {
        List<SimulationResultDTO> retPastSimulations = new ArrayList<>();

        pastSimulations.forEach(
                (id, simulationResult) -> retPastSimulations.add(new SimulationResultDTO(simulationResult.getDateTime(), simulationResult.getDateTimeString(), simulationResult.getId()))
        );

        return retPastSimulations;
    }


    /**
     * Checks whether of not a simulation is currently loaded into the program.
     * @return true if a simulation is loaded, false otherwise.
     */
    public boolean isSimulationLoaded() {
        return currWorld == null;
    }
}