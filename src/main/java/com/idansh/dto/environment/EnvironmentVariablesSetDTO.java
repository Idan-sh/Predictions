package com.idansh.dto.environment;


import java.util.HashSet;
import java.util.Set;

public class EnvironmentVariablesSetDTO {
    Set<EnvironmentVariableDTO> environmentVariableDTOset;

    public EnvironmentVariablesSetDTO() {
        this.environmentVariableDTOset = new HashSet<>();
    }

    public Set<EnvironmentVariableDTO> getEnvironmentVariableInputDTOs() {
        return environmentVariableDTOset;
    }

    public void addEnvironmentVariableInput(String name, Object value) {
        environmentVariableDTOset.add(new EnvironmentVariableDTO(name, value));
    }
}
