package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.entity.SecondaryEntity;
import com.idansh.engine.world.World;

public abstract class Action {
    public enum Type{
        CALCULATION, CONDITION, DECREASE, INCREASE, SET, KILL, REPLACE, PROXIMITY;

        /**
         * Converts a string of an action to its corresponding Action Type.
         * @param s the action type in string format.
         * @return Action.Type that defines the string received.
         * @throws IllegalArgumentException if the string received is in invalid format (not one of the specified strings).
         */
        public static Type getType(String s) {
            switch(s) {
                case "increase":
                    return Type.INCREASE;

                case "decrease":
                    return Type.DECREASE;

                case "calculation":
                    return Type.CALCULATION;

                case "condition":
                    return Type.CONDITION;

                case "set":
                    return Type.SET;

                case "kill":
                    return Type.KILL;

                case "replace":
                    return Type.REPLACE;

                case "proximity":
                    return Type.PROXIMITY;

                default:
                    throw new IllegalArgumentException("cannot convert received string to action type! received string is \"" + s + "\".");
            }
        }
    }
    private final String mainEntityContext;
    private final World worldContext;
    private final SecondaryEntity secondaryEntity; // Optional
    private final String entityToInvokeOn;


    public Action(World worldContext, String mainEntityContext, String entityToInvokeOn) {
        this.mainEntityContext = mainEntityContext;
        this.worldContext = worldContext;
        this.secondaryEntity = null;

        checkEntityToInvokeOn(entityToInvokeOn);
        this.entityToInvokeOn = entityToInvokeOn;

        checkEntityContext(mainEntityContext);
    }

    public Action(World worldContext, String mainEntityContext, SecondaryEntity secondaryEntity, String entityToInvokeOn) {
        this.mainEntityContext = mainEntityContext;
        this.worldContext = worldContext;
        this.secondaryEntity = secondaryEntity;

        checkEntityToInvokeOn(entityToInvokeOn);
        this.entityToInvokeOn = entityToInvokeOn;

        checkEntityContext(mainEntityContext);
    }

    private void checkEntityToInvokeOn(String entityToInvokeOn) {
        if (!entityToInvokeOn.equals(mainEntityContext)) {
            if (secondaryEntity != null && !entityToInvokeOn.equals(secondaryEntity.getName()))
                throw new IllegalArgumentException("Invalid action received in the XML file, " +
                        "cannot create action on entity \"" + entityToInvokeOn + "\" which is not the main or secondary entity!" +
                        "\nMain entity: " + mainEntityContext + "\nSecondary entity: " + secondaryEntity.getName());
        }
    }

    /**
     * Check if there is an entity factory with the name of the value of the received entityContext.
     */
    public void checkEntityContext(String entityContext) {
        worldContext.entityManager.getEntityFactory(entityContext);
    }


    /**
     * Invokes the action, according to the action's type.
     * @param entity the entity instance on which the action will be performed.
     */
    public abstract void invoke(Entity entity);


    /**
     * Invokes the action, according to the action's type.
     * @param mainEntity The main entity instance on which the action will be performed.
     * @param secondaryEntity The secondary entity instance on which the action will be performed.
     */
    public abstract void invoke(Entity mainEntity, Entity secondaryEntity);


    /**
     * @return The type of action.
     */
    public abstract String getActionTypeString();


    /**
     * Deep copies this Action instance, using a new worldContext for the action's activation.
     */
    public abstract Action copy(World worldContext);


    public String getMainEntityContext() {
        return mainEntityContext;
    }

    public SecondaryEntity getSecondaryEntity() {
        return secondaryEntity;
    }

    public World getWorldContext() {
        return worldContext;
    }

    public String getEntityToInvokeOn() {
        return entityToInvokeOn;
    }
}
