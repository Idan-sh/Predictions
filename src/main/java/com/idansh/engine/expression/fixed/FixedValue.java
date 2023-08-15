package com.idansh.engine.expression.fixed;

import com.idansh.engine.expression.api.Expression;

/**
 * An expression with a fixed value.
 * Can be numeric/boolean/string, depends on its context
 */
public class FixedValue implements Expression {
    private final Object value;

    public FixedValue(Object value) {
        this.value = value;
    }
}
