package com.idansh.engine.expression.functions;

import com.idansh.engine.expression.api.Expression;

/**
 * An expression that activates an helper function.
 */
public abstract class FunctionActivationExpression implements Expression {
    public enum Type{
        ENVIRONMENT, RANDOM, EVALUATE, PERCENT, TICKS
    }
    private Type type;

    public FunctionActivationExpression(Type type) {
        this.type = type;
    }

    /**
     * @return The type of the expression.
     */
    public Type getType() {
        return type;
    }
}
