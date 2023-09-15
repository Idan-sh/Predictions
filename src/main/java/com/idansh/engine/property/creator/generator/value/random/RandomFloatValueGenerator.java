package com.idansh.engine.property.creator.generator.value.random;

import com.idansh.engine.helpers.RandomValue;
import com.idansh.engine.helpers.Range;
import com.idansh.engine.property.creator.generator.value.api.ValueGenerator;

/**
 * Value generator for random float values.
 */
public class RandomFloatValueGenerator implements ValueGenerator<Float> {
    private final Range range;

    public RandomFloatValueGenerator(Range range) {
        if(range == null) {
            throw new IllegalArgumentException("Cannot create Random Float Value Generator, received no range!");
        }
        this.range = range;
    }

    @Override
    public Float generateValue() {
        return RandomValue.getRandomFloat(range.getBottom(), range.getTop());
    }
}
