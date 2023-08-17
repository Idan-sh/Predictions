package com.idansh.ui.manager;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.engine.manager.EngineManager;
import com.idansh.ui.display.ConsoleIn;
import com.idansh.ui.display.ConsoleOut;

import java.util.List;

/**
 * Connects the UI with the engine components through the various DTOs.
 */
public class EngineHandler {
    private final EngineManager engineManager;
    private final ConsoleIn consoleIn;

    public EngineHandler() {
        engineManager = new EngineManager();
        consoleIn = new ConsoleIn();
    }


    /**
     * Gets a path of a simulation XML file, loads the file into the engine.
     * If the path entered is not valid, prints a message to the user and returns.
     */
    public void loadSimulationFromFile() {
        System.out.print("Please enter path to the XML world config file: ");
        String path = consoleIn.getInput();

        engineManager.loadSimulationFromFile(path);

//        try {
//            engineManager.loadSimulationFromFile(path);
//        } catch (RuntimeException e) {
//            ConsoleOut.printError(e.getMessage());
//        }
    }


    /**
     * Checks if there is currently a simulation loaded.
     * @return true if there is, false otherwise.
     */
    public boolean isSimulationLoaded() {
        return engineManager.isSimulationLoaded();
    }


    /**
     * Gets CurrentSimulationDTO from the engine with details of the current simulation,
     * prints the simulation details to the screen.
     */
    public void showCurrentSimulationDetails() {
        ConsoleOut.printCurrentSimulationDetails(engineManager.getCurrentSimulationDetails());
    }


    /**
     * Gets SimulationResultDTO from the engine with details of a past simulation with the given simulation ID,
     * prints the simulation details to the screen.
     */
    private void showPastSimulationDetails(int simulationId) {
        ConsoleOut.printPastSimulationDetails(engineManager.getPastSimulationDetailsById(simulationId));
    }


    /**
     * Gets past simulations' results from the engine, prints simple information of each result,
     * and allows the user to choose which simulation to show full details of.
     * Prints the chosen simulation result.
     */
    public void showPastSimulations() {
        List<SimulationResultDTO> pastSimulationsResults = engineManager.getPastSimulationsResults();
        SimulationResultDTO simulationResultDTO;
        int userInput;

        ConsoleOut.printPastSimulationsShortDetails(pastSimulationsResults);

        System.out.print("\nPlease choose number of a past simulation result to view the details of: ");

        try {
            userInput = consoleIn.getIntInput();
        } catch (NumberFormatException e) { return; }

        if(userInput >= 1 && userInput <= pastSimulationsResults.size()) {
            simulationResultDTO = pastSimulationsResults.get(userInput -1);
            ConsoleOut.printPastSimulationDetails(simulationResultDTO);
        }
        else {
            ConsoleOut.printError("invalid past simulation number!");
            return;
        }

        ConsoleOut.printPastSimulationsMenu();
        try {
            userInput = consoleIn.getIntInput();
        } catch (NumberFormatException e) { return; }

        switch(userInput) {
            case 1:
                showNumberOfEntities(simulationResultDTO);
                break;

            case 2:
                showPropertyInformation(simulationResultDTO);
                break;

            default:
                ConsoleOut.printError("invalid past simulation menu choice!");
        }
    }


    /**
     * Initiates the run process of the current loaded simulation.
     */
    public void runSimulation() {
        EnvironmentVariablesListDTO environmentVariablesListDTO = engineManager.getEnvironmentVariablesListDTO();

        ConsoleOut.printEnvironmentVariables(environmentVariablesListDTO);

        //todo - let the user update env variables

        engineManager.runSimulation(environmentVariablesListDTO);
    }


    /**
     * Prints the simulation result's entities with their initial amount and final amount in the population.
     * @param simulationResultDTO DTO containing information about the entities of the simulation.
     */
    private void showNumberOfEntities(SimulationResultDTO simulationResultDTO) {
        simulationResultDTO.getEntityDTOList().forEach(ConsoleOut::printNofEntityDTO);
    }

    private void showPropertyInformation(SimulationResultDTO simulationResultDTO) {
        List<EntityDTO> entityDTOList = simulationResultDTO.getEntityDTOList();
        int userInput;

        int counter = 1;

        for(EntityDTO entityDTO : entityDTOList) {
            System.out.print(counter++ + ". ");
            ConsoleOut.printEntity(entityDTO);
        }

        try {
            userInput = consoleIn.getIntInput();
        } catch (NumberFormatException e) { return; }

        if(userInput >= 1 && userInput < entityDTOList.size()) {
            List<PropertyDTO> propertyDTOList = entityDTOList.get(userInput - 1).getPropertyDTOList();

            counter = 1;
            for(PropertyDTO propertyDTO : propertyDTOList) {
                System.out.print(counter++ + ". ");
                ConsoleOut.printProperty(propertyDTO);
            }

            try {
                userInput = consoleIn.getIntInput();
            } catch (NumberFormatException e) { return; }

            if (userInput >= 1 && userInput < propertyDTOList.size()) {
                ConsoleOut.printProperty(propertyDTOList.get(userInput));
            } else
                ConsoleOut.printError("wrong input choice for property number!");
        } else
            ConsoleOut.printError("wrong input choice for entity number!");

    }

   // ------------------------------------------------------------------------------------------------------------------------------







//
//
//
//    /**
//     * This method gets from the user input for each environment property given from the 'StartData'.
//     *
//     * @param startData The DTO object from the engine that contains information about the environment properties.
//     * @return a DTOThirdFunction object to send to the engine.
//     */
//    private DTOThirdFunction createDTOThirdFunctionObject(StartData startData){
//        List<DTOEnvironmentVariable> environmentVariables = startData.getEnvironmentVariables();
//        DTOThirdFunction ret = new DTOThirdFunction();
//        Object valueToSend;
//        String input;
//
//        Console.showThirdFuncFirstMessage();
//        for(DTOEnvironmentVariable dtoEnvironmentVariable : environmentVariables){
//            Console.showEnvPropertyDet(dtoEnvironmentVariable);
//            input = Input.getInput();
//            if (input.equals("\n")){
//                ret.updateEnvPropertyUserInputs(dtoEnvironmentVariable.getName(),getIsRandomInit(null), null);
//            }
//            else {
//                valueToSend = tryToParse(input, dtoEnvironmentVariable);
//                ret.updateEnvPropertyUserInputs(dtoEnvironmentVariable.getName(), getIsRandomInit(valueToSend), valueToSend);
//            }
//        }
//
//        return ret;
//    }
//
//    /**
//     * Try to parse the input value from the user and check if the input is valid for the given environment property.
//     * If the input value is not valid, the user will try to enter new value.
//     *
//     * @param value the user input.
//     * @param dtoEnvironmentVariable a DTO object represent an environment property.
//     * @return the given value parsed.
//     */
//    private Object tryToParse(String value, DTOEnvironmentVariable dtoEnvironmentVariable){
//        InputValidator inputValidator = new InputValidator();
//        boolean valueIsNotValid = true;
//        Object ret = null;
//
//        while (valueIsNotValid) {
//            // If after an error the user decide to random initialize the value.
//            if(value.equals("\n")) {
//                break;
//            }
//
//            try{
//                switch (dtoEnvironmentVariable.getType()){
//                    case "int":
//                        int integerValue = Integer.parseInt(value);
//                        inputValidator.isIntegerInRange(integerValue, (int)dtoEnvironmentVariable.getFrom(), (int)dtoEnvironmentVariable.getTo());
//                        ret = integerValue;
//                        break;
//                    case "double":
//                        double doubleValue = Double.parseDouble(value);
//                        inputValidator.isDoubleInRange(doubleValue, dtoEnvironmentVariable.getFrom(), dtoEnvironmentVariable.getTo());
//                        ret = doubleValue;
//                        break;
//                    case "boolean":
//                        inputValidator.validateBoolean(value);
//                        ret = Boolean.parseBoolean(value);
//                        break;
//                    case "string":
//                        inputValidator.validateStringValue(value);
//                        ret = value;
//                        break;
//                }
//
//                valueIsNotValid = false;
//
//            } catch (IllegalStringValueException e) {
//                Console.printGivenMessage(e.getMessage());
//            } catch (OutOfRangeException e) {
//                Console.printGivenMessage("The number is out of the property range! Please try again.");
//            } catch (NumberFormatException e){
//                Console.printGivenMessage("The number type doesn't match the property's value type! Please try again.");
//            } catch (IllegalBooleanValueException e) {
//                Console.printGivenMessage("Thr property's value type is boolean! Please try again.");
//            }
//            finally { // If an error occurred, the user will enter a new input.
//                if(valueIsNotValid){
//                    value = Input.getInput();
//                }
//            }
//        }
//
//        return ret;
//    }
}