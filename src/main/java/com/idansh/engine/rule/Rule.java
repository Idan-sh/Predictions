package com.idansh.engine.rule;

import java.util.LinkedList;
import java.util.List;

public class Rule {
    private String name;
    private RuleActivation activation;
    private Integer numOfActions;
    private List<String> namesOfActions;

    public Rule(String name, RuleActivation activation, Integer numOfActions, List<String> namesOfActions) {
        this.name = name;
        this.activation = activation;
        this.numOfActions = numOfActions;
        this.namesOfActions = namesOfActions;
    }

    public String getName() {
        return name;
    }

    public RuleActivation getActivation() {
        return activation;
    }

    public Integer getNumOfActions() {
        return numOfActions;
    }

    public List<String> getNamesOfActions() {
        return namesOfActions;
    }
}
