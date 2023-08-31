package com.idansh.javafx.manager;

import com.idansh.dto.entity.EntityDTO;
import com.idansh.dto.environment.EnvironmentVariableDTO;
import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.range.RangeDTO;
import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.dto.simulation.SimulationEndTDO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.engine.manager.EngineManager;
import com.idansh.javafx.display.ConsoleIn;
import com.idansh.javafx.display.ConsoleOut;

import java.io.File;
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
    public void loadSimulationFromFile(File file) {
        engineManager.loadSimulationFromFile(file);
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
    public CurrentSimulationDTO getCurrentSimulationDetails() {
        return engineManager.getCurrentSimulationDetails();
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
     * @throws RuntimeException In case that no simulations were previously ran.
     */
    public List<SimulationResultDTO> getPastSimulationsResults() {
        List<SimulationResultDTO> pastSimulationsResults = engineManager.getPastSimulationsResults();

        if(pastSimulationsResults.size() == 0) {
            throw new RuntimeException("Please run a simulation before trying to shot past simulations!");
        }

        return pastSimulationsResults;
    }


    /**
     * Initiates the running process of the current loaded simulation.
     * Receives the result from the engine.
     */
    public void runSimulation(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        SimulationEndTDO simulationEndTDO = engineManager.runSimulation(environmentVariablesListDTO);
        ConsoleOut.printSimulationEnd(simulationEndTDO);
        // todo- do something with the simulation end result
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
                if(propertyValues.isEmpty())
                    ConsoleOut.printMessage("No values to display, entity with this property does not exist in the population, maybe all of its instances died :(");
            } else
                ConsoleOut.printError("wrong input choice for property number!");
        } else
            ConsoleOut.printError("wrong input choice for entity number!");
    }
}