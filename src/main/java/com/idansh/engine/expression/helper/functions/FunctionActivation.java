package com.idansh.engine.expression.helper.functions;

import com.idansh.engine.expression.Expression;
import com.idansh.engine.world.World;

import java.util.Random;

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
