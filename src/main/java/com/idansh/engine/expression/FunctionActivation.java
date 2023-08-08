package com.idansh.engine.expression;

import com.idansh.engine.environment.variable.EnvironmentVariable;
import com.idansh.engine.world.World;

import java.util.Random;

/**
 * Activates the proper function
 */
public class FunctionActivation implements Expression {
    private String name;

    @Override
    public String getFirstWord() {
        return null;
    }

    @Override
    public Object environment(String name) {
        EnvironmentVariable<?> var = World.environmentVariables.get(name);

        if(var == null)
            throw new RuntimeException("Error: invalid environment variable name.");
        return var.getValue();
    }

    @Override
    public int random(int max) {
        Random random = new Random();
        return random.nextInt(max + 1); // Generate random number
    }


}
