package com.idansh.engine.actions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.environment.ActiveEnvironmentVariables;
import com.idansh.engine.expression.Expression;

/**
 * Sets the value of a property (of any type) of an entity.
 */
public class SetAction extends Action {
    private final String propertyName;
    private final Expression amount;


    /**
     * @param propertyName name of the property whose value will be changed
     * @param amount the amount to be subtracted from the property's value
     */
    public SetAction(Entity entity, ActiveEnvironmentVariables activeEnvironmentVariables, String propertyName, Expression amount) {
        super(entity, activeEnvironmentVariables);
        this.propertyName = propertyName;
        this.amount = amount;
    }


    public void invoke() {
        // todo- complete set action
    }

}
