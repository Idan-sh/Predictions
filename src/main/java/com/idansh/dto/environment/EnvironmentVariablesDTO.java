package com.idansh.dto.environment;


import java.util.HashSet;
import java.util.Set;

public class EnvironmentVariablesDTO {
    Set<EnvironmentVariableInputDTO> environmentVariableInputDTOs;

    public EnvironmentVariablesDTO() {
        this.environmentVariableInputDTOs = new HashSet<>();
    }

    public Set<EnvironmentVariableInputDTO> getEnvironmentVariableInputDTOs() {
        return environmentVariableInputDTOs;
    }

    public void addEnvironmentVariableInput(String name, Object value) {
        environmentVariableInputDTOs.add(new EnvironmentVariableInputDTO(name, value));
    }
}
