package com.idansh.engine.expression.functions;

import java.util.Random;

/**
 * Generate a random integer between 0 and the received integer.
 */
public class RandomFunctionExpression extends FunctionActivationExpression {
    final int maxValue;

    /**
     * @param max an integer that is greater than 0
     */
    public RandomFunctionExpression(int maxValue) {
        super(Type.RANDOM);
        this.maxValue = maxValue;
    }


    /**
     * @return randomly generated integer between 0 and max
     */
    @Override
    public Object getValue() {
        Random random = new Random();
        return random.nextInt(maxValue + 1);    }
}
