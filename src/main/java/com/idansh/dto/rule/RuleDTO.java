package com.idansh.dto.rule;

import java.util.ArrayList;
import java.util.List;

public class RuleDTO {
    private final String name;
    private final int ticks;
    private final double probability;
    private final int nofActions;
    private List<String> actionNamesList;

    public RuleDTO(String name, int ticks, double probability, int nofActions) {
        this.name = name;
        this.ticks = ticks;
        this.probability = probability;
        this.nofActions = nofActions;
        this.actionNamesList = new ArrayList<>();
    }

    public void addActionName(String name) {
        actionNamesList.add(name);
    }

    public String getName() {
        return name;
    }

    public int getTicks() {
        return ticks;
    }

    public double getProbability() {
        return probability;
    }

    public int getNofActions() {
        return nofActions;
    }

    public List<String> getActionNamesList() {
        return actionNamesList;
    }

    @Override
    public String toString() {
        return name;
    }
}
