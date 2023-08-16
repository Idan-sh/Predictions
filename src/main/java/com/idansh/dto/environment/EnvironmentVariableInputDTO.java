package com.idansh.dto.environment;

/**
 * Created from user input in the UI,
 * and sent into the engine manager for initial setup of the environment variables.
 */
public class EnvironmentVariableInputDTO {
    private String name;
    private Object value;

    public EnvironmentVariableInputDTO(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }
}
