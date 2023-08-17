package com.idansh.dto.environment;

/**
 * Created from user input in the UI,
 * and sent into the engine manager for initial setup of the environment variables.
 */
public class EnvironmentVariableDTO {
    private final String name;
    private Object value;

    public EnvironmentVariableDTO(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
