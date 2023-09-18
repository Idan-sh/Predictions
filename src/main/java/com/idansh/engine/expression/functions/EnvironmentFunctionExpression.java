package com.idansh.engine.expression.functions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.property.instance.PropertyType;

public class EnvironmentFunctionExpression extends FunctionActivationExpression {
    private final String environmentVariableName;
    private final ActiveEnvironmentVariables activeEnvironmentVariables;

    public EnvironmentFunctionExpression(ActiveEnvironmentVariables activeEnvironmentVariables, String environmentVariableName) {
        super(Type.ENVIRONMENT);
        this.environmentVariableName = environmentVariableName;
        this.activeEnvironmentVariables = activeEnvironmentVariables;
    }

    /**
     * Finds and returns the value of an environment variable.
     * The return value is in object format and needs to be converted,
     * according to the PropertyType of the returned value.
     * @return The value of the environment variable.
     */
    @Override
    public Object getValue(Entity ignored) {
        return activeEnvironmentVariables.getActiveEnvironmentVariable(environmentVariableName).getValue();
    }

    @Override
    public Object getValue(Entity ignored1, Entity ignored2) {
        return activeEnvironmentVariables.getActiveEnvironmentVariable(environmentVariableName).getValue();
    }

    /**
     * Finds and returns the type of an environment variable.
     * @return The type of the environment variable with the given name.
     */
    @Override
    public PropertyType getType() {
        return activeEnvironmentVariables.getActiveEnvironmentVariable(environmentVariableName).getType();
    }

    @Override
    public String getAsString() {
        return "EnvironmentVariable(" + environmentVariableName + ")";
    }
}
