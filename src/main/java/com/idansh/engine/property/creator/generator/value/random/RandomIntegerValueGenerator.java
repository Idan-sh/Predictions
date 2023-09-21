package com.idansh.engine.property.creator.generator.value.random;

import com.idansh.engine.helpers.RandomValue;
import com.idansh.engine.helpers.Range;
import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;

/**
 * Value generator for random integer values.
 */
public class RandomIntegerValueGenerator implements ValueGenerator<Integer> {
    private final Range range;

    public RandomIntegerValueGenerator(Range range) {
        this.range = range;
    }

    @Override
    public Integer generateValue() {
        if (range == null)
            return (int) RandomValue.getRandomFloatWithoutRange();

        return RandomValue.getRandomInt(range.getBottom(), range.getTop());
    }
}
