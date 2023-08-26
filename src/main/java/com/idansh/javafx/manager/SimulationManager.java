package com.idansh.javafx.manager;


import com.idansh.dto.simulation.CurrentSimulationDTO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.javafx.display.ConsoleOut;

import java.io.File;
import java.util.List;

import static java.lang.System.exit;

/**
 * Handles the simulations.
 * The main loop of the simulation will run through this class.
 */
public class SimulationManager {
    private final EngineHandler engineHandler;

    public SimulationManager() {
        engineHandler = new EngineHandler();
    }


    /**
     * Loads an XML file with world data into the engine.
     * @param file a valid XML file that was chosen by the user.
     */
    public void loadSimulationFromFile(File file) {
        engineHandler.loadSimulationFromFile(file);
    }


    /**
     * Gets from the engine the current loaded simulation's details.
     * @return DTO containing details of the current loaded simulation.
     */
    public CurrentSimulationDTO getCurrentSimulationDetails() {
        if(!engineHandler.isSimulationLoaded()) {
            ConsoleOut.printError("no simulation loaded! please load before trying to run!");
            // todo- show error popup, return
        }

        return engineHandler.getCurrentSimulationDetails();
    }


    /**
     * Runs the current loaded simulation.
     */
    public void runSimulation() {
        if(!engineHandler.isSimulationLoaded()) {
            ConsoleOut.printError("no simulation loaded! please load before trying to run!");
            // todo- show error popup, return
        }
        engineHandler.runSimulation();
    }


    /**
     * Get all the past simulations' results from the engine.
     */
    public List<SimulationResultDTO> getPastSimulationsResults() {
        return engineHandler.getPastSimulationsResults();
    }
}
