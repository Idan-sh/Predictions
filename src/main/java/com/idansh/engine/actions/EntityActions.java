package com.idansh.engine.actions;

import com.idansh.engine.expression.Expression;

/**
 * Actions that will be preformed in the context of an entity instance.
 */
public interface EntityActions {
    /**
     * Increases the value of a numeric property of the entity.
     * @param propertyName name of the property whose value will be changed
     * @param amount the amount to be added to the property's value
     */
    public void increase(String propertyName, Expression amount);


    /**
     * Decreases the value of a numeric property of the entity.
     * @param propertyName name of the property whose value will be changed
     * @param amount the amount to be subtracted from the property's value
     */
    public void decrease(String propertyName, Expression amount);


    /**
     * Perform a mathematical calculation on a value of a property of the entity.
     * Possible calculations: Multiply / Divide
     * Receives two argument numbers (arg1, arg2) that will be used for the calculation.
     * @param propertyName name of the property that will hold the result of the calculation
     */
    public void calculation(String propertyName, Expression arg1, Expression arg2);


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


    /**
     * Sets the value of a property (of any type) of the entity.
     * @param propertyName name of the property whose value will be changed
     * @param amount the amount to be subtracted from the property's value
     */
    public void set(String propertyName, Expression amount);


    /**
     * Kills a single entity from the population.
     */
    public void kill();
}
