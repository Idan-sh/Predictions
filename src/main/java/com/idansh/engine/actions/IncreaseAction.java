package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.expression.Expression;
import com.idansh.engine.property.instance.Property;

/**
 * Increases the value of a numeric property of an entity.
 */
public class IncreaseAction extends Action {
    String propertyName;
    Expression amount;

    /**
     * @param entity the entity on which the action will be preformed.
     * @param propertyName name of the property whose value will be changed.
     * @param amount the amount to be added to the property's value.
     */
    public IncreaseAction(Entity entity, ActiveEnvironmentVariables activeEnvironmentVariables, String propertyName, Expression amount) {
        super(entity, activeEnvironmentVariables);
        this.propertyName = propertyName;
        this.amount = amount;
    }

    public void invoke() {
        Property property = super.getContextEntity().getPropertyByName(propertyName);

        if(!super.isNumericProperty(property))
            throw new IllegalArgumentException("Error: can preform increase only on numeric property factories!");

        // todo- preform increase on the property
        // todo- make sure after the increase the value is within the property range
    }
}
