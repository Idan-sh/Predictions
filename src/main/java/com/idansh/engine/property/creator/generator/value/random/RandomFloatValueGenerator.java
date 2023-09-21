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
        if (range != null && range.getBottom() > range.getTop())
            throw new RuntimeException("Cannot create random float value generator, received range of bottom value " + range.getBottom() + " that is higher than the top value " + range.getTop());

        this.range = range;
    }

    @Override
    public Float generateValue() {
        if (range == null)
            return RandomValue.getRandomFloatWithoutRange();

        return RandomValue.getRandomFloatFromRange(range.getBottom(), range.getTop());
    }
}
