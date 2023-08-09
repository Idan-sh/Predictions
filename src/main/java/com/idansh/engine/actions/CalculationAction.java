package com.idansh.engine.actions;

import com.idansh.engine.expression.Expression;

/**
 * Perform a mathematical calculation on a value of a property of the entity.
 * Possible calculations: Multiply / Divide
 * Receives two argument numbers (arg1, arg2) that will be used for the calculation.
 * @param propertyName name of the property that will hold the result of the calculation
 */
public class CalculationAction {
    public void calculation(String propertyName, Expression arg1, Expression arg2);

}
