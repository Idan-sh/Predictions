package com.idansh.engine.expression.property;

import com.idansh.engine.expression.api.Expression;

import java.util.Random;

/**
 * An expression that is a property name of the main entity in context.
 */
public class PropertyExpression implements Expression {
    private final String propertyName;

    public PropertyExpression(String propertyName) {
        this.propertyName = propertyName;
    }


    // todo- complete invoke
    /**
     * @return randomly generated integer between 0 and max
     */
    public int invoke() {
        Random random = new Random();
        return random.nextInt(maxValue + 1);
    }
}
