package com.idansh.engine.rule;

import com.idansh.engine.actions.Action;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Rule {
    private final String name;
    private final RuleActivation activation;    // Determines when to activate the rule
    private final Set<Action> actionsSet;             // Set of actions to be preformed when the rule is activated

    public Rule(String name, RuleActivation activation) {
        this.name = name;
        this.activation = activation;
        this.actionsSet = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public RuleActivation getActivation() {
        return activation;
    }


    /**
     * Adds a new action to be preformed when the rule is activated.
     * @param action must be unique.
     */
    public void addAction(Action action) {
        if(actionsSet.contains(action))
            throw new IllegalArgumentException("Error: action " + action.getClass() + " already exists in the rules set!");

        actionsSet.add(action);
    }

    public Set<Action> getActionsSet() {
        return actionsSet;
    }
}
