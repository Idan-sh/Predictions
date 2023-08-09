package com.idansh.engine.actions;

import com.idansh.engine.expression.Expression;


public class ConditionAction {
    /**
     * Checks if a single condition is met,
     * if the condition is met then the received actions will be performed.
     * Referred by the singularity value "single"
     * @param propertyName name of the property that will be checked by the condition
     * @param operator comparison operator for the condition.
     *                 possible values:  = (equal) / != (not equal) / bt (greater than) / lt (less than)
     * @param value a number that will be compared to the property's value
     */
    public void condition(String propertyName, String operator, Expression value);


    /**
     * Allows for multiple conditioning, using both simple and multiple conditions.
     * This condition does not contain a specific condition to be tested, but allows for the OR / AND logic operands
     * to compile multiple simple conditions into a bigger block.
     * Referred by the singularity value "multiple"
     * @param logicOp the logic operand OR/AND that will be used
     */
    public void condition(String logicOp);
}
