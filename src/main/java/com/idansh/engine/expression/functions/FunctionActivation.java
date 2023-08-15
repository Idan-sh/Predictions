package com.idansh.engine.expression.functions;

import com.idansh.engine.expression.api.Expression;

/**
 * An expression that activates an helper function.
 */
public abstract class FunctionActivation implements Expression {
    enum Type{
        ENVIRONMENT, RANDOM, EVALUATE, PERCENT, TICKS
    }
    Type type;

    public FunctionActivation(Type type) {
        this.type = type;
    }
}
