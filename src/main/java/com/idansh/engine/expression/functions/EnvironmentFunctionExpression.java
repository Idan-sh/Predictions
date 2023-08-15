package com.idansh.engine.expression.functions;

import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.property.instance.Property;

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
    public Object invoke() {
        Property activeEnvironmentVariable = activeEnvironmentVariables.getActiveEnvironmentVariable(environmentVariableName);
        return activeEnvironmentVariable.getValue();
    }
}
