package com.idansh.engine.handler;

import com.idansh.engine.expression.Expression;

public interface EngineHandler {

    /* Functions */

    /**
     * Increases the value of a numeric property of a certain entity.
     * @param entityName name of the entity
     * @param propertyName name of the property whose value will be changed
     * @param amount the amount to be added to the property's value
     */
    public void increase(String entityName, String propertyName, Expression amount);


    /**
     * Decreases the value of a numeric property of a certain entity.
     * @param entityName name of the entity
     * @param propertyName name of the property whose value will be changed
     * @param amount the amount to be subtracted from the property's value
     */
    public void decrease(String entityName, String propertyName, Expression amount);


    /**
     * Perform a mathematical calculation on a value of a property of a certain entity.
     * Possible calculations: Multiply / Divide
     * Receives two argument numbers (arg1, arg2) that will be used for the calculation.
     * @param entityName name of the entity
     * @param propertyName name of the property that will hold the result of the calculation
     */
    public void calculation(String entityName, String propertyName, Expression arg1, Expression arg2);


    /**
     * Checks if a single condition is met,
     * if the condition is met then the received actions will be performed.
     * Referred by the singularity value "single"
     * @param entityName name of the entity
     * @param propertyName name of the property that will be checked by the condition
     * @param operator comparison operator for the condition.
     *                 possible values:  = (equal) / != (not equal) / bt (greater than) / lt (less than)
     * @param value a number that will be compared to the property's value
     */
    public void condition(String entityName, String propertyName, String operator, Expression value);


    /**
     * Allows for multiple conditioning, using both simple and multiple conditions.
     * This condition does not contain a specific condition to be tested, but allows for the OR / AND logic operands
     * to compile multiple simple conditions into a bigger block.
     * Referred by the singularity value "multiple"
     * @param logicOp the logic operand OR/AND that will be used
     */
    public void condition(String logicOp);

    /**
     * Sets the value of any property of a certain entity.
     * @param entityName name of the entity
     * @param propertyName name of the property whose value will be changed
     * @param amount the amount to be subtracted from the property's value
     */
    public void set(String entityName, String propertyName, Expression amount);


    /**
     * Kills an entity (removes it from the world)
     * @param entityName name of the entity
     */
    public void kill(String entityName);
}
