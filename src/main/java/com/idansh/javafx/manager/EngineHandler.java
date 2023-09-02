package com.idansh.javafx.manager;

import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.simulation.LoadedSimulationDTO;
import com.idansh.dto.simulation.SimulationEndTDO;
import com.idansh.dto.simulation.SimulationResultDTO;
import com.idansh.engine.manager.EngineManager;

import java.io.File;
import java.util.List;

/**
 * Connects the UI with the engine components through the various DTOs.
 */
public class EngineHandler {
    private final EngineManager engineManager;

    public EngineHandler() {
        engineManager = new EngineManager();
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
    public LoadedSimulationDTO getLoadedSimulationDetails() {
        return engineManager.getLoadedSimulationDetails();
    }


    /**
     * Gets past simulations' results from the engine, prints simple information of each result,
     * and allows the user to choose which simulation to show full details of.
     * @throws RuntimeException In case that no simulations were previously ran.
     */
    public List<SimulationResultDTO> getPastSimulationsResults() {
        return engineManager.getPastSimulationsResults();
    }


    /**
     * Initiates the running process of the current loaded simulation.
     */
    public void runSimulation(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        engineManager.runSimulation(environmentVariablesListDTO);
    }

    public void setEntityAmount(String entityName, int amount) {
        engineManager.setEntityAmount(entityName, amount);
    }
}