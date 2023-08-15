package com.idansh.engine.expression.property;

import com.idansh.engine.expression.api.Expression;

/**
 * An expression that is a property name of the main entity in context.
 */
public class PropertyExpression implements Expression {
    private final String propertyName;

    public PropertyExpression(String propertyName) {
        this.propertyName = propertyName;
    }


    /**
     */
    public int invoke() {
        // todo- complete invoke
    }
}
