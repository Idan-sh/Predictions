package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;
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
        public static Action.Type getType(String s) {
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
    private final String entityContext;
    private final World worldContext;

    public Action(World worldContext, String entityContext) {
        // Check if there is an entity factory with the name of that value of entityContext
        worldContext.entityManager.getEntityFactory(entityContext);

        this.entityContext = entityContext;
        this.worldContext = worldContext;
    }

    public String getEntityContext() {
        return entityContext;
    }


    public World getWorldContext() {
        return worldContext;
    }


    /**
     * Invokes the action, according to the action's type.
     * The action will be invoked on every instance of the defined entity in the population of the simulation.
     * @param entity the entity instance on which the action will be performed.
     */
    public abstract void invoke(Entity entity);


    /**
     * @return The type of action.
     */
    public abstract String getActionTypeString();
}
