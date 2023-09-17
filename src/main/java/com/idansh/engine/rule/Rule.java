package com.idansh.engine.rule;

import com.idansh.engine.actions.Action;
import com.idansh.engine.entity.Entity;
import com.idansh.engine.helpers.Counter;
import com.idansh.engine.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Rule {
    private final String name;
    private final RuleActivation activation;    // Determines when to activate the rule
    private final List<Action> actionsList;       // Set of actions to be preformed when the rule is activated
    private final Counter tickCounter;
    private final World worldContext;

    public Rule(String name, RuleActivation activation, World worldContext) {
        this.name = name;
        this.activation = activation;
        this.actionsList = new ArrayList<>();
        this.tickCounter = new Counter(0);
        this.worldContext = worldContext;
    }

    /**
     * Copy constructor that creates a new rule from the given rule,
     * while setting the world context as the given world context.
     */
    public Rule(Rule rule, World worldContext) {
        this.name = rule.name;
        this.activation = new RuleActivation(rule.getActivation());
        this.actionsList = new ArrayList<>();
        copyActionsSet(rule, worldContext);
        this.tickCounter = new Counter(0);
        this.worldContext = worldContext;
    }

    private void copyActionsSet(Rule rule, World worldContext) {
        for(Action action : rule.getActionsList()) {
            this.actionsList.add(action.copy(worldContext));
        }
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
        if(actionsList.contains(action))
            throw new IllegalArgumentException("action " + action.getClass() + " already exists in the rules set!");

        actionsList.add(action);
    }

    public List<Action> getActionsList() {
        return actionsList;
    }


    /**
     * Tries to activate the rule by invoking all actions defined in this rule.
     * The rule will be activated if both the defined amount of ticks has passed and the probability was achieved.
     * @param entity the current entity in which we try to invoke the rule.
     */
    public void invoke(Entity entity) {
        activation.generateProbability();

        // Check if the rule's activation is activated
        if (activation.isActivated(tickCounter)) {
            for (Action action : actionsList) {
                // Invoke only actions that are within the received main entity's context
                if (entity.getName().equals(action.getMainEntityContext()))  {
                    // Check if a secondary entity was defined on this action
                    if(action.getSecondaryEntity() == null) {
                        action.invoke(entity);
                    }
                    else {
                        List<Entity> chosenEntities = action.getSecondaryEntity().chooseSecondaryEntitiesFromPopulation(worldContext.entityManager);

                        for (Entity chosenEntity : chosenEntities) {
                            action.invoke(entity, chosenEntity);
                        }
                    }
                }
            }
        }
    }


    /**
     * On each tick of the simulation, also tick each rule,
     * to allow each rule to properly check its activation rule.
     */
    public void increaseTickCounter() {
        tickCounter.increaseCount();
    }


    /**
     * Checks if the tick counter of the rule has reached the activation's ticks,
     * if so then resets it back to 0.
     * Call this method after invoking the rule on all the population of the world.
     */
    public void resetTickCounter() {
        if (tickCounter.getCount() == activation.getTicks())
            tickCounter.resetCount();   // Reset counter every tick amount set.
    }
}
