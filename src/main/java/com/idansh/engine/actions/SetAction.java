package com.idansh.engine.actions;

import com.idansh.engine.expression.Expression;

public class SetAction {
    /**
     * Sets the value of a property (of any type) of the entity.
     * @param propertyName name of the property whose value will be changed
     * @param amount the amount to be subtracted from the property's value
     */
    public void set(String propertyName, Expression amount);

}
