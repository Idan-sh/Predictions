package com.idansh.engine.actions.condition;

import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.world.World;

public class SingleConditionAction extends ConditionAction{
    private final String propertyName;
    private final String operator;
    private final Expression value;

    /**
     * Checks if a single condition is met,
     * if the condition is met then the received actions will be performed.
     * Referred by the singularity value "single"
     *
     * @param propertyName name of the property that will be checked by the condition
     * @param operator     comparison operator for the condition.
     *                     possible values:  = (equal) / != (not equal) / bt (greater than) / lt (less than)
     * @param value        a number that will be compared to the property's value
     */
    public SingleConditionAction(World worldContext, String entityContext, String propertyName, String operator, Expression value) {
        super(worldContext, entityContext, Type.SINGLE);
        this.propertyName = propertyName;
        this.operator = operator;
        this.value = value;
    }
}
