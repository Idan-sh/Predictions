package com.idansh.engine.manager;

import com.idansh.dto.environment.EnvironmentVariableDTO;
import com.idansh.dto.environment.EnvironmentVariablesSetDTO;
import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.engine.manager.result.SimulationIdGenerator;
import com.idansh.engine.manager.result.SimulationResult;
import com.idansh.engine.property.creator.factory.PropertyFactory;
import com.idansh.engine.world.World;
import com.idansh.jaxb.unmarshal.reader.Reader;

import java.util.HashMap;
import java.util.Map;

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


    public CurrentSimulationDTO getCurrentSimulationDetails() {
        return new CurrentSimulationDTO(); // todo: add current simulation details to DTO
    }


    /**
     * Finds a simulation result from the past simulations and returns its information/details.
     * @param simulationId the ID of the simulation to get.
     * @return DTO containing its information/details.
     */
    public SimulationResultDTO getSimulationDetailsById(int simulationId) {
        SimulationResult simulationResult = pastSimulations.get(simulationId);
        return new SimulationResultDTO(); // todo: add simulation details to constructor
    }

        private SimulationResult getResultDataById(int id) {
        return pastSimulations.get(id);
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
        pastSimulations.put(SimulationIdGenerator.getID(), simulationResult);

        // TODO : return to the UI the simulation result. digest simulationResult...
        return new SimulationResultDTO();
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


    // ------------------------------------------------------------------------------------------------------------------------------


    /**
     * Create and return the DTO start data which contains information about the simulation's environment variables.
     *
     * @return a StartData DTO
     */
    @Override
    public StartData getSimulationStartData() {
        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>();
        Map<String, Property> environmentProperties = this.world.getEnvironmentProperties();
        Property valueFromTheMap;

        for (Map.Entry<String, Property> entry : environmentProperties.entrySet()) {
            valueFromTheMap = entry.getValue();
            environmentVariables.add(getDTOEnvironmentVariable(valueFromTheMap));
        }

        return new StartData(environmentVariables);
    }

    /**
     * Create a 'DTOEnvironmentVariable' which contain the given environment variable's data and return it.
     */
    private DTOEnvironmentVariable getDTOEnvironmentVariable(Property valueFromTheMap){
        String name = valueFromTheMap.getName(), type = valueFromTheMap.getType().toString().toLowerCase();
        Double from = null, to = null;

        if(valueFromTheMap.getType() == PropertyType.DOUBLE){
            DoubleProperty doubleProperty = (DoubleProperty)valueFromTheMap;
            from = doubleProperty.getFrom();
            to = doubleProperty.getTo();
        } else if (valueFromTheMap.getType() == PropertyType.INT) {
            IntProperty intProperty = (IntProperty)valueFromTheMap;
            from = (double)intProperty.getFrom();
            to = (double)intProperty.getTo();
        }

        return new DTOEnvironmentVariable(name,type,from,to);
    }
}