package com.idansh.engine.actions;

import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;
import com.idansh.engine.world.World;

public abstract class Action {
    public enum Type{
        CALCULATION, CONDITION, DECREASE, INCREASE, SET, KILL, REPLACE, PROXIMITY
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
     */
    abstract void invoke();


    /**
     * Checks if a given property is of the type FLOAT or INTEGER.
     * @return true if the property factory given is numeric, false otherwise.
     */
    protected boolean isNumericProperty(Property property) {
        return PropertyType.INTEGER.equals(property.getType()) || PropertyType.FLOAT.equals(property.getType());
    }
}
