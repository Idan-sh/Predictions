package com.idansh.ui.manager;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariableDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.range.RangeDTO;
import com.idansh.dto.simulation.SimulationEndTDO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.engine.manager.EngineManager;
import com.idansh.ui.display.ConsoleIn;
import com.idansh.ui.display.ConsoleOut;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
            ConsoleOut.printMessage("Successfully loaded simulation data from file!");
        } catch (RuntimeException e) {
            ConsoleOut.printRuntimeException(e);
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

        if(pastSimulationsResults.size() == 0) {
            ConsoleOut.printError("please run a simulation before trying to shot past simulations!");
            return;
        }

        SimulationResultDTO simulationResultDTO;
        int userInput;

        // Try until user enters correct input
        while(true) {
            ConsoleOut.printPastSimulationsShortDetails(pastSimulationsResults);
            System.out.println();
            System.out.print("Please choose number of a past simulation result to view the details of,\nor " + (pastSimulationsResults.size() + 1) + " to return to the main menu: ");

            try {
                userInput = consoleIn.getIntInput();
            } catch (IllegalArgumentException e) {
                ConsoleOut.printError(e.getMessage());
                return;
            }

            if (userInput >= 1 && userInput <= pastSimulationsResults.size()) {
                simulationResultDTO = pastSimulationsResults.get(userInput - 1);
                break;
            } else {
                // Check if user requested to return the to main menu
                if(userInput == pastSimulationsResults.size() + 1)
                    return;

                ConsoleOut.printError("invalid past simulation number! Try again...");
            }
        }

        // Try until user enters correct input
        while(true) {
            ConsoleOut.printPastSimulationsMenu();

            try {
                userInput = consoleIn.getIntInput();
            } catch (IllegalArgumentException e) {
                ConsoleOut.printError(e.getMessage());
                return;
            }

            switch(userInput) {
                case 1:
                    showNumberOfEntities(simulationResultDTO);
                    return;

                case 2:
                    showPropertyInformation(simulationResultDTO);
                    return;

                case 3:
                    return; // User chose to return to the main menu

                default:
                    ConsoleOut.printError("invalid past simulation menu choice! try again...");
                    System.out.println();
            }
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
                userInput = consoleIn.getIntInput() - 1;
            } catch (IllegalArgumentException e) {
                ConsoleOut.printError(e.getMessage());
                return;
            }

            // Check if user requested to finish setup process
            if(userInput == environmentVariableDTOList.size())
                break;

            // Check if user input was outside the valid range
            if(userInput < 0 || userInput > environmentVariableDTOList.size()) {
                ConsoleOut.printError("invalid environment variable number chosen!");
                continue;
            }

            // Update the environment variable requested
            EnvironmentVariableDTO environmentVariableDTOtoSet = environmentVariableDTOList.get(userInput);
            ConsoleOut.printTitle("You chose to update: " + environmentVariableDTOtoSet.getName());
            environmentVariableDTOtoSet.setValue(getEnvironmentVariableValueInput(environmentVariableDTOtoSet));
        }

        // Show updated environment variables
        ConsoleOut.printEnvironmentVariables(environmentVariableDTOList);

        // Finished setting up the environment variables, run the simulation using them
        try{
            SimulationEndTDO simulationEndTDO = engineManager.runSimulation(environmentVariablesDTO);
            ConsoleOut.printSimulationEnd(simulationEndTDO);
        } catch (RuntimeException e) {
            ConsoleOut.printError("Simulation Stopped. Cause was: " + e.getMessage());
        }
    }


    /**
     * Get a new value for an environment variable from the user.
     * Keep requesting new value until the value entered is of the correct type and format for the environment variable.
     * @param environmentVariableDTO DTO that contains data of an environment variable, which will be sent back to the engine.
     * @return an Object value to be set into the environment variable.
     */
    private Object getEnvironmentVariableValueInput(EnvironmentVariableDTO environmentVariableDTO) {
        // Keeps iterating until user enters the correct format
        while (true) {
            try {
                RangeDTO rangeDTO = environmentVariableDTO.getRange();

                System.out.print("Please enter a value of the type \"" + environmentVariableDTO.getType() + "\": ");

                switch (environmentVariableDTO.getType()) {
                    case "decimal":
                        int integerValue;
                        try {
                            integerValue = consoleIn.getIntInput();
                        } catch (IllegalArgumentException e) {
                            ConsoleOut.printError(e.getMessage());
                            continue;
                        }

                        if(rangeDTO != null) {
                            if (InputValidator.isIntegerInRange(integerValue, rangeDTO)) {
                                return integerValue;
                            } else {
                                ConsoleOut.printError("integer number received is not within the environment variable's range (" + rangeDTO.getFrom() + ", " + rangeDTO.getTo() + ")!");
                                break;
                            }
                        } else
                            return integerValue;

                    case "float":
                        float floatValue = consoleIn.getFloatInput();

                        if(rangeDTO != null) {
                            if (InputValidator.isFloatInRange(floatValue, rangeDTO)) {
                                return floatValue;
                            } else {
                                ConsoleOut.printError("float number received is not within the environment variable's range (" + rangeDTO.getFrom() + ", " + rangeDTO.getTo() + ")!");
                                break;
                            }
                        } else
                            return floatValue;

                    case "boolean":
                        return consoleIn.getBooleanInput();

                    case "string":
                        String stringValue = consoleIn.getInput();

                        if(InputValidator.isStringValid(stringValue))
                            return stringValue;

                        ConsoleOut.printError("string received is not within the correct format!");
                        break;
                }
            } catch (IllegalArgumentException ignored) { } // Continue to receive another input
        }
    }


    /**
     * Prints the simulation result's entities with their initial amount and final amount in the population.
     * @param simulationResultDTO DTO containing information about the entities of the simulation.
     */
    private void showNumberOfEntities(SimulationResultDTO simulationResultDTO) {
        simulationResultDTO.getEntityDTOList().forEach(ConsoleOut::printNofEntityDTO);
    }


    /**
     * Prints all entities' properties information received from a simulation result DTO.
     * @param simulationResultDTO the simulation result DTO that contains the entities and their properties.
     */
    private void showPropertyInformation(SimulationResultDTO simulationResultDTO) {
        List<EntityDTO> entityDTOList = simulationResultDTO.getEntityDTOList();
        int userInput;

        ConsoleOut.printEntitiesList(entityDTOList);
        System.out.print("Please enter a number of an entity from the list: ");

        try {
            userInput = consoleIn.getIntInput() - 1;
        } catch (IllegalArgumentException e) {
            ConsoleOut.printError(e.getMessage());
            return;
        }

        if(userInput >= 0 && userInput < entityDTOList.size()) {
            List<PropertyDTO> propertyDTOList = entityDTOList.get(userInput).getPropertyDTOList();

            ConsoleOut.printMessage("You chose entity \"" + entityDTOList.get(userInput).getName() + "\".");
            ConsoleOut.printPropertiesList(propertyDTOList);
            System.out.print("Please enter a number of a property from the list: ");

            try {
                userInput = consoleIn.getIntInput() - 1;
            } catch (IllegalArgumentException e) {
                ConsoleOut.printError(e.getMessage());
                return;
            }

            if (userInput >= 0 && userInput < propertyDTOList.size()) {
                Map<Object, Integer> propertyValues = engineManager.getPropertyValues(simulationResultDTO.getId(), propertyDTOList.get(userInput));
                ConsoleOut.printTitle("Property " + propertyDTOList.get(userInput).getName() + " Values");
                ConsoleOut.printPropertyValues(propertyValues);
            } else
                ConsoleOut.printError("wrong input choice for property number!");
        } else
            ConsoleOut.printError("wrong input choice for entity number!");
    }
}