package com.idansh.javafx.manager;


import com.idansh.dto.environment.EnvironmentVariablesListDTO;
import com.idansh.dto.property.PropertyDTO;
import com.idansh.dto.simulation.LoadedSimulationDTO;

import java.io.File;
import java.util.List;
import java.util.Map;

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
    public LoadedSimulationDTO getLoadedSimulationDetails() {
        if(!engineHandler.isSimulationLoaded()) {
            throw new RuntimeException("No simulation loaded! Please load before trying to show details!");
        }

        return engineHandler.getLoadedSimulationDetails();
    }


    /**
     * Runs the current loaded simulation.
     * @param environmentVariablesListDTO DTO containing a list of all the updated environment variables
     *                                   to run the simulation with.
     */
    public void startSimulation(EnvironmentVariablesListDTO environmentVariablesListDTO) {
        if(!engineHandler.isSimulationLoaded()) {
            throw new RuntimeException("No simulation loaded! Please load before trying to run!");
        }

        engineHandler.runSimulation(environmentVariablesListDTO);
    }


    /**
     * Get all the past simulations' results from the engine.
     */
    public List<Object> getSimulationsList() {
        return engineHandler.getSimulationsList();
    }


    public void setEntityAmount(String entityName, int amount) {
        engineHandler.setEntityAmount(entityName, amount);
    }


    /**
     * Gets from the engine handler a map of:
     * 1. key: the property's value
     * 2. value: the amount of entities in the population with this value
     */
    public Map<Object, Integer> getPropertyValues(int simulationResultID, PropertyDTO propertyDTO) {
        return engineHandler.getPropertyValues(simulationResultID, propertyDTO);
    }}
