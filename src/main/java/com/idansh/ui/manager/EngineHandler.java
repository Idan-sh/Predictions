package com.idansh.ui.manager;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariableDTO;
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

        try {
            engineManager.loadSimulationFromFile(path);
        } catch (RuntimeException e) {
            ConsoleOut.printError(e.getMessage());
        }
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


//    /**
//     * Gets SimulationResultDTO from the engine with details of a past simulation with the given simulation ID,
//     * prints the simulation details to the screen.
//     */
//    private void showPastSimulationDetails(int simulationId) {
//        ConsoleOut.printPastSimulationDetails(engineManager.getPastSimulationDetailsById(simulationId));
//    }


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
     * Firstly lets the user update the environment variables for the simulation,
     * then lets the user continue and start the running process of the simulation.
     */
    public void runSimulation() {
        EnvironmentVariablesListDTO environmentVariablesDTO = engineManager.getEnvironmentVariablesListDTO();
        List<EnvironmentVariableDTO> environmentVariableDTOList = environmentVariablesDTO.getEnvironmentVariableInputDTOs();
        int userInput;

        while(true) {
            ConsoleOut.printEnvironmentVariables(environmentVariableDTOList);

            System.out.print("Please choose the number of environment variable to set its value manually, or enter " + (environmentVariableDTOList.size() + 1) + " to skip: ");

            try {
                userInput = consoleIn.getIntInput();
            } catch (NumberFormatException e) { return; }

            // Check if user requested to finish setup process
            if(userInput == environmentVariableDTOList.size() + 1)
                break;

            // Check if user input was outside the valid range
            if(userInput < 1 || userInput > environmentVariableDTOList.size() + 1) {
                ConsoleOut.printError("invalid environment variable number chosen!");
                return;
            }

            // Update the environment variable requested
            EnvironmentVariableDTO environmentVariableDTOtoSet = environmentVariableDTOList.get(userInput);
            environmentVariableDTOtoSet.setValue(getEnvironmentVariableValueInput(environmentVariableDTOtoSet));
        }

        // Finished setting up the environment variables, run the simulation using them
        engineManager.runSimulation(environmentVariablesDTO);
    }


    /**
     * Get a new value for an environment variable from the user.
     * Keep requesting new value until the value entered is of the correct type and format for the environment variable.
     * @param environmentVariableDTO DTO that contains data of an environment variable, which will be sent back to the engine.
     * @return an Object value to be set into the environment variable.
     */
    private Object getEnvironmentVariableValueInput(EnvironmentVariableDTO environmentVariableDTO) {
        InputValidator inputValidator = new InputValidator();
        boolean valueIsNotValid = true;
        Object ret = null;

        while (valueIsNotValid) {
            // If after an error the user decide to random initialize the value.
            if(value.equals("\n")) {
                break;
            }

            try{
                switch (dtoEnvironmentVariable.getType()){
                    case "int":
                        int integerValue = Integer.parseInt(value);
                        inputValidator.isIntegerInRange(integerValue, (int)dtoEnvironmentVariable.getFrom(), (int)dtoEnvironmentVariable.getTo());
                        ret = integerValue;
                        break;
                    case "double":
                        double doubleValue = Double.parseDouble(value);
                        inputValidator.isDoubleInRange(doubleValue, dtoEnvironmentVariable.getFrom(), dtoEnvironmentVariable.getTo());
                        ret = doubleValue;
                        break;
                    case "boolean":
                        inputValidator.validateBoolean(value);
                        ret = Boolean.parseBoolean(value);
                        break;
                    case "string":
                        inputValidator.validateStringValue(value);
                        ret = value;
                        break;
                }

                valueIsNotValid = false;

            } catch (IllegalStringValueException e) {
                Console.printGivenMessage(e.getMessage());
            } catch (OutOfRangeException e) {
                Console.printGivenMessage("The number is out of the property range! Please try again.");
            } catch (NumberFormatException e){
                Console.printGivenMessage("The number type doesn't match the property's value type! Please try again.");
            } catch (IllegalBooleanValueException e) {
                Console.printGivenMessage("Thr property's value type is boolean! Please try again.");
            }
            finally { // If an error occurred, the user will enter a new input.
                if(valueIsNotValid){
                    value = Input.getInput();
                }
            }
        }

        return ret;
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
}