package com.idansh.dto.environment;


import java.util.ArrayList;
import java.util.List;

public class EnvironmentVariablesListDTO {
    List<EnvironmentVariableDTO> environmentVariableDTOlist;

    public EnvironmentVariablesListDTO() {
        this.environmentVariableDTOlist = new ArrayList<>();
    }

    public List<EnvironmentVariableDTO> getEnvironmentVariableInputDTOs() {
        return environmentVariableDTOlist;
    }

    public void addEnvironmentVariableInput(String name, Object value) {
        environmentVariableDTOlist.add(new EnvironmentVariableDTO(name, value));
    }
}
