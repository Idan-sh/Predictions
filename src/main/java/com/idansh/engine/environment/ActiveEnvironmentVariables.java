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
    private final Map<String, Property> envVariables;


    public ActiveEnvironmentVariables() {
        this.envVariables = new HashMap<>();
    }


    /**
     * Adds an environment variable to the active variables.
     */
    public void addActiveEnvironmentVariable(Property envVariable) {
        if(envVariables.containsKey(envVariable.getName()))
            throw new IllegalArgumentException("Error: the environment variable name's already exists!");

        envVariables.put(envVariable.getName(), envVariable);
    }


    /**
     * Returns an active environment variable, defined as a property.
     */
    public Property getActiveEnvironmentVariable(String name) {
        if (!envVariables.containsKey(name))
            throw new IllegalArgumentException("Error: could not find environment variable with name " + name);


        return envVariables.get(name);
    }
}
