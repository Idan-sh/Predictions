package com.idansh.engine.entity;

import com.idansh.engine.actions.condition.ConditionAction;
import com.idansh.engine.helpers.RandomValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Defines how a secondary entity can be chosen within an Action.
 * Can Choose secondary entities from the population according to the settings that
 * were set up during the object's creation.
 */
public class SecondaryEntity {
    private final String name;                      // Name of the secondary entity
    private final Integer amount;                   // Amount of secondary entity instances to choose
    private final List<ConditionAction> conditions; // Optional conditions on which secondary entity instances to choose

    /**
     * Create instruction on how to choose secondary entities.
     * @param amount String containing a positive number, or the String "ALL".
     */
    public SecondaryEntity(String name, String amount) {
        this.name = name;
        this.conditions = new ArrayList<>();

        // Get the amount of secondary entities to choose
        if(amount.equals("ALL"))
            this.amount = -1;
        else {
            try {
                this.amount = Integer.parseInt(amount);

                if (this.amount < 1)
                    throw new IllegalArgumentException("Received negative secondary entity amount of \"" + amount + "\"." +
                            " Accepts only positive numbers, or the String \"ALL\".");
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Received invalid secondary entity amount of \"" + amount + "\"." +
                        " Please set a positive number or \"ALL\".");
            }
        }
    }

    /**
     * Create empty secondary entity with only a name.
     */
    public SecondaryEntity(String name) {
        this.name = name;
        this.amount = null;
        this.conditions = null;
    }

    public void addNewConditionAction(ConditionAction conditionAction) {
        conditions.add(conditionAction);
    }


    /**
     * Create a list of secondary entity instances from the population, by the received size and conditions.
     */
    public List<Entity> chooseSecondaryEntitiesFromPopulation(EntityManager entityManager) {
        List<Entity> chosenSecondaryEntities = new ArrayList<>();
        List<Entity> allSecondaryEntityInstances = entityManager.getAllEntityInstancesInPopulation(name);
        List<Entity> secondaryEntityInstancesThatConditionApplies = new ArrayList<>();

        int amountChosen = 0;
        int amountToChoose = amount;
        if(amountToChoose == -1)
            return allSecondaryEntityInstances; // Choose all population

        // Go through allSecondaryEntityInstances, create new list only with the entities that the conditions are activated on
        for (Entity entity : allSecondaryEntityInstances) {
            for (ConditionAction conditionAction : conditions) {
                conditionAction.invoke(entity);

                if(conditionAction.isActivated()) {
                    secondaryEntityInstancesThatConditionApplies.add(entity);
                }
            }
        }

        // The maximum amount that is possible to choose is the amount of secondary entity instances in the population
        amountToChoose = Math.min(amountToChoose, secondaryEntityInstancesThatConditionApplies.size());

        System.out.println("\n\nstarting loop");
        while (amountChosen < amountToChoose) {
            System.out.println("im in secondary entity loop wheeee");
            int rndIndex = RandomValue.getRandomInt(0, secondaryEntityInstancesThatConditionApplies.size() - 1); // Choose a random index in the population
            Entity entityChosen = secondaryEntityInstancesThatConditionApplies.get(rndIndex);
            chosenSecondaryEntities.add(entityChosen);
            amountChosen++;
        }
        System.out.println("finished loop :(");

        return chosenSecondaryEntities;
    }

    public String getName() {
        return name;
    }
}
