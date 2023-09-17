package com.idansh.engine.expression.functions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.expression.api.Expression;
import com.idansh.engine.property.instance.PropertyType;

/**
 * An expression that activates an helper function.
 */
public abstract class FunctionActivationExpression implements Expression {
    public enum Type{
        ENVIRONMENT, RANDOM, EVALUATE, PERCENT, TICKS;

        /**
         * Converts a string of a function activation to
         * its corresponding function activation type.
         * @param s the function activation type in string format.
         * @return FunctionActivationExpression.Type that defines the string received.
         * @throws IllegalArgumentException if the string received is in invalid format (not one of the specified strings).
         */
        public static FunctionActivationExpression.Type getType(String s) {
            switch(s) {
                case "environment":
                    return Type.ENVIRONMENT;

                case "random":
                    return Type.RANDOM;

                case "evaluate":
                    return Type.EVALUATE;

                case "percent":
                    return Type.PERCENT;

                case "ticks":
                    return Type.TICKS;

                default:
                    throw new IllegalArgumentException("Error: invalid property type- \"" + s + "\"");
            }
        }
    }

    private final Type type;

    public FunctionActivationExpression(Type type) {
        this.type = type;
    }

    /**
     * @return The type of the expression.
     */
    public Type getFunctionType() {
        return type;
    }

    @Override
    public abstract PropertyType getType();

    @Override
    public abstract Object getValue(Entity entityInstance);
}
