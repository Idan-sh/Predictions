package com.idansh.dto.environment;

import com.idansh.dto.range.RangeDTO;

/**
 * Created from user input in the UI,
 * and sent into the engine manager for initial setup of the environment variables.
 */
public class EnvironmentVariableDTO {
    private final String name;
    private Object value;
    private final String type;
    private final RangeDTO range;

    public EnvironmentVariableDTO(String name, Object value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.range = null;
    }

    public EnvironmentVariableDTO(String name, Object value, String type, RangeDTO range) {
        this.name = name;
        this.value = value;
        this.type = type;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public Object getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    public RangeDTO getRange() {
        return range;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return name;
    }
}
