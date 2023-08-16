package com.idansh.engine.expression.fixed;

import com.idansh.engine.expression.api.Expression;

/**
 * An expression with a fixed value.
 * Can be numeric/boolean/string, depends on its context
 */
public class FixedValueExpression implements Expression {
    private final Object value;

    public FixedValueExpression(Object value) {
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
