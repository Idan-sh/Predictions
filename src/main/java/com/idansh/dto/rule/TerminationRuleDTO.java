package com.idansh.dto.rule;

public class TerminationRuleDTO {
    String type;
    Integer value;

    public TerminationRuleDTO(String type, Integer value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type;
    }
}
