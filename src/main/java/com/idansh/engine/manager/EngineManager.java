package com.idansh.engine.manager;

import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.engine.manager.result.SimulationResult;
import com.idansh.engine.world.World;
import com.idansh.jaxb.unmarshal.reader.Reader;

import java.util.HashMap;
import java.util.Map;

/**
 * The UI will handle the engine through this class.
 * Each method will return a DTO that contains data from the simulation.
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


    public void runSimulation(DTOThirdFunction dtoThirdFunction) {
        // fetch the user data input into the simulation's environment properties.
        fetchDTOThirdFunctionObject(dtoThirdFunction);
        // run the simulation.
        this.world.invoke();
        // TODO : add the simulation result data to 'pastSimulations' and return to the UI these results.
    }

    // ------------------------------------------------------------------------------------------------------------------------------




//
//
//    /**
//     * Get the third function's DTO object, extract the user input from this object and update the simulation's environment variables.
//     *
//     * @param dtoThirdFunction the third function's DTO object
//     */
//    private void fetchDTOThirdFunctionObject(DTOThirdFunction dtoThirdFunction){
//        Set<EnvPropertyUserInput> envPropertyUserInputs = dtoThirdFunction.getEnvPropertyUserInputs();
//        Map<String, Property> environmentProperties = this.world.getEnvironmentProperties();
//        Property envProperty;
//
//        for (EnvPropertyUserInput envPropertyUserInput : envPropertyUserInputs){
//            envProperty = environmentProperties.get(envPropertyUserInput.getName());
//            if(envPropertyUserInput.isRandomInit()){
//                envProperty.updateValueAndIsRandomInit(getRandomValueByType(envProperty),envPropertyUserInput.isRandomInit());
//            }
//            else {
//                envProperty.updateValueAndIsRandomInit(envPropertyUserInput.getValue(), envPropertyUserInput.isRandomInit());
//            }
//        }
//    }
//
//    /**
//     * This method generate a random object match to the environment variable's type.
//     *
//     * @param envProperty the environment variable
//     * @return a random object
//     */
//    private Object getRandomValueByType(Property envProperty){
//        Object ret = null;
//
//        switch (envProperty.getType()){
//            case INT:
//                IntProperty intProperty = (IntProperty)envProperty;
//                RandomValueGenerator<Integer> randomIntValueGenerator = new IntRndValueGen(intProperty.getFrom(), intProperty.getTo());
//                ret = randomIntValueGenerator.generateRandomValue();
//                break;
//            case DOUBLE:
//                DoubleProperty doubleProperty = (DoubleProperty)envProperty;
//                RandomValueGenerator<Double> randomDoubleValueGenerator = new DoubleRndValueGen(doubleProperty.getFrom(), doubleProperty.getTo());
//                ret = randomDoubleValueGenerator.generateRandomValue();
//                break;
//            case BOOLEAN:
//                RandomValueGenerator<Boolean> randomBooleanValueGenerator = new BoolRndValueGen();
//                ret = randomBooleanValueGenerator.generateRandomValue();
//                break;
//            case STRING:
//                RandomValueGenerator<String> randomStringValueGenerator = new StringRndValueGen();
//                ret = randomStringValueGenerator.generateRandomValue();
//                break;
//        }
//
//        return ret;
//    }
//
//    /**
//     * Create and return the DTO start data which contains information about the simulation's environment variables.
//     *
//     * @return a StartData DTO
//     */
//    @Override
//    public StartData getSimulationStartData() {
//        List<DTOEnvironmentVariable> environmentVariables = new ArrayList<>();
//        Map<String, Property> environmentProperties = this.world.getEnvironmentProperties();
//        Property valueFromTheMap;
//
//        for (Map.Entry<String, Property> entry : environmentProperties.entrySet()) {
//            valueFromTheMap = entry.getValue();
//            environmentVariables.add(getDTOEnvironmentVariable(valueFromTheMap));
//        }
//
//        return new StartData(environmentVariables);
//    }
//
//    /**
//     * Create a 'DTOEnvironmentVariable' which contain the given environment variable's data and return it.
//     */
//    private DTOEnvironmentVariable getDTOEnvironmentVariable(Property valueFromTheMap){
//        String name = valueFromTheMap.getName(), type = valueFromTheMap.getType().toString().toLowerCase();
//        Double from = null, to = null;
//
//        if(valueFromTheMap.getType() == PropertyType.DOUBLE){
//            DoubleProperty doubleProperty = (DoubleProperty)valueFromTheMap;
//            from = doubleProperty.getFrom();
//            to = doubleProperty.getTo();
//        } else if (valueFromTheMap.getType() == PropertyType.INT) {
//            IntProperty intProperty = (IntProperty)valueFromTheMap;
//            from = (double)intProperty.getFrom();
//            to = (double)intProperty.getTo();
//        }
//
//        return new DTOEnvironmentVariable(name,type,from,to);
//    }
}