package com.idansh.engine.expression.api;

import com.idansh.engine.expression.functions.FunctionActivationExpression;

/**
 * Base interface for - helper functions / entity's property / a free value.
 */
public interface Expression {
    /**
     * @return The value of the expression
     */
    Object getValue();
}
