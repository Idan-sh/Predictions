package com.idansh.engine;

import java.util.LinkedList;
import java.util.List;

public class Rule extends HasUniqueName {
    static List<String> namesPool = new LinkedList<>();
    private RuleActivation activation;
    private Integer numOfActions;
    private List<String> namesOfActions;

    public Rule(String name) {
        super(name, namesPool);
    }
}
