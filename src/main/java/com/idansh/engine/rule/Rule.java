package com.idansh.engine.rule;

import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.helpers.Counter;

import java.util.HashSet;
import java.util.Set;

public class Rule {
    private final String name;
    private final RuleActivation activation;    // Determines when to activate the rule
    private final Set<Action> actionsSet;             // Set of actions to be preformed when the rule is activated
    private final Counter tickCounter;

    public Rule(String name, RuleActivation activation) {
        this.name = name;
        this.activation = activation;
        this.actionsSet = new HashSet<>();
        this.tickCounter = new Counter(0);
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


    /**
     * Tries to activate the rule by invoking all actions defined in this rule.
     * The rule will be activated if both the defined amount of ticks has passed and the probability was achieved.
     * @param entity the current entity in which we try to invoke the rule.
     */
    public void invoke(Entity entity) {
        activation.generateProbability();

        tickCounter.increaseCount();

        // Check if the amount of ticks in the simulation have passed
        if((activation.getTicks() == tickCounter.getCount())) {
            tickCounter.resetCount();   // Reset counter every tick amount set.

            // Check if the probability was activated
            if(activation.isProbabilityActivated())
                actionsSet.forEach(
                        action -> {
                            // Invoke only actions that are within the received entity's context
                            if(entity.getName().equals(action.getEntityContext()))
                                action.invoke(entity);
                        }
                );
        }
    }
}
