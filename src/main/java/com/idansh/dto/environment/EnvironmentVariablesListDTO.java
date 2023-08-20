package com.idansh.dto.environment;

import com.idansh.dto.range.RangeDTO;

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

    public void addEnvironmentVariableInput(String name, Object value, String type) {
        environmentVariableDTOlist.add(new EnvironmentVariableDTO(name, value, type));
    }

    public void addEnvironmentVariableInput(String name, Object value, String type, RangeDTO rangeDTO) {
        environmentVariableDTOlist.add(new EnvironmentVariableDTO(name, value, type, rangeDTO));
    }
}
