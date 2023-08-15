package com.idansh.engine.expression.functions;

import java.util.Random;

/**
 * Generate a random integer between 0 and the received integer.
 */
public class RandomFunction extends FunctionActivation{
    final int maxValue;

    /**
     * @param max an integer that is greater than 0
     */
    public RandomFunction(int maxValue) {
        super(Type.RANDOM);
        this.maxValue = maxValue;
    }


    /**
     * @return randomly generated integer between 0 and max
     */
    public int invoke() {
        Random random = new Random();
        return random.nextInt(maxValue + 1);
    }
}
