package com.idansh.dto.rule;

public class TerminationRuleDTO {
    String type;
    int value;

    public TerminationRuleDTO(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return type;
    }
}
