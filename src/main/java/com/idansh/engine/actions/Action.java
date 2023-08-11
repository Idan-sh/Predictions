package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.property.instance.Property;
import com.idansh.engine.property.instance.PropertyType;

public abstract class Action {
    private final Entity entity;
    private final ActiveEnvironmentVariables activeEnvironmentVariables;

    public Action(Entity entity, ActiveEnvironmentVariables activeEnvironmentVariables) {
        this.entity = entity;
        this.activeEnvironmentVariables = activeEnvironmentVariables;
    }

    public Entity getContextEntity() {
        return entity;
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
