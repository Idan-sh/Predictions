package com.idansh.engine.environment;

import com.idansh.engine.property.instance.Property;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that holds the instances of the environment variables,
 * which will be used by the simulated world.
 * @apiNote Should only be instantiated by the EnvironmentVariablesManager class !!!
 */
public class ActiveEnvironmentVariables {
    private final Map<String, Property> environmentVariables;


    public ActiveEnvironmentVariables() {
        this.environmentVariables = new HashMap<>();
    }


    /**
     * Adds an environment variable to the active variables.
     */
    public void addActiveEnvironmentVariable(Property envVariable) {
        if(environmentVariables.containsKey(envVariable.getName()))
            throw new IllegalArgumentException("Error: the environment variable name's already exists!");

        environmentVariables.put(envVariable.getName(), envVariable);
    }


    /**
     * Returns an active environment variable, defined as a property.
     */
    public Property getActiveEnvironmentVariable(String name) {
        if (!environmentVariables.containsKey(name))
            throw new IllegalArgumentException("Error: could not find environment variable with name " + name);

        return environmentVariables.get(name);
    }

    public Map<String, Property> getEnvironmentVariables() {
        return environmentVariables;
    }
}
