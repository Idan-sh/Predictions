package com.idansh.engine.expression.functions;

import com.idansh.engine.entity.Entity;
import com.idansh.engine.property.instance.PropertyType;

import java.util.Random;

/**
 * Generate a random integer between 0 and the received integer.
 */
public class RandomFunctionExpression extends FunctionActivationExpression {
    final int maxValue;

    /**
     * @param maxValue an integer that is greater than 0
     */
    public RandomFunctionExpression(int maxValue) {
        super(Type.RANDOM);
        this.maxValue = maxValue;
    }


    /**
     * @return randomly generated integer between 0 and max
     */
    @Override
    public Object getValue(Entity ignored) {
        Random random = new Random();
        return random.nextInt(maxValue + 1);
    }


    @Override
    public Object getValue(Entity ignored1, Entity ignored2) {
        Random random = new Random();
        return random.nextInt(maxValue + 1);
    }


    @Override
    public PropertyType getType() {
        return PropertyType.INTEGER;
    }
}
