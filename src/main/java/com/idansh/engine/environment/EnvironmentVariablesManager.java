package com.idansh.engine.environment;

import com.idansh.engine.property.instance.Property;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Manager class for managing environment variables of a simulation.
 * Has a map of environment variables, which are defined as properties.
 */
public class EnvironmentVariablesManager {
    private final Map<String, Property> envVariables;

    public EnvironmentVariablesManager() {
        envVariables = new HashMap<>();
    }

    /**
     * Adds a new environment variable to the manager.
     * @param property the environment variable to add.
     */
    public void addEnvironmentVariable(Property property) {
        if(envVariables.containsKey(property.getName()))
            throw new IllegalArgumentException("Error: an environment variable with the given name already exists!");

        envVariables.put(property.getName(), property);
    }

//    public ActiveEnvironment createActiveEnvironment() {
//        return new ActiveEnvironmentImpl();
//    }


    /**
     * Get a single environment variable from the manager.
     * @param name the name of the environment variable to get.
     */
    public Property getEnvironmentVariable(String name) {
        return envVariables.get(name);
    }
}
