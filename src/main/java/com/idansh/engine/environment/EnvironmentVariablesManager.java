package com.idansh.engine.environment;

import com.idansh.engine.property.creator.factory.PropertyFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager class for managing environment variables factories for a simulation.
 * Has a map of environment variables to create, which are defined as property factories.
 * Using the manager an ActiveEnvironmentVariables class instance will be created,
 * which will be used by the simulated world.
 */
public class EnvironmentVariablesManager {
    private final Map<String, PropertyFactory> envVariablesToCreate;

    public EnvironmentVariablesManager() {
        envVariablesToCreate = new HashMap<>();
    }

    /**
     * Adds a new environment variable factory to the manager,
     * which will create an active environment variable.
     */
    public void addEnvironmentVariableFactory(PropertyFactory propertyFactory) {
        if(envVariablesToCreate.containsKey(propertyFactory.getName()))
            throw new IllegalArgumentException("Error: an environment variable with the given name already exists!");

        envVariablesToCreate.put(propertyFactory.getName(), propertyFactory);
    }


    /**
     * Creates an active environment variables class instance using the manager's environment variables factories.
     * @return ActiveEnvironmentVariables instance which contains activated environment variables,
     *         to be used by the simulation.
     */
    public ActiveEnvironmentVariables createActiveEnvironmentVariables() {
        ActiveEnvironmentVariables activeEnvironmentVariables = new ActiveEnvironmentVariables();

        // Create the environment variables according to the factories:
        envVariablesToCreate.forEach(
                (name, factory) ->
                        activeEnvironmentVariables.addActiveEnvironmentVariable(factory.createProperty()));

        return activeEnvironmentVariables;
    }


    /**
     * Get a single environment variable from the manager.
     * @param name the name of the environment variable to get.
     */
    public PropertyFactory getEnvironmentVariable(String name) {
        return envVariablesToCreate.get(name);
    }
}
