package com.idansh.engine.actions;

import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.world.World;


public class ConditionAction extends Action {
    private enum Type {
        SINGLE, MULTI
    }

    private final String propertyName;
    private final String operator;
    private final Expression value;
    private final String logicOp;
    private final Type type;

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
    public ConditionAction(World worldContext, String entityContext, String propertyName, String operator, Expression value) {
        super(worldContext, entityContext);
        this.type = Type.SINGLE;
        this.propertyName = propertyName;
        this.operator = operator;
        this.value = value;
        this.logicOp = null;
    }


    /**
     * Allows for multiple conditioning, using both simple and multiple conditions.
     * This condition does not contain a specific condition to be tested, but allows for the OR / AND logic operands
     * to compile multiple simple conditions into a bigger block.
     * Referred by the singularity value "multiple"
     *
     * @param logicOp the logic operand OR/AND that will be used
     */
    public ConditionAction(World worldContext, String entityContext, String logicOp) {
        super(worldContext, entityContext);
        this.type = Type.MULTI;
        this.logicOp = logicOp;
        this.propertyName = null;
        this.operator = null;
        this.value = null;
    }

    public void invoke() {
        // todo- complete condition action
        // todo- maybe check if the new value is within a range for numeric properties?
    }
}